package cn.ruanyun.backInterface.modules.base.controller.manage;

import cn.hutool.core.util.StrUtil;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.constant.SettingConstant;
import cn.ruanyun.backInterface.common.exception.RuanyunException;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.EmailValidate;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.entity.Setting;
import cn.ruanyun.backInterface.modules.base.entity.User;
import cn.ruanyun.backInterface.modules.base.service.SettingService;
import cn.ruanyun.backInterface.modules.base.service.UserService;
import cn.ruanyun.backInterface.modules.base.vo.OtherSetting;
import com.google.gson.Gson;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author fei
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/email")
@Transactional
public class EmailValidateController {

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private IpInfoUtil ipInfoUtil;

    @Autowired
    private SettingService settingService;

    @Autowired
    private SecurityUtil securityUtil;

    public OtherSetting getOtherSetting(){

        Setting setting = settingService.get(SettingConstant.OTHER_SETTING);
        if(StrUtil.isBlank(setting.getValue())){
            throw new RuanyunException("系统未配置访问域名，请联系管理员");
        }
        return new Gson().fromJson(setting.getValue(), OtherSetting.class);
    }

    @RequestMapping(value = "/sendEditCode/{email}", method = RequestMethod.GET)
    public Result<Object> sendEditCode(@PathVariable String email,
                                       HttpServletRequest request){

        return sendEmailCode(email, "修改邮箱", "【软云】修改邮箱验证","code-email", request);
    }

    @RequestMapping(value = "/sendResetCode/{email}", method = RequestMethod.GET)
    public Result<Object> sendResetCode(@PathVariable String email,
                                        HttpServletRequest request){

        return sendEmailCode(email, "重置密码", "【软云】重置密码邮箱验证", "code-email", request);
    }

    /**
     * 发送邮件验证码
     * @param email
     * @param operation
     * @param title
     * @param template
     * @param request
     * @return
     */
    public Result<Object> sendEmailCode(String email, String operation, String title, String template, HttpServletRequest request){

        // 生成验证码 存入相关信息
        EmailValidate e = new EmailValidate();
        e.setOperation(operation);
        // 验证是否注册
        User user = userService.findByEmail(email);
        if("修改邮箱".equals(operation)){
            if(user!=null){
                return ResultUtil.error("该邮箱已绑定其他账号");
            }
            User u = securityUtil.getCurrUser();
            e.setUsername(u.getUsername());
        }else if("重置密码".equals(operation)){
            if(user==null){
                return ResultUtil.error("该邮箱未注册");
            }
            e.setUsername(user.getUsername());
        }

        // IP限流 1分钟限1个请求
        String key = "sendEmailCode:"+ipInfoUtil.getIpAddr(request);
        String value = redisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(value)) {
            return ResultUtil.error("您发送的太频繁啦，请稍后再试");
        }

        String code = CommonUtil.getRandomNum();
        e.setCode(code);
        e.setEmail(email);
        e.setFullUrl(getOtherSetting().getDomain());
        redisTemplate.opsForValue().set(CommonConstant.PRE_EMAIL + email, new Gson().toJson(e, EmailValidate.class), 10L, TimeUnit.MINUTES);

        emailUtil.sendTemplateEmail(email, title, template, e);
        // 请求成功 标记限流
        redisTemplate.opsForValue().set(key, "sended", 1L, TimeUnit.MINUTES);
        return ResultUtil.success("发送成功");
    }

    @RequestMapping(value = "/editEmail", method = RequestMethod.POST)
    @ApiOperation(value = "修改邮箱或重置密码")
    public Result<Object> editEmail(@RequestParam String email) {

        User u = securityUtil.getCurrUser();
        u.setEmail(email);
        userService.update(u);
        // 删除缓存
        redisTemplate.delete("user::"+u.getUsername());
        return new ResultUtil<>().setSuccessMsg("修改邮箱成功");
    }

    @RequestMapping(value = "/resetByEmail", method = RequestMethod.POST)
    public Result<Object> resetByEmail(@RequestParam String email,
                                       @RequestParam String password,
                                       @RequestParam String passStrength) {

        User u = userService.findByEmail(email);

        //在线DEMO所需
        if("test".equals(u.getUsername())||"test2".equals(u.getUsername())){
            return new ResultUtil<>().setErrorMsg("演示账号不支持重置密码");
        }

        String encryptPass = new BCryptPasswordEncoder().encode(password);
        u.setPassword(encryptPass);
        u.setPassStrength(passStrength);
        userService.update(u);
        // 删除缓存
        redisTemplate.delete("user::"+u.getUsername());
        return new ResultUtil<>().setSuccessMsg("重置密码成功");
    }
}
