package cn.ruanyun.backInterface.modules.weChat.serviceimpl;


import cn.hutool.http.HttpUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.weChat.DTO.RongyunUser;
import cn.ruanyun.backInterface.modules.weChat.entity.WeChat;
import cn.ruanyun.backInterface.modules.weChat.mapper.WeChatMapper;
import cn.ruanyun.backInterface.modules.weChat.service.IweChatService;
import cn.ruanyun.backInterface.modules.weChat.service.rabbitmq.WixinListener;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 微信接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IweChatServiceImpl extends ServiceImpl<WeChatMapper, WeChat> implements IweChatService {


    @Autowired
    private WixinListener wixinListener;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private IUserService userService;

    /**
     * 微信appId
     */
    @Value("${ruanyun.social.weixin.appId}")
    private String appId;

    /**
     * 微信appKey
     */
    @Value("${ruanyun.social.weixin.secret}")
    private String secret;


    @Override
    public Result<Object> getOpenId (String code) {


        if (ToolUtil.isEmpty(code)){
            return new ResultUtil<>().setErrorMsg(201,"暂未授权！");
        }

        log.info("当前的appid是" + appId);
        //1.通过code获取openId
        String str = "https://api.weixin.qq.com/sns/jscode2session?appid=" +
                appId +
                "&secret=" +
                secret +
                "&js_code=" +
                code +
                "&grant_type=authorization_code";
        String result = HttpUtil.get(str);

        //判断是否获取成功
        Integer errorCode = JSONObject.parseObject(result).getInteger("errcode");
        String  errmsg = JSONObject.parseObject(result).getString("errmsg");
        if (ToolUtil.isNotEmpty(errorCode)) {
            return new ResultUtil<>().setErrorMsg(202, errmsg);
        }

        String openid = JSONObject.parseObject(result).getString("openid");

        return new ResultUtil<>().setData(openid,"获取openid成功！");
    }

    @Override
    public Result<Object> login(WeChat weChat) {

       return Optional.ofNullable(wixinListener.resolveOpenId(weChat)).map(token -> new ResultUtil<>().setData(token,"登录成功！"))
               .orElse(new ResultUtil<>().setErrorMsg(201,"登录失败！"));

    }

    /**
     * 获取用户openId
     *
     * @return
     */
    @Override
    public String getOpenId() {

       return Optional.ofNullable(this.getOne(Wrappers.<WeChat>lambdaQuery()
            .eq(WeChat::getRelateUsername,securityUtil.getCurrUser().getUsername())))
            .map(WeChat::getOpenId)
            .orElse(null);
    }

}