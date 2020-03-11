package cn.ruanyun.backInterface.modules.base.controller.manage;

import cn.hutool.core.util.StrUtil;
import cn.ruanyun.backInterface.common.constant.SettingConstant;
import cn.ruanyun.backInterface.common.utils.CommonUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.entity.Setting;
import cn.ruanyun.backInterface.modules.base.service.SettingService;
import cn.ruanyun.backInterface.modules.base.vo.*;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author fei
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/setting")
public class SettingController {

    @Autowired
    private SettingService settingService;

    @RequestMapping(value = "/seeSecret/{settingName}", method = RequestMethod.GET)
    public Result<Object> seeSecret(@PathVariable String settingName) {

        String result = "";
        Setting setting = settingService.get(settingName);
        if(setting==null||StrUtil.isBlank(setting.getValue())){
            return new ResultUtil<>().setErrorMsg("配置不存在");
        }
        switch (settingName) {
            case SettingConstant.QINIU_OSS:
            case SettingConstant.ALI_OSS:
            case SettingConstant.TENCENT_OSS:
            case SettingConstant.MINIO_OSS:

                result = new Gson().fromJson(setting.getValue(), OssSetting.class).getSecretKey();
                break;
            case SettingConstant.ALI_SMS:

                result = new Gson().fromJson(setting.getValue(), SmsSetting.class).getSecretKey();
                break;
            case SettingConstant.EMAIL_SETTING:

                result = new Gson().fromJson(setting.getValue(), EmailSetting.class).getPassword();
                break;
            case SettingConstant.VAPTCHA_SETTING:

                result = new Gson().fromJson(setting.getValue(), VaptchaSetting.class).getSecretKey();
                break;
        }
        return new ResultUtil<>().setData(result);
    }

    @RequestMapping(value = "/oss/check", method = RequestMethod.GET)
    public Result<Object> osscheck() {

        Setting setting = settingService.get(SettingConstant.OSS_USED);
        if(setting==null||StrUtil.isBlank(setting.getValue())){
            return new ResultUtil<>().setErrorMsg(501, "您还未配置第三方OSS服务");
        }
        return new ResultUtil<>().setData(setting.getValue());
    }

    @RequestMapping(value = "/oss/{serviceName}", method = RequestMethod.GET)
    public Result<OssSetting> oss(@PathVariable String serviceName) {

        Setting setting = new Setting();
        if(serviceName.equals(SettingConstant.QINIU_OSS)||serviceName.equals(SettingConstant.ALI_OSS)
                ||serviceName.equals(SettingConstant.TENCENT_OSS)||serviceName.equals(SettingConstant.MINIO_OSS)
                ||serviceName.equals(SettingConstant.LOCAL_OSS)){
            setting = settingService.get(serviceName);
        }
        if(setting==null||StrUtil.isBlank(setting.getValue())){
            return new ResultUtil<OssSetting>().setData(null);
        }
       OssSetting ossSetting = new Gson().fromJson(setting.getValue(), OssSetting.class);
        ossSetting.setSecretKey("**********");
        return new ResultUtil<OssSetting>().setData(ossSetting);
    }

    @RequestMapping(value = "/sms/{serviceName}", method = RequestMethod.GET)
    public Result<SmsSetting> sms(@PathVariable String serviceName) {

        Setting setting = new Setting();
        if(serviceName.equals(SettingConstant.ALI_SMS)){
            setting = settingService.get(SettingConstant.ALI_SMS);
        }
        if(setting==null||StrUtil.isBlank(setting.getValue())){
            return new ResultUtil<SmsSetting>().setData(null);
        }
        SmsSetting smsSetting = new Gson().fromJson(setting.getValue(),SmsSetting.class);
        smsSetting.setSecretKey("**********");
        if(smsSetting.getType()!=null){
            Setting code = settingService.get(CommonUtil.getSmsTemplate(smsSetting.getType()));
            smsSetting.setTemplateCode(code.getValue());
        }
        return new ResultUtil<SmsSetting>().setData(smsSetting);
    }

    @RequestMapping(value = "/sms/templateCode/{type}", method = RequestMethod.GET)
    public Result<String> smsTemplateCode(@PathVariable Integer type) {

        String templateCode = "";
        if(type!=null){
            String template = CommonUtil.getSmsTemplate(type);
            Setting setting = settingService.get(template);
            if(StrUtil.isNotBlank(setting.getValue())){
                templateCode = setting.getValue();
            }
        }
        return new ResultUtil<String>().setData(templateCode);
    }

    @RequestMapping(value = "/vaptcha", method = RequestMethod.GET)
    public Result<VaptchaSetting> vaptcha() {

        Setting setting = settingService.get(SettingConstant.VAPTCHA_SETTING);
        if(setting==null||StrUtil.isBlank(setting.getValue())){
            return new ResultUtil<VaptchaSetting>().setData(null);
        }
       VaptchaSetting vaptchaSetting = new Gson().fromJson(setting.getValue(), VaptchaSetting.class);
        vaptchaSetting.setSecretKey("**********");
        return new ResultUtil<VaptchaSetting>().setData(vaptchaSetting);
    }

    @RequestMapping(value = "/email", method = RequestMethod.GET)
    public Result<EmailSetting> email() {

        Setting setting = settingService.get(SettingConstant.EMAIL_SETTING);
        if(setting==null||StrUtil.isBlank(setting.getValue())){
            return new ResultUtil<EmailSetting>().setData(null);
        }
        EmailSetting emailSetting = new Gson().fromJson(setting.getValue(), EmailSetting.class);
        emailSetting.setPassword("**********");
        return new ResultUtil<EmailSetting>().setData(emailSetting);
    }

    @RequestMapping(value = "/other", method = RequestMethod.GET)
    public Result<OtherSetting> other() {

        Setting setting = settingService.get(SettingConstant.OTHER_SETTING);
        if(setting==null||StrUtil.isBlank(setting.getValue())){
            return new ResultUtil<OtherSetting>().setData(null);
        }
        OtherSetting otherSetting = new Gson().fromJson(setting.getValue(), OtherSetting.class);
        return new ResultUtil<OtherSetting>().setData(otherSetting);
    }

    @RequestMapping(value = "/oss/set", method = RequestMethod.POST)
    public Result<Object> ossSet(@ModelAttribute OssSetting ossSetting) {

        String name = ossSetting.getServiceName();
        Setting setting = settingService.get(name);
        if(name.equals(SettingConstant.QINIU_OSS)||name.equals(SettingConstant.ALI_OSS)
                ||name.equals(SettingConstant.TENCENT_OSS)||name.equals(SettingConstant.MINIO_OSS)){

            // 判断是否修改secrectKey 保留原secrectKey 避免保存***加密字符
            if(StrUtil.isNotBlank(setting.getValue())&&!ossSetting.getChanged()){
                String secrectKey = new Gson().fromJson(setting.getValue(),OssSetting.class).getSecretKey();
                ossSetting.setSecretKey(secrectKey);
            }
        }
        setting.setValue(new Gson().toJson(ossSetting));
        settingService.saveOrUpdate(setting);
        Setting used = settingService.get(SettingConstant.OSS_USED);
        used.setValue(name);
        settingService.saveOrUpdate(used);
        return new ResultUtil<>().setData(null);
    }

    @RequestMapping(value = "/sms/set", method = RequestMethod.POST)
    public Result<Object> smsSet(@ModelAttribute SmsSetting smsSetting) {

        if(smsSetting.getServiceName().equals(SettingConstant.ALI_SMS)){
            // 阿里
            Setting setting = settingService.get(SettingConstant.ALI_SMS);
            if(StrUtil.isNotBlank(setting.getValue())&&!smsSetting.getChanged()){
                String secrectKey = new Gson().fromJson(setting.getValue(), SmsSetting.class).getSecretKey();
                smsSetting.setSecretKey(secrectKey);
            }
            if(smsSetting.getType()!=null){
                Setting codeSetting = settingService.get(CommonUtil.getSmsTemplate(smsSetting.getType()));
                codeSetting.setValue(smsSetting.getTemplateCode());
                settingService.saveOrUpdate(codeSetting);
            }
            smsSetting.setType(null);
            smsSetting.setTemplateCode(null);
            setting.setValue(new Gson().toJson(smsSetting));
            settingService.saveOrUpdate(setting);

            Setting used = settingService.get(SettingConstant.SMS_USED);
            used.setValue(SettingConstant.ALI_SMS);
            settingService.saveOrUpdate(used);
        }
        return new ResultUtil<>().setData(null);
    }

    @RequestMapping(value = "/email/set", method = RequestMethod.POST)
    public Result<Object> emailSet(@ModelAttribute EmailSetting emailSetting) {

        Setting setting = settingService.get(SettingConstant.EMAIL_SETTING);
        if(StrUtil.isNotBlank(setting.getValue())&&!emailSetting.getChanged()){
            String password = new Gson().fromJson(setting.getValue(), EmailSetting.class).getPassword();
            emailSetting.setPassword(password);
        }
        setting.setValue(new Gson().toJson(emailSetting));
        settingService.saveOrUpdate(setting);
        return new ResultUtil<>().setData(null);
    }

    @RequestMapping(value = "/vaptcha/set", method = RequestMethod.POST)
    public Result<Object> vaptchaSet(@ModelAttribute VaptchaSetting vaptchaSetting) {

        Setting setting = settingService.get(SettingConstant.VAPTCHA_SETTING);
        if(StrUtil.isNotBlank(setting.getValue())&&!vaptchaSetting.getChanged()){
            String key = new Gson().fromJson(setting.getValue(), VaptchaSetting.class).getSecretKey();
            vaptchaSetting.setSecretKey(key);
        }
        setting.setValue(new Gson().toJson(vaptchaSetting));
        settingService.saveOrUpdate(setting);
        return new ResultUtil<>().setData(null);
    }

    @RequestMapping(value = "/other/set", method = RequestMethod.POST)
    public Result<Object> otherSet(@ModelAttribute OtherSetting otherSetting) {

        Setting setting = settingService.get(SettingConstant.OTHER_SETTING);
        setting.setValue(new Gson().toJson(otherSetting));
        settingService.saveOrUpdate(setting);
        return new ResultUtil<      >().setData(null);
    }
}
