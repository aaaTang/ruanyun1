package cn.ruanyun.backInterface.modules.weChat.service;



import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.weChat.entity.WeChat;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.UnsupportedEncodingException;

/**
 * 微信接口
 * @author fei
 */
public interface IweChatService extends IService<WeChat> {



    /**
     *发送openId 到rabbitmq
     * @param code
     */
    Result<Object> getOpenId(String code) throws UnsupportedEncodingException;

    /**
     * 登录
     * @param weChat
     * @return
     */
    Result<Object> login(WeChat weChat);

    /**
     * 获取用户openId
     * @return
     */
    String getOpenId();
}