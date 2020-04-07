package cn.ruanyun.backInterface.modules.base.controller.manage;


import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.dto.UserDTO;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import com.aliyun.oss.ClientException;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


/**
 * 后台管理用户接口
 * @author fei
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/user")
@Transactional
public class BackUserController {

    private SecurityUtil securityUtil;

    private IUserService iUserService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SmsUtil smsUtil;

    @Autowired
    public BackUserController(SecurityUtil securityUtil, IUserService iUserService) {
        this.securityUtil = securityUtil;
        this.iUserService = iUserService;
    }

    /**
     * 获取用户个人信息详情
     * @return
     */
    @GetMapping("/info")
    public Result<Object> getUserInfo(){

        return new ResultUtil<>().setData(securityUtil.getCurrUser(), "获取个人信息成功！");
    }




    /**
     * 重置密码
     * @param userIds
     * @return
     */
    @PostMapping("/resetPass")
    public Result<Object> resetPass(String userIds){

        return iUserService.resetPass(userIds);
    }


    /**
     * 修改用户资料
     * @param u
     * @return
     */
    @PostMapping("/editOwn")
    public Result<Object> editOwn(@ModelAttribute User u){

        return iUserService.editOwn(u);
    }


    /**
     * 修改密码
     * @param password
     * @param newPass
     * @return
     */
    @PostMapping("/modifyPass")
    public Result<Object> modifyPass( String password, String newPass){

        return iUserService.modifyPass(password, newPass);
    }


    /**
     * 后台管理添加用户
     * @param user
     * @param roleIds
     * @return
     */
    @PostMapping(value = "/addUser")
    public Result<Object> addUser(User user, String roleIds){

        return iUserService.addUser(user, roleIds);
    }

    /**
     * 获取后台管理员列表
     * @param userDTO
     * @param pageVo
     * @return
     */
    @PostMapping("/getBackUserAdminList")
    public Result<Object> getBackUserAdminList(UserDTO userDTO, PageVo pageVo) {

        return Optional.ofNullable(iUserService.getBackUserAdminList(userDTO))
                .map(backUserVOS -> {

                    Map<String, Object> result = Maps.newHashMap();
                    result.put("size", backUserVOS.size());
                    result.put("data", PageUtil.listToPage(pageVo, backUserVOS));
                    return new ResultUtil<>().setData(result, "获取管理员用户成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }


    /**
     * 获取后台商家列表
     * @param userDTO
     * @param pageVo
     * @return
     */
    @PostMapping("/getBackUserStoreList")
    public Result<Object> getBackUserStoreList(UserDTO userDTO, PageVo pageVo) {

        return Optional.ofNullable(iUserService.getBackUserStoreList(userDTO))
                .map(backUserVOS -> {

                    Map<String, Object> result = Maps.newHashMap();
                    result.put("size", backUserVOS.size());
                    result.put("data", PageUtil.listToPage(pageVo, backUserVOS));
                    return new ResultUtil<>().setData(result, "获取商家列表成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }

    /**
     * 获取普通用户列表
     * @param userDTO
     * @param pageVo
     * @return
     */
    @PostMapping("/getBackUserCommonList")
    public Result<Object> getBackUserCommonList(UserDTO userDTO, PageVo pageVo) {

        return Optional.ofNullable(iUserService.getBackUserCommonList(userDTO))
                .map(backUserVOS -> {

                    Map<String, Object> result = Maps.newHashMap();
                    result.put("size", backUserVOS.size());
                    result.put("data", PageUtil.listToPage(pageVo, backUserVOS));
                    return new ResultUtil<>().setData(result, "获取普通消费者列表成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }

    /**
     * 获取后台个人商家列表
     * @param userDTO
     * @param pageVo
     * @return
     */
    @PostMapping("/getBackUserPersonStoreList")
    public Result<Object> getBackUserPersonStoreList(UserDTO userDTO, PageVo pageVo) {

        return Optional.ofNullable(iUserService.getBackUserPersonStoreList(userDTO))
                .map(backUserVOS -> {

                    Map<String, Object> result = Maps.newHashMap();
                    result.put("size", backUserVOS.size());
                    result.put("data", PageUtil.listToPage(pageVo, backUserVOS));
                    return new ResultUtil<>().setData(result, "获取个人商家列表成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }


    /**
     * 获取用户列表
     * @param userDTO
     * @param pageVo
     * @return
     */
    @PostMapping("/getUserList")
    public Result<Object>  getUserList(UserDTO userDTO, PageVo pageVo) {

        return Optional.ofNullable(iUserService.getUserList(userDTO))
                .map(backUserVOS -> {

                    Map<String, Object> result = Maps.newHashMap();
                    result.put("size", backUserVOS.size());
                    result.put("data", PageUtil.listToPage(pageVo, backUserVOS));
                    return new ResultUtil<>().setData(result, "获取用户管理列表成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }


    /**
     * 冻结账号
     * @param userId
     * @return
     */
    @PostMapping("/freezeAccount")
    public Result<Object> freezeAccount(String userId) {

        return iUserService.freezeAccount(userId);
    }

    /**
     * 发送短信验证码
     */
    @PostMapping("/sendMessage")
    public Result<Object> sendMessage(String mobile) throws ClientException {

        // 生成6位数验证码
        String code = CommonUtil.getRandomNum();
        // 缓存验证码
        redisTemplate.opsForValue().set(CommonConstant.PRE_SMS + mobile, code, 15L, TimeUnit.MINUTES);
        // 发送验证码
        try {
            SendSmsResponse response = smsUtil.sendCode(mobile, code, "SMS_176930019");
            if (response.getCode() != null && ("OK").equals(response.getMessage())) {
                return ResultUtil.success("发送短信验证码成功");
            } else {
                return ResultUtil.error("请求发送验证码失败，" + response.getMessage());
            }
        } catch (ClientException e) {
            log.error("请求发送短信验证码失败，" + e);
            return ResultUtil.error("请求发送验证码失败，" + e.getMessage());
        } catch (com.aliyuncs.exceptions.ClientException e) {
            e.printStackTrace();
            return ResultUtil.error("请求发送验证码失败，" + e.getMessage());
        }
    }
}
