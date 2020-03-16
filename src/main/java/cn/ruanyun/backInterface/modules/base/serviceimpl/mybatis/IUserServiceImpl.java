package cn.ruanyun.backInterface.modules.base.serviceimpl.mybatis;


import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.dto.UserDTO;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.pojo.UserRole;
import cn.ruanyun.backInterface.modules.base.service.UserRoleService;
import cn.ruanyun.backInterface.modules.base.service.UserService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.base.vo.AppUserVO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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

    public Result<Object> updateAppUserInfo(User user){

        user.setId(securityUtil.getCurrUser().getId());
        user.setUpdateBy(securityUtil.getCurrUser().getId());

        Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(user)))
                .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                .toFuture().join();
        return new ResultUtil<>().setSuccessMsg("更新个人信息成功！");
    }
}
