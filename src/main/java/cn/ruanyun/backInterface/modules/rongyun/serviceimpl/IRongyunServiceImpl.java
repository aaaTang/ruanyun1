package cn.ruanyun.backInterface.modules.rongyun.serviceimpl;

import cn.ruanyun.backInterface.common.exception.RuanyunException;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.business.platformServicer.pojo.PlatformServicer;
import cn.ruanyun.backInterface.modules.business.platformServicer.service.IPlatformServicerService;
import cn.ruanyun.backInterface.modules.business.storeServicer.pojo.StoreServicer;
import cn.ruanyun.backInterface.modules.business.storeServicer.service.IStoreServicerService;
import cn.ruanyun.backInterface.modules.rongyun.DTO.GroupInfoCreate;
import cn.ruanyun.backInterface.modules.rongyun.DTO.GroupUser;
import cn.ruanyun.backInterface.modules.rongyun.mapper.RongyunMapper;
import cn.ruanyun.backInterface.modules.rongyun.pojo.Rongyun;
import cn.ruanyun.backInterface.modules.rongyun.service.IRongyunService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

        // 商家客服
        String merchantServiceId = merchantId;
        Map<String, Object> map = new HashMap<>();
        map.put("store_id", merchantId);
        List<StoreServicer> storeServicers = iStoreServicerService.listByMap(map);
        if(storeServicers.size() > 0){
            int chooseDindex = new Random().nextInt(storeServicers.size());
            merchantServiceId = storeServicers.get(chooseDindex).getServicerId();
        }

        // 平台客服
        String platformServiceId;
        List<PlatformServicer> platformServicers = iPlatformServicerService.list();
        int chooseDindex = new Random().nextInt(platformServicers.size());
        platformServiceId = platformServicers.get(chooseDindex).getServicerId();

        GroupInfoCreate groupInfoCreate = new GroupInfoCreate();
        List<GroupUser> groupUsers = new ArrayList<>();

        Optional.ofNullable(userService.getById(merchantServiceId))
                .ifPresent(user -> groupUsers.add(new GroupUser(user.getId(), user.getAvatar(), user.getNickName(), "merchant")));

        Optional.ofNullable(userService.getById(platformServiceId))
                .ifPresent(user -> groupUsers.add(new GroupUser(user.getId(), user.getAvatar(), user.getNickName(), "platform")));

        Optional.ofNullable(userService.getById(userId))
                .ifPresent(user -> groupUsers.add(new GroupUser(user.getId(), user.getAvatar(), user.getNickName(), "user")));


        String [] users = {userId, merchantServiceId, platformServiceId};
        // 群组id
        String groupId = ToolUtil.getRandomString(24);// 最大30位

        // 群组名称
        String groupName = userService.getById(merchantServiceId).getShopName();

        groupInfoCreate.setGroupUsers(groupUsers);
        groupInfoCreate.setGroupId(groupId);
        groupInfoCreate.setGroupName(groupName);

        try {
            CodeSuccessResult result = imClient.group.create(users, groupId, groupName);
            if(result.getCode() == 200){
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
            // List<GagGroupUser> users = listGagGroupUserResult.getUsers();
            if(listGagGroupUserResult.getCode() == 200){
                return new Object();
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


}