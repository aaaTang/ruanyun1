package cn.ruanyun.backInterface.modules.base.controller.manage;


import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.dto.UserDTO;
import cn.ruanyun.backInterface.modules.base.dto.UserUpdateDTO;
import cn.ruanyun.backInterface.modules.base.dto.WechatLoginDto;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.base.vo.UserPayPasswordVo;
import cn.ruanyun.backInterface.modules.base.vo.UserProfitVO;
import jdk.nashorn.internal.ir.Optimistic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Optional;


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
     * APP通过邀请码获取用户信息
     * @param user
     * @return
     */
    @PostMapping("/appGetinvitationCode")
    public Result<Object> appGetinvitationCode(UserDTO user) {

        return userService.appGetinvitationCode(user);
    }


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
    public Result<Object> getAccountBalance(String userId){

        return new ResultUtil<>().setData(userService.getAccountBalance(userId), "获取账户余额成功！");

    }


    /**
     * 设置-忘记支付密码
     * @param userPayPasswordVo
     * @return
     */
    @PostMapping("/setPayPassword")
    public Result<Object> setPayPassword(UserPayPasswordVo userPayPasswordVo) {

        return userService.setPayPassword(userPayPasswordVo);
    }


    /**
     * 更新支付密码
     * @param userPayPasswordVo
     * @return
     */
    @PostMapping("/updatePayPassword")
    public Result<Object> updatePayPassword(UserPayPasswordVo userPayPasswordVo) {

        return userService.updatePayPassword(userPayPasswordVo);
    }


    /**
     * 获取龙虎排行榜
     * @param pageVo
     * @return
     */
    @PostMapping("/getUserProfitList")
    public Result<Object> getUserProfitList(PageVo pageVo) {

        return Optional.ofNullable(userService.getUserProfitList())
                .map(userProfitVos -> {

                    DataVo<UserProfitVO> result = new DataVo<>();
                    result.setTotalNumber(userProfitVos.size())
                            .setDataResult(PageUtil.listToPage(pageVo, userProfitVos));

                    return new ResultUtil<>().setData(result, "获取龙湖排行榜成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));

    }


    /**
     * 获取精准客户
     * @param pageVo 分页参数
     * @return Object
     */
    @PostMapping("/getStoreAccurateCustomer")
    public Result<Object> getStoreAccurateCustomer(PageVo pageVo) {

        return Optional.ofNullable(userService.getStoreAccurateCustomer(null))
                .map(storeCustomVos -> {

                    DataVo<UserProfitVO> result = new DataVo<>();
                    result.setTotalNumber(storeCustomVos.size())
                            .setDataResult(PageUtil.listToPage(pageVo, storeCustomVos));

                    return new ResultUtil<>().setData(result, "获取精准客户数据成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));

    }

    /**
     * 获取潜在客户
     * @param pageVo 分页参数
     * @return Object
     */
    @PostMapping("/getStoreProspectiveCustomer")
    public Result<Object> getStoreProspectiveCustomer(PageVo pageVo) {

        return Optional.ofNullable(userService.getStoreProspectiveCustomer())
                .map(storeCustomVos -> {

                    DataVo<UserProfitVO> result = new DataVo<>();
                    result.setTotalNumber(storeCustomVos.size())
                            .setDataResult(PageUtil.listToPage(pageVo, storeCustomVos));

                    return new ResultUtil<>().setData(result, "获取潜在客户数据成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));

    }


    /**
     * 微信授权登录
     * @param wechatLoginDto 参数
     * @return Object
     */
    @PostMapping("/wechatLogin")
    public Result<Object> wechatLogin(WechatLoginDto wechatLoginDto) {

        return userService.wechatLogin(wechatLoginDto);
    }

    /**
     * 绑定手机号
     * @param wechatLoginDto 参数
     * @return Object
     */
    @PostMapping("/bindMobile")
    public Result<Object> bindMobile(WechatLoginDto wechatLoginDto) {

        return userService.bindMobile(wechatLoginDto);
    }

}
