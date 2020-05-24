package cn.ruanyun.backInterface.modules.jpush.util;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-05-20 14:39
 **/
@Component
@Slf4j
public class JPushUtil {


    /**
     * appkey
     */
    private static final String JPUSH_KEY = "67cc92b03a914067ded6b3d7";


    /**
     * appSecret
     */
    private static final String JPUSH_SECRET = "ae16690a1b16bcb51ba28f46";

    /**
     * 创建 基础 JPushClient
     * @return JPushClient
     */
    private JPushClient getjpushclient() {
        return new JPushClient(JPUSH_SECRET, JPUSH_KEY);
    }

    /**
     * 注销pushid绑定的tag和alias
     *
     * @param pushId
     *            极光的推送编号
     * @throws APIRequestException  APIRequestException
     * @throws APIConnectionException APIRequestException
     */
    public void logout(String pushId) throws APIConnectionException, APIRequestException {
        getjpushclient().updateDeviceTagAlias(pushId, true, true);
    }
    /**
     * 更新用户tags和alias
     * @param pushId
     * 			推送push编号
     * @param cardNumber
     * 			用户卡号
     * @param tagsToAdd
     * 			用户标签
     * @throws APIConnectionException APIConnectionException
     * @throws APIRequestException APIRequestException
     */
    public void updateTagAlias(String pushId, String cardNumber, Set<String> tagsToAdd)
            throws APIConnectionException, APIRequestException {
        getjpushclient().updateDeviceTagAlias(pushId, cardNumber, tagsToAdd,
                new HashSet<>(getjpushclient().getDeviceTagAlias(pushId).tags));
    }
    /**
     * 进行推送
     * @param build build
     * @throws APIConnectionException APIConnectionException
     * @throws APIRequestException APIRequestException
     */
    public void sendPush(PushPayload build) throws APIConnectionException, APIRequestException {
        getjpushclient().sendPush(build);
    }

    /**
     *向所有平台单个或多个指定Tag用户推送消息
     * @param tagsList
     * @param msg_content
     * @param extrasparam
     * @return
     */
    private static PushPayload buildPushObject_all_tagList_alertWithTitle(List<String> tagsList,
                                                                       String msg_content,
                                                                       String extraKey,
                                                                       String extrasparam,
                                                                       String notification_title) {

        log.info("----------向所有平台单个或多个指定Tag用户推送消息中.......");

        return PushPayload.newBuilder()
                //指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
                .setPlatform(Platform.all())
                //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
                .setAudience(Audience.tag(tagsList))
                //jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
                .setNotification(Notification.newBuilder()
                        //指定当前推送的android通知
                        .addPlatformNotification(AndroidNotification.newBuilder()

                                .setAlert(msg_content)
                                .setTitle(notification_title)
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra(extraKey,extrasparam)

                                .build())
                        //指定当前推送的iOS通知
                        .addPlatformNotification(IosNotification.newBuilder()
                                //传一个IosAlert对象，指定apns title、title、subtitle等
                                .setAlert(msg_content)
                                //直接传alert
                                //此项是指定此推送的badge自动加1
                                .incrBadge(1)
                                //此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                                .setSound("default")
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra(extraKey,extrasparam)
                                //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                //取消此注释，消息推送时ios将无法在锁屏情况接收
                                // .setContentAvailable(true)

                                .build())


                        .build())
                //Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
                /*.setMessage(Message.newBuilder()

                        .setMsgContent(msg_content)

                        .setTitle(msg_title)

                        .addExtra("message extras key",extrasparam)

                        .build())*/
                .build();

    }
}
