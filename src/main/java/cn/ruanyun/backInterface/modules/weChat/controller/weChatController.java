package cn.ruanyun.backInterface.modules.weChat.controller;


import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.weChat.entity.WeChat;
import cn.ruanyun.backInterface.modules.weChat.service.IweChatService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

/**
 * @author fei
 */
@Slf4j
@RestController
@Api(description = "微信管理接口")
@RequestMapping("/ruanyun/weChat")
@Transactional
public class weChatController {

    @Autowired
    private IweChatService iweChatService;

    /**
     * 微信小程序获取openId
     *
     * @param code
     * @return
     */
    @PostMapping("/getOpenid")
    public Result<Object> getOpenid(String code) throws UnsupportedEncodingException {
        return iweChatService.getOpenId(code);
    }

    /**
     * 微信登录
     *
     * @param weChat
     * @return
     */
    @PostMapping("/login")
    public Result<Object> login(WeChat weChat) {

        return iweChatService.login(weChat);
    }
}
