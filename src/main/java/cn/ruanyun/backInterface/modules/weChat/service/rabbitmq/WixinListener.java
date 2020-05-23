package cn.ruanyun.backInterface.modules.weChat.service.rabbitmq;


import cn.ruanyun.backInterface.common.utils.RedisUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.pojo.UserRole;
import cn.ruanyun.backInterface.modules.base.service.UserService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserRoleService;
import cn.ruanyun.backInterface.modules.weChat.entity.WeChat;
import cn.ruanyun.backInterface.modules.weChat.service.IweChatService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @program: ruanyun-plus
 * @description:
 * @author: fei
 * @create: 2020-02-09 16:31
 **/
@Component
@Slf4j
public class WixinListener {

    @Autowired
    private IweChatService weChatService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private IUserRoleService userRoleService;


    /*
    微信appId
     */
    @Value("${ruanyun.social.weixin.appId}")
    private String appId;

    /*
    微信appKey
     */
    @Value("${ruanyun.social.weixin.secret}")
    private String secret;

    /***
     * 业务流程
     * @describe 1.判断openId 是否存在
     * @describe 1.1 如果存在,判断是否绑定用户,如果绑定用户直接登录。
     * @describe 1.2 如果没有绑定用户，重新绑定.如果不存在获取token,获取用户基本信息。
     * @param weChat
     */
    public String resolveOpenId(WeChat weChat) {

        WeChat weChatGet = weChatService.getOne(Wrappers.<WeChat>lambdaQuery()
                .eq(WeChat::getOpenId, weChat.getOpenId()));

        if (ToolUtil.isEmpty(weChatGet)) {

            //插入微信信息
            WeChat weChatInsert = new WeChat();
            weChatInsert.setUsername(weChat.getUsername())
                    .setAvatar(weChat.getAvatar())
                    .setOpenId(weChat.getOpenId());
            weChatService.save(weChatInsert);


            //插入用户信息表
            User user = new User();
            user.setUsername(weChat.getUsername())
                    .setNickName(weChat.getUsername())
                    .setAvatar(weChat.getAvatar());
            userService.save(user);

            //插入角色表
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId("496138616573953");
            userRoleService.save(userRole);

            //登录微信
            return login(weChat.getUsername());


        } else {

            return login(weChatGet.getRelateUsername());
        }
    }


    public String login(String username) {

        String JWT = securityUtil.getToken(username, true);
//          // 存入redis
//          String JWTKey = UUID.randomUUID().toString().replace("-","");
//          RedisUtil.set(JWTKey, JWT, 2000);

        return JWT;
    }


}
