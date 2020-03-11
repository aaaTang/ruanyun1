package cn.ruanyun.backInterface.config.jiGuang;


import cn.jpush.api.JPushClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@EnableConfigurationProperties(JiGuangConfig.class)
@ConfigurationProperties(prefix = "social.jpush")
@Data
public class JiGuangConfig {

    // 极光官网-个人管理中心-appkey
    @Value("appkey")
    private String appkey;

    // 极光官网-个人管理中心-点击查看-secret
    @Value("secret")
    private String secret;


    private JPushClient jPushClient;

   /* // 推送客户端
    @PostConstruct
    public void initJPushClient() {
        jPushClient = new JPushClient(secret, appkey);
    }*/

    // 获取推送客户端
    public JPushClient getJPushClient() {
        return jPushClient;
    }

}
