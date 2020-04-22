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
import cn.ruanyun.backInterface.modules.base.vo.AppUserVO;
import cn.ruanyun.backInterface.modules.base.vo.BackStrictVO;
import cn.ruanyun.backInterface.modules.base.vo.BackUserInfo;
import cn.ruanyun.backInterface.modules.base.vo.BackUserVO;
import cn.ruanyun.backInterface.modules.business.followAttention.service.IFollowAttentionService;
import cn.ruanyun.backInterface.modules.business.myFavorite.service.IMyFavoriteService;
import cn.ruanyun.backInterface.modules.business.myFootprint.service.IMyFootprintService;
import com.aliyuncs.ram.model.v20150501.ListVirtualMFADevicesResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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


    @Override
    public String getUserIdByName(String userName) {

        return Optional.ofNullable(super.getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUsername, userName)))
                .map(User::getId)
                .orElse(null);
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

        //3.可以登录
        String token = securityUtil.getToken(userGet.getUsername(), true);

        return new ResultUtil<>().setData(token, "登录成功！");
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
            return new ResultUtil<>().setErrorMsg("缺少必需表单字段");
        }


        if (ToolUtil.isNotEmpty(super.getOne(Wrappers.<User>lambdaQuery()
            .eq(User::getUsername, user.getUsername())))) {

            return new ResultUtil<>().setErrorMsg("名字重复！");
        }

        String encryptPass = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encryptPass);

      return Optional.ofNullable(userService.save(user)).map(userInsert -> {

          UserRole userRole = new UserRole();
          userRole.setUserId(user.getId());
          userRole.setRoleId(roleService.getIdByRoleName(CommonConstant.ADMIN));
          userRoleService.save(userRole);

          return new ResultUtil<>().setSuccessMsg("添加成功！");

        }).orElse(new ResultUtil<>().setErrorMsg(201, "添加失败！"));

    }

    @Override
    public List<BackUserVO> getBackUserAdminList(UserDTO userDTO) {

        return Optional.ofNullable(getUserList(UserTypeEnum.ADMIN))
                .map(users -> users.parallelStream().flatMap(user -> Stream.of(getBackUserVO(user.getId())))
                .collect(Collectors.toList()))
                .orElse(null);

    }

    @Override
    public List<BackStrictVO> getBackUserStoreList(UserDTO userDTO) {

        return null;
    }

    @Override
    public List<BackUserVO> getBackUserCommonList(UserDTO userDTO) {

        return Optional.ofNullable(getUserList(UserTypeEnum.DEFAULT_ROLE))
                .map(users -> users.parallelStream().flatMap(user -> Stream.of(getBackUserVO(user.getId())))
                        .collect(Collectors.toList()))
                .orElse(null);
    }

    @Override
    public List<BackUserVO> getBackUserPersonStoreList(UserDTO userDTO) {

        return Optional.ofNullable(getUserList(UserTypeEnum.PER_STORE))
                .map(users -> users.parallelStream().flatMap(user -> Stream.of(getBackUserVO(user.getId())))
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
    public BackUserVO getBackUserVO(String userId) {

        BackUserVO backUserVO = new BackUserVO();

        Optional.ofNullable(super.getById(userId))
                .ifPresent(user -> ToolUtil.copyProperties(user, backUserVO));

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
    public BigDecimal getAccountBalance() {

        return  Optional.ofNullable(super.getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getId,securityUtil.getCurrUser().getId())))
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


}
