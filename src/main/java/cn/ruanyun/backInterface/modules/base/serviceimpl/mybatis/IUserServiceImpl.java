package cn.ruanyun.backInterface.modules.base.serviceimpl.mybatis;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.dto.UserDTO;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.pojo.UserRole;
import cn.ruanyun.backInterface.modules.base.service.PermissionService;
import cn.ruanyun.backInterface.modules.base.service.UserRoleService;
import cn.ruanyun.backInterface.modules.base.service.UserService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.*;
import cn.ruanyun.backInterface.modules.base.vo.AppUserVO;
import cn.ruanyun.backInterface.modules.base.vo.BackUserInfo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
    private IPermissionService permissionService;

    @Autowired
    private IRolePermissionService rolePermissionService;


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

            return new ResultUtil<>().setErrorMsg(201, "改手机号已注册过==！");
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

            userNew.setUsername("梵夫莎用户" + CommonUtil.getRandomNum())
                    .setPassword(new BCryptPasswordEncoder().encode(user.getPassword()))
                    .setMobile(user.getMobile())
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


        return new ResultUtil<>().setSuccessMsg("注册成功！");


    }

    @Override
    public Result<Object> loginByPhoneAndPassword(UserDTO user) {

        User userGet = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getMobile, user.getMobile()));

        //1.判断手机号是否存在
        if (ToolUtil.isEmpty(userGet)) {

            return new ResultUtil<>().setErrorMsg(201, "该手机号不存在或者没有注册!");
        }

        //2.判断密码是否一致
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

        //当前角色
        appUserVO.setRoleName(roleService.getRolesByRoleIds(userRoleService.getRoleIdsByUserId(securityUtil.getCurrUser()
                .getId())).get(0).getName());

        // TODO: 2020/3/13 我的收藏数量
        // TODO: 2020/3/13 我的足迹数量
        // TODO: 2020/3/13 我的粉丝数量
        // TODO: 2020/3/13 我的关注数量
        // TODO: 2020/3/13 我的额度

        return appUserVO;

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

            //添加默认角色
            Optional.ofNullable(ToolUtil.setListToNul(ToolUtil.splitterStr(roleIds)))
                    .ifPresent(ids -> ids.parallelStream().forEach(id -> {

                        UserRole userRole = new UserRole();
                        userRole.setUserId(user.getId());
                        userRole.setRoleId(id);
                        userRoleService.save(userRole);
                    }));

            return new ResultUtil<>().setSuccessMsg("添加成功！");

        }).orElse(new ResultUtil<>().setErrorMsg(201, "添加失败！"));

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
}
