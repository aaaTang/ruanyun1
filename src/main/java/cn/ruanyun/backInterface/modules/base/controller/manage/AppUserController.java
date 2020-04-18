package cn.ruanyun.backInterface.modules.base.controller.manage;


import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.dto.UserDTO;
import cn.ruanyun.backInterface.modules.base.dto.UserUpdateDTO;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;


/**
 * app端的用户部分
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/appUser")
@Transactional
public class AppUserController {


    @Autowired
    private IUserService userService;


    /**
     * 通过手机号注册
     * @param user
     * @return
     */
    @PostMapping("/registerByPhoneAndPassword")
    public Result<Object> registerByPhoneAndPassword(UserDTO user) {

        return userService.registerByPhoneAndPassword(user);
    }


    /**
     * 通过手机号和密码登录
     * @param user
     * @return
     */
    @PostMapping("/loginByPhoneAndPassword")
    public Result<Object> loginByPhoneAndPassword(UserDTO user) {

        return userService.loginByPhoneAndPassword(user);
    }


    /**
     * 获取个人信息
     * @return
     */
    @GetMapping("/getAppUserInfo")
    public Result<Object> getAppUserInfo() {

        return new ResultUtil<>().setData(userService.getAppUserInfo(), "获取个人信息成功！");

    }

    /**
     * 更新个人信息
     * @return
     */
    @PostMapping("/updateAppUserInfo")
    public Result<Object> updateAppUserInfo(UserUpdateDTO userUpdateDTO){

        return userService.updateAppUserInfo(userUpdateDTO);
    }

    /**
     * 忘记密码
     */
    @PostMapping("/forgetPassword")
    public Result<Object> forgetPassword(UserDTO user){

        return userService.forgetPassword(user);
    }

    /**
     * 获取账户余额
     */
    @PostMapping("/getAccountBalance")
    public BigDecimal getAccountBalance(){

        return userService.getAccountBalance();
    }

}
