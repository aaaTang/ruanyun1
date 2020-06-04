package cn.ruanyun.backInterface.modules.rongyun.serviceimpl;

import cn.ruanyun.backInterface.common.exception.RuanyunException;
import cn.ruanyun.backInterface.common.utils.EmptyUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.business.group.pojo.Group;
import cn.ruanyun.backInterface.modules.business.group.service.IGroupService;
import cn.ruanyun.backInterface.modules.business.platformServicer.pojo.PlatformServicer;
import cn.ruanyun.backInterface.modules.business.platformServicer.service.IPlatformServicerService;
import cn.ruanyun.backInterface.modules.business.storeServicer.pojo.StoreServicer;
import cn.ruanyun.backInterface.modules.business.storeServicer.service.IStoreServicerService;
import cn.ruanyun.backInterface.modules.rongyun.DTO.GroupInfoCreate;
import cn.ruanyun.backInterface.modules.rongyun.DTO.GroupUser;
import cn.ruanyun.backInterface.modules.rongyun.DTO.GroupUserVO;
import cn.ruanyun.backInterface.modules.rongyun.mapper.RongyunMapper;
import cn.ruanyun.backInterface.modules.rongyun.pojo.Rongyun;
import cn.ruanyun.backInterface.modules.rongyun.service.IRongyunService;
import com.alipay.api.domain.UserVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dm.jdbc.util.StringUtil;
import io.rong.RongCloud;
import io.rong.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;


/**
 * 融云接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IRongyunServiceImpl extends ServiceImpl<RongyunMapper, Rongyun> implements IRongyunService {


    RongCloud imClient = null;

    @Value("${spring.rong-cloud.app-key}")
    private String appkey;

    @Value("${spring.rong-cloud.app-secret}")
    private  String appSecret;


    @Autowired
    private IUserService userService;

    @Autowired
    private IUserRoleService iUserRoleService;

    @Autowired
    private IStoreServicerService iStoreServicerService;

    @Autowired
    private IPlatformServicerService iPlatformServicerService;

    @Autowired
    private IGroupService iGroupService;

    @Autowired
    private IUserService iUserService;

    @PostConstruct
    public void init() {

        imClient = RongCloud.getInstance(appkey, appSecret);

    }

    @Override
    public void addUser(String id, String name, String portrait) throws RuanyunException {
        try {

            TokenResult result = imClient.user.getToken(id, name, portrait);
            if(result.getCode() == 200){

                Optional.ofNullable(userService.getById(id))
                        .ifPresent(user -> {

                            user.setImToken(result.getToken());
                            userService.updateById(user);
                        });

            }else{
                throw new RuanyunException("同步注册im用户出错");
            }
        } catch (Exception e) {
            throw new RuanyunException("系统异常");
        }
    }

    @Override
    public boolean updateUser(String id, String name, String portrait) throws RuanyunException {
        try {

            CodeSuccessResult result = imClient.user.refresh(id, name, portrait);
            if(result.getCode() == 200){
                return true;
            }else{
                throw new RuanyunException("同步更新im用户出错");
            }
        } catch (Exception e) {
            throw new RuanyunException("系统异常");
        }
    }


    @Override
    public String getToken(String userId, String name, String portraitUri) throws RuanyunException {

        try {

            TokenResult result = imClient.user.getToken(userId, name, portraitUri);
            if (result.getCode() == 200) {

                return result.getToken();
            }else {

                throw new RuanyunException("获取用户token失败！");
            }
        }catch (Exception e) {

            throw new RuanyunException("系统异常");
        }
    }

    @Override
    public String checkOnlineResult(String userId) throws RuanyunException {

        try {

            CheckOnlineResult result = imClient.user.checkOnline(userId);
            if (result.getCode() == 200) {

                return result.getStatus();
            }else {

                throw new RuanyunException("获取用户在线状态失败！");
            }
        }catch (Exception e) {

            throw new RuanyunException("系统异常");
        }
    }

    @Override
    public GroupInfoCreate createGroup(String userId, String merchantId) {
        // 创建群组返回数据
        GroupInfoCreate groupInfoCreate = new GroupInfoCreate();

        // 群组用户列表
        List<GroupUser> groupUsers = new ArrayList<>();

        // 查询是否建过群
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("store_id", merchantId);
        List<Group> groups = iGroupService.listByMap(map);

        System.out.println("是否建群"+ groups);

        // 建过群，检查是否有商检客服
        if(groups.size() > 0){
            Group group = groups.get(0);

            // 群组中没有商家客服，检查商家有没有客服
            if(EmptyUtil.isEmpty(group.getStoreServicerId())){
                // 查询商家客服
                Map<String, Object> map2 = new HashMap<>();
                map2.put("store_id", merchantId);
                List<StoreServicer> storeServicers = iStoreServicerService.listByMap(map2);

                // 商家有客服
                if(storeServicers.size() > 0){
                    int chooseDindex = new Random().nextInt(storeServicers.size());
                    // 选择客服ID
                    String merchantServiceId = storeServicers.get(chooseDindex).getServicerId();
                    try {
                        CodeSuccessResult result = imClient.group.join(new String[]{merchantServiceId}, group.getGroupId(), group.getGroupName());
                        if(result.getCode() == 200){
                            group.setStoreServicerId(merchantServiceId);
                            ThreadPoolUtil.getPool().execute(()->iGroupService.updateById(group));
                            Optional.ofNullable(userService.getById(merchantServiceId))
                                    .ifPresent(user -> groupUsers.add(new GroupUser(user.getId(), user.getAvatar(), user.getNickName(), "merchantServicer")));

                            Optional.ofNullable(userService.getById(group.getPlatformServicerId()))
                                    .ifPresent(user -> groupUsers.add(new GroupUser(user.getId(), user.getAvatar(), user.getNickName(), "platformServicer")));

                            Optional.ofNullable(userService.getById(group.getUserId()))
                                    .ifPresent(user -> groupUsers.add(new GroupUser(user.getId(), user.getAvatar(), user.getNickName(), "user")));

                            Optional.ofNullable(userService.getById(group.getStoreId()))
                                    .ifPresent(user -> groupUsers.add(new GroupUser(user.getId(), user.getAvatar(), user.getNickName(), "merchant")));

                            groupInfoCreate.setGroupUsers(groupUsers);
                            groupInfoCreate.setGroupId(group.getGroupId());
                            groupInfoCreate.setGroupName(group.getGroupName());
                            return groupInfoCreate;
                        } else {
                            throw new RuanyunException("拉客服进群失败");
                        }
                    } catch (Exception e) {
                        throw new RuanyunException("系统异常");
                    }
                } else {
                    //商家没有客服
                    Optional.ofNullable(userService.getById(group.getPlatformServicerId()))
                            .ifPresent(user -> groupUsers.add(new GroupUser(user.getId(), user.getAvatar(), user.getNickName(), "platformServicer")));

                    Optional.ofNullable(userService.getById(group.getUserId()))
                            .ifPresent(user -> groupUsers.add(new GroupUser(user.getId(), user.getAvatar(), user.getNickName(), "user")));

                    Optional.ofNullable(userService.getById(group.getStoreId()))
                            .ifPresent(user -> groupUsers.add(new GroupUser(user.getId(), user.getAvatar(), user.getNickName(), "merchant")));

                    groupInfoCreate.setGroupUsers(groupUsers);
                    groupInfoCreate.setGroupId(group.getGroupId());
                    groupInfoCreate.setGroupName(group.getGroupName());
                    return groupInfoCreate;

                }
            } else {
                // 群组中有商家客服，直接返回
                Optional.ofNullable(userService.getById(group.getStoreServicerId()))
                        .ifPresent(user -> groupUsers.add(new GroupUser(user.getId(), user.getAvatar(), user.getNickName(), "merchantServicer")));

                Optional.ofNullable(userService.getById(group.getPlatformServicerId()))
                        .ifPresent(user -> groupUsers.add(new GroupUser(user.getId(), user.getAvatar(), user.getNickName(), "platformServicer")));

                Optional.ofNullable(userService.getById(group.getUserId()))
                        .ifPresent(user -> groupUsers.add(new GroupUser(user.getId(), user.getAvatar(), user.getNickName(), "user")));

                Optional.ofNullable(userService.getById(group.getStoreId()))
                        .ifPresent(user -> groupUsers.add(new GroupUser(user.getId(), user.getAvatar(), user.getNickName(), "merchant")));

                groupInfoCreate.setGroupUsers(groupUsers);
                groupInfoCreate.setGroupId(group.getGroupId());
                groupInfoCreate.setGroupName(group.getGroupName());
                return groupInfoCreate;
            }
        }

        // 商家客服
        String merchantServiceId = "";
        Map<String, Object> map2 = new HashMap<>();
        map2.put("store_id", merchantId);
        List<StoreServicer> storeServicers = iStoreServicerService.listByMap(map2);
        if(storeServicers.size() > 0){
            int chooseDindex = new Random().nextInt(storeServicers.size());
            merchantServiceId = storeServicers.get(chooseDindex).getServicerId();
        }

        // 要添加的群组成员
        List<String> members = new ArrayList<>();

        // 平台客服
        String platformServiceId;
        List<PlatformServicer> platformServicers = iPlatformServicerService.list();
        int chooseDindex = new Random().nextInt(platformServicers.size());
        platformServiceId = platformServicers.get(chooseDindex).getServicerId();
        Optional.ofNullable(userService.getById(platformServiceId))
                .ifPresent(user -> groupUsers.add(new GroupUser(user.getId(), user.getAvatar(), user.getNickName(), "platformServicer")));
        members.add(platformServiceId);

        // 用户
        Optional.ofNullable(userService.getById(userId))
                .ifPresent(user -> groupUsers.add(new GroupUser(user.getId(), user.getAvatar(), user.getNickName(), "user")));
        members.add(userId);

        // 商家客服
        if(!"".equals(merchantServiceId)){
            Optional.ofNullable(userService.getById(merchantServiceId))
                    .ifPresent(user -> groupUsers.add(new GroupUser(user.getId(), user.getAvatar(), user.getNickName(), "merchantServicer")));
            members.add(merchantServiceId);
        }

        // 商家
        Optional.ofNullable(userService.getById(merchantId))
                .ifPresent(user -> groupUsers.add(new GroupUser(user.getId(), user.getAvatar(), user.getNickName(), "merchant")));

        // 群组id
        // String groupId = ToolUtil.getRandomString(24);// 最大30位
        String groupId = merchantId + "," + userId;

        // 群组名称
        String groupName = userService.getById(merchantId).getNickName();

        groupInfoCreate.setGroupUsers(groupUsers);
        groupInfoCreate.setGroupId(groupId);
        groupInfoCreate.setGroupName(groupName);

        try {
            // 创建群组
            CodeSuccessResult result = imClient.group.create(members.toArray(new String[]{}), groupId, groupName);
            if(result.getCode() == 200){
                Group group = new Group();
                group.setGroupId(groupId);
                group.setUserId(userId);
                group.setPlatformServicerId(platformServiceId);
                group.setStoreServicerId(merchantServiceId);
                group.setStoreId(merchantId);
                group.setGroupName(groupName);
                iGroupService.insertOrderUpdateGroup(group);
                return groupInfoCreate;
            }else{
                throw new RuanyunException("创建群组失败");
            }
        } catch (Exception e) {
            throw new RuanyunException("系统异常");
        }
    }

    @Override
    public Object joinGroup(String groupId, String groupName, String[] member) {
        try {
            CodeSuccessResult result = imClient.group.join(member, groupId, groupName);
            if(result.getCode() == 200){
                Group group = iGroupService.getOne(Wrappers.<Group>lambdaQuery()
                        .eq(Group::getGroupId, groupId));
                String userId = group.getUserId();
                List<String> ids = Arrays.asList(userId.split(","));
                ids.addAll(Arrays.asList(member));
                group.setUserId(ids.toString());
                iGroupService.updateById(group);
                return new Object();
            }else{
                throw new RuanyunException("加入群组失败");
            }
        } catch (Exception e) {
            throw new RuanyunException("系统异常");
        }
    }

    @Override
    public Object updateGroup(String groupId, String groupName) {
        try {
            CodeSuccessResult result = imClient.group.refresh(groupId, groupName);
            if(result.getCode() == 200){
                Group group = iGroupService.getOne(Wrappers.<Group>lambdaQuery()
                        .eq(Group::getGroupId, groupId));
                group.setGroupName(groupName);
                iGroupService.updateById(group);
                return new Object();
            }else{
                throw new RuanyunException("更新群聊信息失败");
            }
        } catch (Exception e) {
            throw new RuanyunException("系统异常");
        }
    }

    @Override
    public Object quitGroup(String groupId, String[] member) {
        try {
            CodeSuccessResult result = imClient.group.quit(member, groupId);
            if(result.getCode() == 200){
                Group group = iGroupService.getOne(Wrappers.<Group>lambdaQuery()
                        .eq(Group::getGroupId, groupId));
                String userId = group.getUserId();
                List<String> ids = Arrays.asList(userId.split(","));
                for (String id: ids) {
                    for (String memberOne: member){
                        if (memberOne.equals(id)){
                            ids.remove(id);
                        }
                    }
                }
                iGroupService.updateById(group);
                return new Object();
            }else{
                throw new RuanyunException("退出群聊失败");
            }
        } catch (Exception e) {
            throw new RuanyunException("系统异常");
        }
    }

    @Override
    public Object dismissGroup(String groupId, String userId) {
        try {
            CodeSuccessResult result = imClient.group.dismiss(userId, groupId);
            if(result.getCode() == 200){
                Map<String, Object> removeGroup = new HashMap<>();
                removeGroup.put("group_id", groupId);
                boolean b = iGroupService.removeByMap(removeGroup);
                log.info("移除群组" + groupId + "结果: " + b);
                return new Object();
            }else{
                throw new RuanyunException("解散群聊失败");
            }
        } catch (Exception e) {
            throw new RuanyunException("系统异常");
        }
    }

    @Override
    public Object addGagUser(String userId, String groupId, String minute) {
        try {
            CodeSuccessResult result = imClient.group.addGagUser(userId, groupId, minute);
            if(result.getCode() == 200){
                return new Object();
            }else{
                throw new RuanyunException("禁言成功");
            }
        } catch (Exception e) {
            throw new RuanyunException("系统异常");
        }
    }

    @Override
    public Object lisGagUser(String groupId) {
        try {
            ListGagGroupUserResult listGagGroupUserResult = imClient.group.lisGagUser(groupId);
            // 禁言成员列表
             List<GagGroupUser> users = listGagGroupUserResult.getUsers();
            if(listGagGroupUserResult.getCode() == 200){
                return users;
            }else{
                throw new RuanyunException("查询被禁言成员成功");
            }
        } catch (Exception e) {
            throw new RuanyunException("系统异常");
        }
    }

    @Override
    public Object rollBackGagUser(String[] userId, String groupId) {
        try {
            CodeSuccessResult result = imClient.group.rollBackGagUser(userId, groupId);
            if(result.getCode() == 200){
                return new Object();
            }else{
                throw new RuanyunException("移除被禁言成员成功");
            }
        } catch (Exception e) {
            throw new RuanyunException("系统异常");
        }
    }

    @Override
    public Map<String, Object> getUserByGroupId(String groupId) {
        Group group = iGroupService.getOne(Wrappers.<Group>lambdaQuery()
                .eq(Group::getGroupId, groupId));
        System.out.println(group);
        String userId = group.getUserId();
        List<String> ids = Arrays.asList(userId.split(","));
        Map<String, Object> map = new HashMap<>();
        map.put("user", iUserService.listByIds(ids));


        map.put("store_servicer", iUserService.getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getId, group.getStoreServicerId())));

        map.put("platform_servicer", iUserService.getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getId, group.getPlatformServicerId())));

        return map;
    }

    @Override
    public Object getGroupListByUserId(String userId) {
        List<Group> groups;
        Map<String, Object> user1 = new HashMap<>();
        user1.put("user_id", userId);
        List<Group> group1 = iGroupService.listByMap(user1);

        if(group1.size() == 0){
            Map<String, Object> user2 = new HashMap<>();
            user2.put("store_servicer_id", userId);
            List<Group> group2 = iGroupService.listByMap(user2);
            if(group2.size() == 0){
                Map<String, Object> user3 = new HashMap<>();
                user3.put("platform_servicer_id", userId);
                List<Group> group3 = iGroupService.listByMap(user3);
                if(group3.size() == 0){
                    return new ArrayList<>();
                } else {
                    groups = group3;
                }
            } else {
                groups = group2;
            }
        } else {
            groups = group1;
        }

        List<Map<String, Object>> list = new ArrayList<>();
        for (Group group: groups) {
            Map<String, Object> back = new HashMap<>();
            back.put("group_id", group.getGroupId());
            back.put("group_name", group.getGroupName());

            List<User> userList = iUserService.listByIds(Arrays.asList(group.getUserId().split(",")));
            List<GroupUserVO> groupUserVOList = new ArrayList<>();
            for (User user : userList) {
                groupUserVOList.add(new GroupUserVO(user.getId(), user.getAvatar(), user.getNickName()));
            }
            back.put("user", Optional.ofNullable(ToolUtil.setListToNul(userList)).map(users -> users.get(0)).orElse(null));

            User store_servicer = iUserService.getOne(Wrappers.<User>lambdaQuery()
                    .eq(User::getId, group.getStoreServicerId()));
            if(EmptyUtil.isNotEmpty(store_servicer)){
                back.put("store_servicer", new GroupUserVO(store_servicer.getId(), store_servicer.getAvatar(), store_servicer.getNickName()));
            }

            User platform_servicer = iUserService.getOne(Wrappers.<User>lambdaQuery()
                    .eq(User::getId, group.getPlatformServicerId()));
            back.put("platform_servicer", new GroupUserVO(platform_servicer.getId(), platform_servicer.getAvatar(), platform_servicer.getNickName()));

            User store = iUserService.getOne(Wrappers.<User>lambdaQuery()
                    .eq(User::getId, group.getStoreId()));
            back.put("store", new GroupUserVO(store.getId(), store.getAvatar(), store.getNickName()));


            list.add(back);
        }
        return list;
    }


}