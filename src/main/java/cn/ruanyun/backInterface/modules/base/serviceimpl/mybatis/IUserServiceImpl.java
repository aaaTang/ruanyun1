package cn.ruanyun.backInterface.modules.base.serviceimpl.mybatis;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.CheckEnum;
import cn.ruanyun.backInterface.common.enums.UserTypeEnum;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.dto.StoreListDto;
import cn.ruanyun.backInterface.modules.base.dto.UserDTO;
import cn.ruanyun.backInterface.modules.base.dto.UserUpdateDTO;
import cn.ruanyun.backInterface.modules.base.dto.WechatLoginDto;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.pojo.UserRole;
import cn.ruanyun.backInterface.modules.base.service.UserService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IRolePermissionService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.base.vo.*;
import cn.ruanyun.backInterface.modules.business.area.pojo.Area;
import cn.ruanyun.backInterface.modules.business.area.service.IAreaService;
import cn.ruanyun.backInterface.modules.business.balance.service.IBalanceService;
import cn.ruanyun.backInterface.modules.business.comment.service.ICommentService;
import cn.ruanyun.backInterface.modules.business.firstRateService.mapper.FirstRateServiceMapper;
import cn.ruanyun.backInterface.modules.business.firstRateService.pojo.FirstRateService;
import cn.ruanyun.backInterface.modules.business.firstRateService.service.IFirstRateServiceService;
import cn.ruanyun.backInterface.modules.business.followAttention.service.IFollowAttentionService;
import cn.ruanyun.backInterface.modules.business.good.VO.AppGoodListVO;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import cn.ruanyun.backInterface.modules.business.good.serviceimpl.IGoodServiceImpl;
import cn.ruanyun.backInterface.modules.business.goodCategory.entity.GoodCategory;
import cn.ruanyun.backInterface.modules.business.goodCategory.mapper.GoodCategoryMapper;
import cn.ruanyun.backInterface.modules.business.goodCategory.service.IGoodCategoryService;
import cn.ruanyun.backInterface.modules.business.goodCategory.serviceimpl.IGoodCategoryServiceImpl;
import cn.ruanyun.backInterface.modules.business.grade.service.IGradeService;
import cn.ruanyun.backInterface.modules.business.myFavorite.service.IMyFavoriteService;
import cn.ruanyun.backInterface.modules.business.myFootprint.pojo.MyFootprint;
import cn.ruanyun.backInterface.modules.business.myFootprint.service.IMyFootprintService;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;
import cn.ruanyun.backInterface.modules.business.order.service.IOrderService;
import cn.ruanyun.backInterface.modules.business.selectStore.service.ISelectStoreService;
import cn.ruanyun.backInterface.modules.business.staffManagement.mapper.StaffManagementMapper;
import cn.ruanyun.backInterface.modules.business.staffManagement.pojo.StaffManagement;
import cn.ruanyun.backInterface.modules.business.storeAudit.mapper.StoreAuditMapper;
import cn.ruanyun.backInterface.modules.business.storeAudit.pojo.StoreAudit;
import cn.ruanyun.backInterface.modules.business.storeAudit.service.IStoreAuditService;
import cn.ruanyun.backInterface.modules.business.storeFirstRateService.mapper.StoreFirstRateServiceMapper;
import cn.ruanyun.backInterface.modules.business.storeFirstRateService.pojo.StoreFirstRateService;
import cn.ruanyun.backInterface.modules.business.storeFirstRateService.service.IstoreFirstRateServiceService;
import cn.ruanyun.backInterface.modules.business.userRelationship.pojo.UserRelationship;
import cn.ruanyun.backInterface.modules.business.userRelationship.service.IUserRelationshipService;
import cn.ruanyun.backInterface.modules.fadada.service.IfadadaService;
import cn.ruanyun.backInterface.modules.rongyun.service.IRongyunService;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.api.client.util.ArrayMap;
import com.google.api.client.util.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author fei
 */
@Service
@Slf4j
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserService userService;
    @Autowired
    private IUserRoleService userRoleService;
    @Resource
    private UserMapper userMapper;
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
    private IGoodCategoryServiceImpl iGoodCategoryServiceImpl;
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
    @Resource
    private StaffManagementMapper staffManagementMapper;
    @Autowired
    private IGoodCategoryService goodCategoryService;
    @Resource
    private GoodCategoryMapper goodCategoryMapper;
    @Autowired
    private IStoreAuditService storeAuditService;
    @Autowired
    private ICommentService commentService;
    @Autowired
    private IGoodService goodService;
    @Autowired
    private IstoreFirstRateServiceService storeFirstRateServiceService;
    @Autowired
    private IGradeService gradeService;

    @Autowired
    private IfadadaService fadadaService;

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

        /*//2.判断验证码是否失效(默认时间5分钟)
        if (ToolUtil.isEmpty(RedisUtil.getStr(CommonConstant.PRE_SMS + user.getMobile()))) {

            return new ResultUtil<>().setErrorMsg(202, "验证码已失效,请重新发送短信验证！");
        }

        //3.判断验证码是否一致
        if (!RedisUtil.getStr(CommonConstant.PRE_SMS + user.getMobile()).equals(user.getCode())) {

            return new ResultUtil<>().setErrorMsg(203, "验证码不一致！");
        }*/

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

        //2.判断密码是否一致
        if (!new BCryptPasswordEncoder().matches(user.getPassword(), userGet.getPassword())) {

            return new ResultUtil<>().setErrorMsg(202, "密码不一致！");
        }
        //3.判断是否被禁用状态
        if (ToolUtil.isNotEmpty(userGet)) {

            if(userGet.getStatus().equals(CommonConstant.STATUS_DISABLE)){

                return new ResultUtil<>().setErrorMsg(203, "该用户已被禁用！");
            }
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
        //商家名称
        appUserVO.setShopName(
                Optional.ofNullable(this.getById(Optional.ofNullable(staffManagementMapper.selectOne(new QueryWrapper<StaffManagement>().lambda()
                        .eq(StaffManagement::getStaffId,user.getId()))).map(StaffManagement::getCreateBy).orElse(null))).map(User::getShopName).orElse(null)
                );
        //是否有支付密码
        if(ToolUtil.isNotEmpty(user.getPayPassword())){
            appUserVO.setPay(1); }else {
            appUserVO.setPay(0);
        }

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
                        .filter(pcGood-> pcGood.getShopName().contains(ToolUtil.isNotEmpty(userDTO.getUsername())?userDTO.getUsername():pcGood.getShopName()))
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
                                .eq(StoreAudit::getCreateBy,userId).eq(StoreAudit::getCheckEnum,CheckEnum.CHECK_SUCCESS));
                        backUserVO.setIdCardFront(Optional.ofNullable(storeAudit).map(StoreAudit::getIdCardFront).orElse("暂无！"));//身份证正面
                        backUserVO.setIdCardBack(Optional.ofNullable(storeAudit).map(StoreAudit::getIdCardBack).orElse("暂无！"));//身份证反面
                        backUserVO.setBusinessCard(Optional.ofNullable(storeAudit).map(StoreAudit::getBusinessCard).orElse("暂无！"));//营业执照
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


                    //查询一级分类
                   Optional.ofNullable(goodCategoryMapper.selectById(user.getClassId())).ifPresent(goodCategory -> {

                       GoodCategory goodCategory1 = new GoodCategory();

                       goodCategory1 = goodCategoryMapper.selectById(goodCategory.getParentId());

                       if(ToolUtil.isNotEmpty(goodCategory1)){
                           backUserInfo.setServiceCategoryName(goodCategory1.getTitle());
                       }else {
                           backUserInfo.setServiceCategoryName(goodCategory.getTitle());
                       }

                   });

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
                }).sorted(Comparator.comparing(UserProfitVO::getTotalProfitMoney).reversed())
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
     * @param storeCustomVo storeCustomVo
     * @return storeCustomVo
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

    @Override
    public Result<Object> wechatLogin(WechatLoginDto wechatLoginDto) {

        if (ToolUtil.isEmpty(wechatLoginDto.getCode())) {
            return new ResultUtil<>().setErrorMsg(201, "暂未授权！");
        }


        log.info("1111" + wechatLoginDto);
        //1.通过code获取openId
        String str = "https://api.weixin.qq.com/sns/jscode2session?appid=" +
                CommonConstant.APP_ID +
                "&secret=" +
                CommonConstant.APP_SECRET +
                "&js_code=" +
                wechatLoginDto.getCode() +
                "&grant_type=authorization_code";
        String result = HttpUtil.get(str);

        //2. 获取accessToken

        String openid = JSONObject.parseObject(result).getString("openid");

        WechatVo wechatVo = new WechatVo();
        wechatVo.setOpenId(openid);
        //判断当前用户是否是新用户:如果是新用户, 则返回一个状态值,提示用户绑定手机号；如果不是新用户, 则直接返回token
        return Optional.ofNullable(this.getOne(Wrappers.<User>lambdaQuery().eq(User::getOpenId, openid)))
                .map(user -> new ResultUtil<>().setData(securityUtil.getToken(user.getUsername(), true), "登录成功！"))
                .orElse(new ResultUtil<>().setData(wechatVo, "需要绑定手机号", 201));
    }

    @Override
    public Result<Object> bindMobile(WechatLoginDto wechatLoginDto) {


        log.info("22222" + wechatLoginDto);

        if (ToolUtil.isEmpty(wechatLoginDto.getMessageCode())) {

            return new ResultUtil<>().setErrorMsg(201, "请输入验证码");

        }else if (ToolUtil.isEmpty(RedisUtil.getStr(CommonConstant.PRE_SMS + wechatLoginDto.getMobile()))) {

            return new ResultUtil<>().setErrorMsg(202, "验证码已失效,请重新发送短信验证！");

        }else if (!RedisUtil.getStr(CommonConstant.PRE_SMS + wechatLoginDto.getMobile()).equals(wechatLoginDto.getMessageCode())) {

            return new ResultUtil<>().setErrorMsg(203, "验证码不一致！");

        }else {

            //1. 判断该手机号在数据库中是否存在

            return Optional.ofNullable(this.getOne(Wrappers.<User>lambdaQuery().eq(User::getMobile, wechatLoginDto.getMobile())))
                    .map(user -> {

                        user.setOpenId(wechatLoginDto.getOpenId());
                        this.updateById(user);

                        String jwt = securityUtil.getToken(user.getUsername(), true);

                        log.info("当前的jwt是" + jwt);

                        return new ResultUtil<>().setData(jwt, "登录成功！");
                    }).orElseGet(() -> {

                        User user = new User();

                        user.setNickName(wechatLoginDto.getNickName())
                                .setAvatar(wechatLoginDto.getHeadimgUrl())
                                .setSex(wechatLoginDto.getGender())
                                .setUsername(wechatLoginDto.getOpenId())
                                .setOpenId(wechatLoginDto.getOpenId())
                                .setMobile(wechatLoginDto.getMobile());


                        if (this.save(user)) {

                            //建立用户与邀请人关系
                            if(ToolUtil.isNotEmpty(wechatLoginDto.getInvitationCode())){
                                //取邀请人的id
                                String inviteUserId = Optional.ofNullable(this.getOne(Wrappers.<User>lambdaQuery().eq(User::getInvitationCode,wechatLoginDto.getInvitationCode())))
                                        .map(User::getId).orElse(null);

                                if(ToolUtil.isNotEmpty(inviteUserId)){

                                    UserRelationship userRelationship = new UserRelationship();
                                    userRelationship.setCreateBy(user.getId());
                                    userRelationship.setParentUserid(inviteUserId);
                                    iUserRelationshipService.insertOrderUpdateUserRelationship(userRelationship);
                                }else {

                                    return new ResultUtil<>().setErrorMsg(208, "当前邀请码无效！");
                                }
                            }

                            //注册融云im服务通讯
                            iRongyunService.addUser(user.getId(),user.getNickName(),user.getAvatar());

                            return new ResultUtil<>().setData(securityUtil.getToken(user.getUsername(), true), "登录成功！");
                        }else {

                            return new ResultUtil<>().setErrorMsg(207, "内部错误！！！！");
                        }
                    });
        }
    }

    @Override
    public UserBalanceVo getUserBalance() {

        User user = this.getById(securityUtil.getCurrUser().getId());
        UserBalanceVo userBalanceVo = new UserBalanceVo();
        userBalanceVo.setFreezeBalance(balanceService.getOrderFreezeMoney(user.getId()))
                .setNormalBalance(user.getBalance().subtract(userBalanceVo.getFreezeBalance()));
        return userBalanceVo;
    }

    @Override
    public Integer getUserCurrentScore(String userId) {

        AtomicReference<Integer> score = new AtomicReference<>(0);

        Optional.ofNullable(this.getById(userId)).ifPresent(user -> {

            //用户上传展示视频的分数
            Optional.ofNullable(user.getPic()).ifPresent(pics -> score.updateAndGet(v -> v + 3));

            //用户上传门店地址，定位，店铺电话，店铺营业时间的分数
            Optional.ofNullable(user.getAddress()).flatMap(address -> Optional.ofNullable(user.getLatitude()))
                    .flatMap(latitude -> Optional.ofNullable(user.getMobile()))
                    .flatMap(mobile -> Optional.ofNullable(user.getBusinessHours()))
                    .ifPresent(businessHours -> score.updateAndGet(v -> v + 1));
        });

        return score.get();
    }

    @Override
    public Integer judgeStoreLevel(String userId) {

       return Optional.ofNullable(this.getById(userId)).map(user -> {


            if (user.getScore() + getUserCurrentScore(userId) < 30) {

                return 1;
            }else if (30 <= user.getScore() + getUserCurrentScore(userId) && user.getScore() + getUserCurrentScore(userId) < 60) {

                return 2;
            }else if (60 <= user.getScore() + getUserCurrentScore(userId) && user.getScore() + getUserCurrentScore(userId) < 70) {

                return 3;
            }else if (70 <= user.getScore() + getUserCurrentScore(userId) && user.getScore() + getUserCurrentScore(userId) < 85) {

                return 4;
            }else if (85 <= user.getScore() + getUserCurrentScore(userId) && user.getScore() + getUserCurrentScore(userId) < 95) {

                return 5;
            }else if (95 <= user.getScore() + getUserCurrentScore(userId) && user.getScore() + getUserCurrentScore(userId) < 100) {

                return 6;
            }

            return 0;

        }).orElse(0);
    }

    @Override
    public Result<DataVo<StoreListVo>> getStoreList(PageVo pageVo,StoreListDto storeListDto) {

        return Optional.ofNullable(storeAuditService.getStoreIdByCheckPass(storeListDto))
                .map(users -> {
                    
                    //封装门店数据
                    List<StoreListVo> storeListVos = users.parallelStream().flatMap(user -> {

                        StoreListVo storeListVo = new StoreListVo();

                        User user1 = userMapper.selectById(user.getId());

                        //等级
                        storeListVo.setStoreLevel(judgeStoreLevel(user.getId()))

                                //门店星级
                                .setStoreStarLevel(Double.parseDouble(gradeService.getShopScore(user.getId())))

                                //优质服务
                                .setFirstRateService(
                                        storeFirstRateServiceService.getStoreFirstRateServiceName(user.getId(),CheckEnum.CHECK_SUCCESS)
                                )
                                //信任标识
                                .setTrustIdentity(user1.getTrustIdentity())

                                //连锁认证
                                .setAuthenticationTypeEnum(user1.getAuthenticationTypeEnum())

                                //评价条数
                                .setCommentNum(commentService.getCommentByStore(user.getId()))

                                //最低价格
                                .setLowPrice(goodService.getLowPriceByStoreId(user.getId()))

                                //距离
                                .setDistance(LocationUtils.getDistance( storeListDto.getLongitude(),storeListDto.getLatitude(), user.getLongitude(),user.getLatitude()))

                                //商家类型 （1，酒店 2.主持人 3.默认）
                                .setStoreType(iGoodCategoryServiceImpl.judgeStoreType(user))
                        ;


                        ToolUtil.copyProperties(user, storeListVo);

                        return Stream.of(storeListVo);
                    })/*.sorted(Comparator.comparing(StoreListVo::getDistance).thenComparing(Comparator.comparing(StoreListVo::getStoreLevel)
                    .thenComparing(StoreListVo::getStoreStarLevel)))*/
                        //门店等级 判断门店等级 1.没有等级 2.普通 3.铜牌 4.银牌 5.金牌 6.钻石
                       .filter(storeListVo -> storeListVo.getStoreLevel().equals(ToolUtil.isNotEmpty(storeListDto.getStoreLevel())?storeListDto.getStoreLevel():storeListVo.getStoreLevel()))
                    .collect(Collectors.toList());

                    if(ToolUtil.isNotEmpty(storeListDto.getFilterCondition())){

                        //销量升序 1
                        if(CommonConstant.SALES_VOLUME_ASC.equals(storeListDto.getFilterCondition())){
                            storeListVos = storeListVos.parallelStream().sorted(Comparator.comparing(StoreListVo::getCommentNum)).collect(Collectors.toList());
                            //销量降序2
                        }else if(CommonConstant.SALES_VOLUME_DESC.equals(storeListDto.getFilterCondition())){
                            storeListVos = storeListVos.parallelStream().sorted(Comparator.comparing(StoreListVo::getCommentNum).reversed()).collect(Collectors.toList());
                            //价格升序3
                        }else if(CommonConstant.PRICE_ASC.equals(storeListDto.getFilterCondition())){
                            storeListVos = storeListVos.parallelStream().sorted(Comparator.comparing(StoreListVo::getLowPrice)).collect(Collectors.toList());
                            //价格价格4
                        }else if(CommonConstant.PRICE_DESC.equals(storeListDto.getFilterCondition())){
                            storeListVos = storeListVos.parallelStream().sorted(Comparator.comparing(StoreListVo::getLowPrice).reversed()).collect(Collectors.toList());
                            //评论数升序5
                        }else if(CommonConstant.COMMENTS_NUM_ASC.equals(storeListDto.getFilterCondition())){
                            storeListVos = storeListVos.parallelStream().sorted(Comparator.comparing(StoreListVo::getCommentNum)).collect(Collectors.toList());
                            //评论数降序6
                        }else if(CommonConstant.COMMENTS_NUM_DESC.equals(storeListDto.getFilterCondition())){
                            storeListVos = storeListVos.parallelStream().sorted(Comparator.comparing(StoreListVo::getCommentNum).reversed()).collect(Collectors.toList());
                            //门店等级升序7
                        }else if(CommonConstant.STORE_LEVEL_ASC.equals(storeListDto.getFilterCondition())){
                            storeListVos = storeListVos.parallelStream().sorted(Comparator.comparing(StoreListVo::getStoreLevel)).collect(Collectors.toList());
                            // 门店等级降序8
                        }else if(CommonConstant.STORE_LEVEL_DESC.equals(storeListDto.getFilterCondition())){
                            storeListVos = storeListVos.parallelStream().sorted(Comparator.comparing(StoreListVo::getStoreLevel).reversed()).collect(Collectors.toList());
                            //门店星级升序9
                        }else if(CommonConstant.STORE_STAT_LEVEL_ASC.equals(storeListDto.getFilterCondition())){
                            storeListVos = storeListVos.parallelStream().sorted(Comparator.comparing(StoreListVo::getStoreStarLevel)).collect(Collectors.toList());
                            //门店星级降序10
                        }else if(CommonConstant.STORE_STAT_LEVEL_DESC.equals(storeListDto.getFilterCondition())){
                            storeListVos = storeListVos.parallelStream().sorted(Comparator.comparing(StoreListVo::getStoreStarLevel).reversed()).collect(Collectors.toList());
                            //距离升序 11
                        }else if(CommonConstant.DISTANCE_ASC.equals(storeListDto.getFilterCondition())){
                            storeListVos = storeListVos.parallelStream().sorted(Comparator.comparing(StoreListVo::getDistance)).collect(Collectors.toList());
                            //距离降序12
                        }else if(CommonConstant.DISTANCE_DESC.equals(storeListDto.getFilterCondition())){
                            storeListVos = storeListVos.parallelStream().sorted(Comparator.comparing(StoreListVo::getDistance).reversed()).collect(Collectors.toList());
                        }else if (ToolUtil.isNotEmpty(storeListDto.getAreaName())) {

                            Area area = iAreaService.getOne(Wrappers.<Area>lambdaQuery()
                            .like(Area::getTitle, storeListDto.getAreaName()));

                            if (ToolUtil.isNotEmpty(area)) {

                                storeListVos = storeListVos.parallelStream().filter(storeListVo -> storeListVo.getAreaId().equals(area.getId()))
                                        .collect(Collectors.toList());
                            }
                        }
                    }

                    DataVo<StoreListVo> result = new DataVo<>();

                    ToolUtil.copyProperties(storeListDto, result);
                    result.setTotalNumber(storeListVos.size())
                            .setDataResult(PageUtil.listToPage(pageVo, storeListVos));

                   if(ToolUtil.isNotEmpty(storeListVos)){
                       return new ResultUtil<DataVo<StoreListVo>>().setData(result, "获取门店列表数据成功！");
                   } else {
                       return new ResultUtil<DataVo<StoreListVo>>().setErrorMsg(201, "暂无数据！");
                   }

                })
                .orElse(new ResultUtil<DataVo<StoreListVo>>().setErrorMsg(201, "暂无数据！"));
    }
}
