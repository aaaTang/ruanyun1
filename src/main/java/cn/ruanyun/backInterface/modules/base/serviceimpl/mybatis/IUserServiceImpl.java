package cn.ruanyun.backInterface.modules.base.serviceimpl.mybatis;


import cn.hutool.core.util.ObjectUtil;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.UserTypeEnum;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.dto.UserDTO;
import cn.ruanyun.backInterface.modules.base.dto.UserUpdateDTO;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.pojo.UserRole;
import cn.ruanyun.backInterface.modules.base.service.UserService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IRolePermissionService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.base.vo.*;
import cn.ruanyun.backInterface.modules.business.area.service.IAreaService;
import cn.ruanyun.backInterface.modules.business.balance.service.IBalanceService;
import cn.ruanyun.backInterface.modules.business.followAttention.service.IFollowAttentionService;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import cn.ruanyun.backInterface.modules.business.good.serviceimpl.IGoodServiceImpl;
import cn.ruanyun.backInterface.modules.business.goodCategory.service.IGoodCategoryService;
import cn.ruanyun.backInterface.modules.business.goodService.pojo.GoodService;
import cn.ruanyun.backInterface.modules.business.goodService.service.IGoodServiceService;
import cn.ruanyun.backInterface.modules.business.myFavorite.service.IMyFavoriteService;
import cn.ruanyun.backInterface.modules.business.myFootprint.pojo.MyFootprint;
import cn.ruanyun.backInterface.modules.business.myFootprint.service.IMyFootprintService;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;
import cn.ruanyun.backInterface.modules.business.order.service.IOrderService;
import cn.ruanyun.backInterface.modules.business.selectStore.service.ISelectStoreService;
import cn.ruanyun.backInterface.modules.business.storeAudit.mapper.StoreAuditMapper;
import cn.ruanyun.backInterface.modules.business.storeAudit.pojo.StoreAudit;
import cn.ruanyun.backInterface.modules.business.userRelationship.pojo.UserRelationship;
import cn.ruanyun.backInterface.modules.business.userRelationship.service.IUserRelationshipService;
import cn.ruanyun.backInterface.modules.rongyun.service.IRongyunService;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.api.client.util.ArrayMap;
import com.google.api.client.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author fei
 */
@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserService userService;
    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IRolePermissionService rolePermissionService;
    @Autowired
    private IMyFavoriteService iMyFavoriteService;
    @Autowired
    private IMyFootprintService iMyFootprintService;
    @Autowired
    private IFollowAttentionService iFollowAttentionService;
    @Autowired
    private IRongyunService iRongyunService;
    @Autowired
    private IUserRelationshipService iUserRelationshipService;
    @Autowired
    private IGoodServiceImpl iGoodService;
    @Autowired
    private ISelectStoreService iSelectStoreService;
    @Autowired
    private IGoodCategoryService iGoodCategoryService;
    @Autowired
    private IAreaService iAreaService;
    @Resource
    private StoreAuditMapper storeAuditMapper;

    @Autowired
    private IBalanceService balanceService;

    @Autowired
    private IUserRelationshipService userRelationshipService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IMyFootprintService myFootprintService;


    @Override
    public String getUserIdByName(String userName) {

        return Optional.ofNullable(super.getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUsername, userName)))
                .map(User::getId)
                .orElse(null);
    }


    /**
     * APP通过邀请码获取用户信息
     * @param user
     * @return
     */
    @Override
    public Result<Object> appGetinvitationCode(UserDTO user) {

        if(ToolUtil.isNotEmpty(user.getInvitationCode())){

          return new ResultUtil<>().setData(Optional.ofNullable(
                  super.getOne(Wrappers.<User>lambdaQuery()
                          .eq(User::getInvitationCode, user.getInvitationCode())
                          ))
                  .map(User::getNickName).orElse(null),"获取成功！");
        }else {
            return new ResultUtil<>().setErrorMsg(201,"邀请码不能为空！");
        }

    }


    @Override
    public Result<Object> registerByPhoneAndPassword(UserDTO user) {

        //1.判断改手机号是否注册过账号
        if (ToolUtil.isNotEmpty(this.getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getMobile, user.getMobile())))) {

            return new ResultUtil<>().setErrorMsg(201, "该手机号已注册过==！");
        }

        //2.判断验证码是否失效(默认时间5分钟)
        if (ToolUtil.isEmpty(RedisUtil.getStr(CommonConstant.PRE_SMS + user.getMobile()))) {

            return new ResultUtil<>().setErrorMsg(202, "验证码已失效,请重新发送短信验证！");
        }

        //3.判断验证码是否一致
        if (!RedisUtil.getStr(CommonConstant.PRE_SMS + user.getMobile()).equals(user.getCode())) {

            return new ResultUtil<>().setErrorMsg(203, "验证码不一致！");
        }

        CompletableFuture.supplyAsync(() -> {

            //4.生成默认用户
            User userNew = new User();

            userNew.setUsername(user.getMobile())
                    .setNickName("梵夫莎用户" + CommonUtil.getRandomNum())
                    .setPassword(new BCryptPasswordEncoder().encode(user.getPassword()))
                    .setMobile(user.getMobile())
                    .setAvatar(CommonConstant.USER_DEFAULT_AVATAR)
                    .setInvitationCode(CommonUtil.getRandomNum());

            return userService.save(userNew);

        }).thenAcceptAsync(user1 -> CompletableFuture.allOf(

                //生成默认角色
                CompletableFuture.runAsync(() -> {
                    UserRole userRole = new UserRole();
                    userRole.setUserId(user1.getId());
                    userRole.setRoleId(roleService.getIdByRoleName(CommonConstant.DEFAULT_ROLE));
                    userRoleService.save(userRole);

                    //注册融云im服务通讯
                    iRongyunService.addUser(user1.getId(),user1.getNickName(),user1.getAvatar());

                    //建立用户与邀请人关系
                    if(ToolUtil.isNotEmpty(user.getInvitationCode())){
                        //取邀请人的id
                        String userid = Optional.ofNullable(this.getOne(Wrappers.<User>lambdaQuery().eq(User::getInvitationCode,user.getInvitationCode())))
                                .map(User::getId).orElse(null);

                        if(!StringUtils.isEmpty(userid)){

                            UserRelationship userRelationship = new UserRelationship();
                            userRelationship.setCreateBy(user1.getId());
                            userRelationship.setParentUserid(userid);
                            userRelationship.setCreateTime(new Date());

                            iUserRelationshipService.insertOrderUpdateUserRelationship(userRelationship);
                        }
                    }

                })


                // TODO: 2020/3/13 处理分销
        ));


        return new ResultUtil<>().setData(200,"注册成功！");


    }

    @Override
    public Result<Object> loginByPhoneAndPassword(UserDTO user) {

        User userGet = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getMobile, user.getMobile()));

        //1.判断手机号是否存在
        if (ToolUtil.isEmpty(userGet)) {

            return new ResultUtil<>().setErrorMsg(203, "该手机号不存在或者没有注册!");
        }

        //2.判断密码是否一致S
        if (!new BCryptPasswordEncoder().matches(user.getPassword(), userGet.getPassword())) {

            return new ResultUtil<>().setErrorMsg(202, "密码不一致！");
        }

        Map<Object,String> map = new ArrayMap<>();
        //3.可以登录
        String token = securityUtil.getToken(userGet.getUsername(), true);
        map.put("userToken",token);
        map.put("nickName",userGet.getNickName());
        map.put("avatar",userGet.getAvatar());
        map.put("userId",userGet.getId());
        map.put("imToken",userGet.getImToken());

        return new ResultUtil<>().setData(map, "登录成功！");
    }

    @Override
    public AppUserVO getAppUserInfo() {

        //先删除缓存
        RedisUtil.del("user::" + securityUtil.getCurrUser().getUsername());

        AppUserVO appUserVO = new AppUserVO();

        //个人基本信息
        ToolUtil.copyProperties(securityUtil.getCurrUser(), appUserVO);

        User user = this.getById(securityUtil.getCurrUser().getId());
        ToolUtil.copyProperties(user, appUserVO);
        //当前角色
        appUserVO.setRoleName(Optional.ofNullable(roleService.getRolesByRoleIds(userRoleService.getRoleIdsByUserId(securityUtil.getCurrUser()
                .getId()))).map(roles -> roles.get(0).getName()).orElse("暂无！"));

        //邀请码加地址
        appUserVO.setUrlAndInvitationCode("http://hqhh520.com:8085/?invitationCode="+user.getInvitationCode());

        // TODO: 2020/3/13 我的余额
        appUserVO.setMyBalance(user.getBalance());
        // TODO: 2020/3/13 我的收藏数量
        appUserVO.setMyCollectNum(iMyFavoriteService.getMyFavoriteNum());
        // TODO: 2020/3/13 我的足迹数量
        appUserVO.setMyFootprintNum(iMyFootprintService.getMyFootprintNum());
        // TODO: 2020/3/13 我的粉丝数量
        appUserVO.setMyFansNum(iFollowAttentionService.getMefansNum(null));
        // TODO: 2020/3/13 我的关注数量
        appUserVO.setMyAttentionNum(iFollowAttentionService.getfollowAttentionNum());


//        // TODO: 2020/3/13 我的余额
//        appUserVO.setMyBalance(user.getBalance());
//        // TODO: 2020/4/13 我的性别
//        appUserVO.setSex(user.getSex());
//        // TODO: 2020/4/13 我的婚期
//        appUserVO.setWeddingDay(user.getWeddingDay());
//        // TODO: 2020/4/13 个人简介
//        appUserVO.setIndividualResume(user.getIndividualResume());
        return appUserVO;

    }

    @Override
    public Result<Object> updateAppUserInfo(UserUpdateDTO userUpdateDTO){
        User byId = super.getById(userUpdateDTO.getId());
        return Optional.ofNullable(byId)
                .map(user -> {
                    ToolUtil.copyProperties(userUpdateDTO,byId);
                    byId.setShopName(userUpdateDTO.getShopName());
                    super.saveOrUpdate(byId);
//                    //3.可以登录
//                    String token = securityUtil.getToken(user.getUsername(), true);
                    return new ResultUtil<>().setSuccessMsg("修改成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "当前用户不存在！"));
    }


    @Override
    public Result<Object> addUser(User user, String roleIds) {

        if(ToolUtil.isEmpty(user.getUsername()) || ToolUtil.isEmpty(user.getPassword())){
            return new ResultUtil<>().setErrorMsg("缺少账号密码不能为空！");
        }


        if (ToolUtil.isNotEmpty(super.getOne(Wrappers.<User>lambdaQuery()
            .eq(User::getUsername, user.getUsername())))) {

            return new ResultUtil<>().setErrorMsg("账号名称重复！");
        }

        String encryptPass = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encryptPass);

      return Optional.ofNullable(userService.save(user)).map(userInsert -> {

          UserRole userRole = new UserRole();
          userRole.setUserId(user.getId());
//          userRole.setRoleId(roleService.getIdByRoleName(CommonConstant.ADMIN));
          userRole.setRoleId(roleIds);
          userRoleService.save(userRole);

          return new ResultUtil<>().setSuccessMsg("添加成功！");

        }).orElse(new ResultUtil<>().setErrorMsg(201, "添加失败！"));

    }

    @Override
    public List<BackUserVO> getBackUserAdminList(UserDTO userDTO) {

        return Optional.ofNullable(getUserList(UserTypeEnum.ADMIN))
                .map(users -> users.parallelStream().flatMap(user -> Stream.of(getBackUserVO(user.getId(),UserTypeEnum.ADMIN)))
                .collect(Collectors.toList()))
                .orElse(null);

    }

    @Override
    public List<BackUserVO> getBackUserStoreList(UserDTO userDTO) {

        return Optional.ofNullable(getUserList(UserTypeEnum.STORE))
                .map(users -> users.parallelStream().map(user -> (getBackUserVO(user.getId(),UserTypeEnum.STORE)))
                        .filter(pcGood-> pcGood.getNickName().contains(ToolUtil.isNotEmpty(userDTO.getUsername())?userDTO.getUsername():pcGood.getNickName()))
                        .filter(pcGood-> pcGood.getMobile().contains(ToolUtil.isNotEmpty(userDTO.getMobile())?userDTO.getMobile():pcGood.getMobile()))
                        .filter(pcGood-> pcGood.getAddress().contains(ToolUtil.isNotEmpty(userDTO.getAddress())?userDTO.getAddress():pcGood.getAddress()))
                        .filter(pcGood-> pcGood.getIsBest().equals(ToolUtil.isNotEmpty(userDTO.getIsBest())?userDTO.getIsBest():pcGood.getIsBest()))
                        .collect(Collectors.toList()))
                .orElse(null);
    }

    @Override
    public List<BackUserVO> getBackUserCommonList(UserDTO userDTO) {

        return Optional.ofNullable(getUserList(UserTypeEnum.DEFAULT_ROLE))
                .map(users -> users.parallelStream().flatMap(user -> Stream.of(getBackUserVO(user.getId(),UserTypeEnum.DEFAULT_ROLE)))
                        .collect(Collectors.toList()))
                .orElse(null);
    }

    @Override
    public List<BackUserVO> getBackUserPersonStoreList(UserDTO userDTO) {

        return Optional.ofNullable(getUserList(UserTypeEnum.PER_STORE))
                .map(users -> users.parallelStream().flatMap(user -> Stream.of(getBackUserVO(user.getId(),UserTypeEnum.PER_STORE)))
                        .collect(Collectors.toList()))
                .orElse(null);
    }

    @Override
    public List<BackUserInfo> getUserList(UserDTO userDTO) {

        if (ObjectUtil.equal("", userDTO.getUsername()) || ObjectUtil.equal(" ", userDTO.getUsername())) {

            userDTO.setUsername(null);
        }
        LambdaQueryWrapper<User> userQuery = Wrappers.<User>lambdaQuery()
                .orderByDesc(User::getCreateTime);


        if (ToolUtil.isNotEmpty(userDTO.getMobile())) {

            userQuery.eq(User::getMobile, userDTO.getMobile());
        }

        if (ToolUtil.isNotEmpty(userDTO.getUsername())) {

            userQuery.like(User::getUsername, userDTO.getUsername());
        }

        return Optional.ofNullable(ToolUtil.setListToNul(super.list(userQuery)))
                .map(users -> users.parallelStream().flatMap(user -> Stream.of(getBackUserInfo(user.getUsername())))
                .collect(Collectors.toList()))
                .orElse(null);
    }


    /**
     * 获取基本用户数据
     * @param userType
     * @return
     */
    public List<User> getUserList(UserTypeEnum userType) {

        if (ObjectUtil.equal(UserTypeEnum.DEFAULT_ROLE, userType)) {

            return userRoleService.getUserIdsByRoleId(roleService.getIdByRoleName(CommonConstant.DEFAULT_ROLE));
        }else if (ObjectUtil.equal(UserTypeEnum.STORE, userType)) {

            return userRoleService.getUserIdsByRoleId(roleService.getIdByRoleName(CommonConstant.STORE));
        }else if (ObjectUtil.equal(UserTypeEnum.ADMIN, userType)){

            return userRoleService.getUserIdsByRoleId(roleService.getIdByRoleName(CommonConstant.ADMIN));
        }else {

            return userRoleService.getUserIdsByRoleId(roleService.getIdByRoleName(CommonConstant.PER_STORE));
        }
    }

    /**
     * 封装数据
     * @param userId
     * @return
     */
    public BackUserVO getBackUserVO(String userId ,UserTypeEnum userTypeEnum) {

        BackUserVO backUserVO = new BackUserVO();

        Optional.ofNullable(this.getById(userId))
                .ifPresent(user -> {
                    ToolUtil.copyProperties(user, backUserVO);
                    if(userTypeEnum.equals(UserTypeEnum.STORE)||userTypeEnum.equals(UserTypeEnum.PER_STORE)){
                        backUserVO.setIsBest(iSelectStoreService.getSelectStore(userId));//是否严选
                        backUserVO.setClassName(iGoodCategoryService.getGoodCategoryName(user.getClassId()));//分类名称
                        backUserVO.setAreaName(iAreaService.getAddressName(user.getAreaId()));//区域名称
                        StoreAudit storeAudit = storeAuditMapper.selectOne(Wrappers.<StoreAudit>lambdaQuery()
                                .eq(StoreAudit::getCreateBy,userId));
                        backUserVO.setIdCardFront(storeAudit.getIdCardFront());//身份证正面
                        backUserVO.setIdCardBack(storeAudit.getIdCardBack());//身份证反面
                        backUserVO.setBusinessCard(storeAudit.getBusinessCard());//营业执照
                    }
                });

            return backUserVO;
    }


    @Override
    public Result<Object> resetPass(String userIds) {

        return Optional.ofNullable(ToolUtil.setListToNul(ToolUtil.splitterStr(userIds)))
                .map(ids -> {

                    ids.parallelStream().forEach(id -> {

                        String newPassword = new BCryptPasswordEncoder().encode("123456");

                        Optional.ofNullable(super.getById(id))
                                .ifPresent(user -> {

                                    user.setPassword(newPassword);
                                    super.updateById(user);
                                });
                    });

                    return new ResultUtil<>().setSuccessMsg("重置密码成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "参数为空！"));
    }

    @Override
    public Result<Object> editOwn(User u) {

        return Optional.ofNullable(super.getById(u.getId()))
                .map(user -> {

                    ToolUtil.copyProperties(u,user);
                    super.saveOrUpdate(user);
                    return new ResultUtil<>().setSuccessMsg("修改成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "当前用户不存在！"));
    }

    @Override
    public Result<Object> modifyPass(String password, String newPass) {

        BackUserInfo backUserInfo = securityUtil.getCurrUser();

        if ( !new BCryptPasswordEncoder().matches(password, backUserInfo.getPassword())) {

            return new ResultUtil<>().setErrorMsg(201, "密码不一致！");
        }

        return Optional.ofNullable(super.getById(backUserInfo.getId()))
                .map(user -> {

                    user.setPassword(new BCryptPasswordEncoder().encode(newPass));
                    super.updateById(user);

                    return new ResultUtil<>().setSuccessMsg("修改密码成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(202, "不存在当前用户！"));
    }

    @Override
    public Result<Object> freezeAccount(String userId) {

        return Optional.ofNullable(super.getById(userId))
                .map(user -> {
                    String result = "";
                    if(user.getStatus().equals(-1)){
                        user.setStatus(0);
                        result="解冻成功！";
                    }else if (user.getStatus().equals(0)){
                        user.setStatus(CommonConstant.USER_STATUS_LOCK);
                        result="冻结成功！";
                    }
                    super.updateById(user);

                    return new ResultUtil<>().setSuccessMsg(result);
                }).orElse(new ResultUtil<>().setErrorMsg(201, "不存在此用户"));
    }


    @Override
    public BackUserInfo getBackUserInfo(String username) {

        return Optional.ofNullable(super.getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUsername, username)))
                .map(user -> {

                    BackUserInfo backUserInfo = new BackUserInfo();
                    ToolUtil.copyProperties(user, backUserInfo);
                    //角色
                    backUserInfo.setRoles(roleService.getRolesByRoleIds(userRoleService.getRoleIdsByUserId(user.getId())))
                            .setPermissions(rolePermissionService.getPermissionByRoles(userRoleService.getRoleIdsByUserId(user.getId())));

                    backUserInfo.setType(iGoodService.getRoleUserList(user.getId()));


                    return backUserInfo;

                }).orElse(null);
    }

    @Override
    public Result<Object> forgetPassword(UserDTO user) {

        User old = this.getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getMobile, user.getMobile()));

        //1.判断用户是否存在
        if (ToolUtil.isEmpty(old)) {
            return new ResultUtil<>().setErrorMsg(201, "手机号错误或用户不存在");
        }
        //2.判断验证码是否失效(默认时间5分钟)
        if (ToolUtil.isEmpty(RedisUtil.getStr(CommonConstant.PRE_SMS + user.getMobile()))) {

            return new ResultUtil<>().setErrorMsg(202, "验证码已失效,请重新发送短信验证！");
        }
        //3.判断验证码是否一致
        if (!RedisUtil.getStr(CommonConstant.PRE_SMS + user.getMobile()).equals(user.getCode())) {

            return new ResultUtil<>().setErrorMsg(203, "验证码不一致！");
        }
        old.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        this.updateById(old);
        //删除缓存
        RedisUtil.del("user::" + old.getUsername());
        return new ResultUtil<>().setData(200,"修改成功！");
    }

    @Override
    public BigDecimal getAccountBalance(String userId) {
        String id = null;
        if(ToolUtil.isNotEmpty(userId)){
            id = userId;
        }else {
            id = securityUtil.getCurrUser().getId();
        }

        return  Optional.ofNullable(super.getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getId,id)))
                .map(User::getBalance)
                .orElse(null);
    }

    @Override
    public String getUserIdByUserName(String id) {
        return  Optional.ofNullable(super.getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getId,id)))
                .map(User::getShopName)
                .orElse(null);
    }

    @Override
    public String getUserIdByUserPic(String id) {
        return  Optional.ofNullable(super.getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getId,id)))
                .map(User::getAvatar)
                .orElse(null);
    }


    /**
     * 后端获取用户详情
     * @return
     */
    @Override
    public BackUserVO getBackUserParticulars(String userId,UserTypeEnum userTypeEnum) {

        if(ToolUtil.isEmpty(userId) && ToolUtil.isEmpty(userTypeEnum)){

            String roleName = iGoodService.getRoleUserList(securityUtil.getCurrUser().getId());

            if(roleName.equals(UserTypeEnum.STORE.getValue())){
                return this.getBackUserVO(securityUtil.getCurrUser().getId(), UserTypeEnum.STORE);
            }else if(roleName.equals(UserTypeEnum.PER_STORE.getValue())){
                return this.getBackUserVO(securityUtil.getCurrUser().getId(), UserTypeEnum.PER_STORE);
            }

           return null;
        }else {
            return this.getBackUserVO(userId, UserTypeEnum.STORE);
        }

    }

    @Override
    public List<UserProfitVO> getUserProfitList() {

        return Optional.ofNullable(ToolUtil.setListToNul(this.list()))
                .map(users -> users.parallelStream().filter(user -> ToolUtil.isNotEmpty(userRelationshipService
                .getUserRelationshipListByUserId(user.getId()))).flatMap(user -> {

                    UserProfitVO userProfitVO = new UserProfitVO();
                    ToolUtil.copyProperties(user, userProfitVO);
                    userProfitVO.setTotalProfitMoney(balanceService.getProfitByUserId(user.getId()));

                    return Stream.of(userProfitVO);
                }).sorted(Comparator.comparing(UserProfitVO::getTotalProfitMoney))
                        .collect(Collectors.toList()))

                .orElse(null);

    }

    @Override
    public Result<Object> setPayPassword(UserPayPasswordVo userPayPasswordVo) {

        // redis 存储 短信code
        String redisCode = RedisUtil.getStr(CommonConstant.PRE_SMS.concat(securityUtil.getCurrUser().getMobile()));

        if (ToolUtil.isNotEmpty(redisCode)) {

            if (ObjectUtil.equal(userPayPasswordVo.getCode(), redisCode)) {

                User user = this.getById(securityUtil.getCurrUser().getId());

                user.setPayPassword(new BCryptPasswordEncoder().encode(userPayPasswordVo.getPayPassword()));
                this.updateById(user);

                return new ResultUtil<>().setSuccessMsg("设置支付密码成功！");
            }else {

                return new ResultUtil<>().setErrorMsg(202, "验证码不一致");
            }
        }else {

            return new ResultUtil<>().setErrorMsg(201, "验证码失效！");
        }
    }

    @Override
    public Result<Object> updatePayPassword(UserPayPasswordVo userPayPasswordVo) {

        User user = this.getById(securityUtil.getCurrUser().getId());

        if (ToolUtil.isNotEmpty(user.getPayPassword())) {

            if (new BCryptPasswordEncoder().matches(userPayPasswordVo.getOldPayPassword(), user.getPayPassword())) {

                user.setPayPassword(new BCryptPasswordEncoder().encode(userPayPasswordVo.getPayPassword()));
                this.updateById(user);

                return new ResultUtil<>().setSuccessMsg("设置支付密码成功!");

            }else {

                return new ResultUtil<>().setErrorMsg(202, "密码不一致!");
            }

        }else {

            return new ResultUtil<>().setErrorMsg(201, "暂未设置支付密码！");
        }
    }

    @Override
    public List<StoreCustomVo> getStoreAccurateCustomer(String storeId) {

        String currentUserId = ToolUtil.isEmpty(storeId) ? securityUtil.getCurrUser().getId() : storeId;

        return Optional.ofNullable(orderService.getOrderListByStoreId(currentUserId))
                .map(orders -> {

                    Map<String, List<Order>> data = orders.parallelStream().collect(Collectors.groupingBy(Order::getCreateBy));

                    List<StoreCustomVo> storeCustomVos = Lists.newArrayList();
                    data.forEach((k , v) -> {

                        StoreCustomVo storeCustomVo = new StoreCustomVo();
                        Optional.ofNullable(this.getById(k)).ifPresent(user -> {

                            ToolUtil.copyProperties(user, storeCustomVo);
                            storeCustomVo.setServiceCount(v.size());

                            storeCustomVos.add(storeCustomVo);
                        });
                    });

                    return storeCustomVos;
                }).orElse(null);

    }

    /**
     * 判断潜在客户不在精准客户中
     * @param storeCustomVo
     * @return
     */
    public Boolean containCustom(StoreCustomVo storeCustomVo, String currentUserId) {

        return Optional.ofNullable(getStoreAccurateCustomer(currentUserId)).map(storeCustomVos ->
                !storeCustomVos.parallelStream().map(StoreCustomVo::getId).collect(Collectors.toList())
                .contains(storeCustomVo.getId())).orElse(true);
    }

    @Override
    public List<StoreCustomVo> getStoreProspectiveCustomer() {

        String currentUserId = securityUtil.getCurrUser().getId();

        return Optional.ofNullable(myFootprintService.getMyFootPrintByStoreId(currentUserId))
                .map(myFootprints -> {

                    Map<String, List<MyFootprint>> data = myFootprints.parallelStream().collect(Collectors.groupingBy(MyFootprint::getCreateBy));

                    List<StoreCustomVo> storeCustomVos = Lists.newArrayList();
                    data.forEach((k , v) -> {

                        StoreCustomVo storeCustomVo = new StoreCustomVo();
                        Optional.ofNullable(this.getById(k)).ifPresent(user -> {

                            ToolUtil.copyProperties(user, storeCustomVo);

                            storeCustomVos.add(storeCustomVo);
                        });
                    });

                    return storeCustomVos.parallelStream().filter(storeCustomVo -> containCustom(storeCustomVo, currentUserId))
                            .flatMap(storeCustomVo -> {

                                Optional.ofNullable(this.getById(storeCustomVo.getId()))
                                        .ifPresent(user -> ToolUtil.copyProperties(user, storeCustomVo));

                                return Stream.of(storeCustomVo);
                            }).collect(Collectors.toList());
                }).orElse(null);
    }



    /****************************************************生成二维码***************************************************/









}
