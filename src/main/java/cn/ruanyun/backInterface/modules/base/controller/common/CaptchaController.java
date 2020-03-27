package cn.ruanyun.backInterface.modules.base.controller.common;


import cn.hutool.core.util.StrUtil;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.service.SettingService;
import cn.ruanyun.backInterface.modules.base.service.UserService;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author fei
 */
@RequestMapping("/ruanyun/common/captcha")
@RestController
@Transactional
@Slf4j
public class CaptchaController {

    @Autowired
    private SmsUtil smsUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private IpInfoUtil ipInfoUtil;



    @PostMapping("/sendRegistSmsCode")
    @ApiOperation(value = "发送注册短信验证码")
    public Result<Object> sendRegistSmsCode(String mobile, HttpServletRequest request) {

        return sendSms(mobile, request);
    }


    /**
     *
     * @param mobile 手机号
     */
    public Result<Object> sendSms(String mobile, HttpServletRequest request){

        // IP限流 1分钟限1个请求
        String key = "sendSms:"+ipInfoUtil.getIpAddr(request);
        String value = redisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(value)) {
            return ResultUtil.error("您发送的太频繁啦，请稍后再试");
        }
        // 生成6位数验证码
        String code = CommonUtil.getRandomNum();
        // 缓存验证码
        redisTemplate.opsForValue().set(CommonConstant.PRE_SMS + mobile, code,5L, TimeUnit.MINUTES);
        // 发送验证码
        try {

            SendSmsResponse response = smsUtil.sendCode(mobile, code, "SMS_176930019");
            if(response.getCode() != null && ("OK").equals(response.getMessage())) {
                // 请求成功 标记限流
                redisTemplate.opsForValue().set(key, "sended", 1L, TimeUnit.MINUTES);
                return ResultUtil.success("发送短信验证码成功");
            }else{
                return ResultUtil.error("请求发送验证码失败，" + response.getMessage());
            }
        } catch (ClientException e) {
            log.error("请求发送短信验证码失败，" + e);
            return ResultUtil.error("请求发送验证码失败，" + e.getMessage());
        }
    }
}
