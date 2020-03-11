package cn.ruanyun.backInterface.modules.jpush.service;

import cn.jpush.api.push.model.PushPayload;
import cn.ruanyun.backInterface.modules.jpush.bean.PushBean;


/**
 * 极光推送
 * 封装第三方api
 */
public interface MyJiGuangPushService {

    /**
     * 广播 (所有平台，所有设备, 不支持附加信息)
     * @param pushBean 推送内容
     * @return
     */
    boolean pushAll(PushBean pushBean);


    /**
     * ios广播
     * @param pushBean 推送内容
     * @return
     */
    boolean pushIos(PushBean pushBean);


    /**
     * ios通过registid推送 (一次推送最多 1000 个)
     * @param pushBean 推送内容
     * @param registids 推送id
     * @return
     */
    boolean pushIos(PushBean pushBean, String... registids);

    /**
     * android广播
     * @param pushBean 推送内容
     * @return
     */
    boolean pushAndroid(PushBean pushBean);


    /**
     * android通过registid推送 (一次推送最多 1000 个)
     * @param pushBean 推送内容
     * @param registids 推送id
     * @return
     */
    boolean pushAndroid(PushBean pushBean, String... registids);


    /**
     * 调用api推送
     * @param pushPayload 推送实体
     * @return
     */
    boolean sendPush(PushPayload pushPayload);

}
