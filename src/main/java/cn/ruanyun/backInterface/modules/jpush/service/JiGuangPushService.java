package cn.ruanyun.backInterface.modules.jpush.service;

import cn.ruanyun.backInterface.modules.jpush.bean.PushBean;

/**
 * 推送服务
 * 封装业务功能相关
 */
public interface JiGuangPushService {


    /**
     * 推送全部, 不支持附加信息
     * @return
     */
    boolean pushAll(PushBean pushBean);


    /**
     * 推送全部ios
     * @return
     */
    boolean pushIos(PushBean pushBean);


    /**
     * 推送ios 指定id
     * @return
     */
    boolean pushIos(PushBean pushBean, String... registids);


    /**
     * 推送全部android
     * @return
     */
    boolean pushAndroid(PushBean pushBean);


    /**
     * 推送android 指定id
     * @return
     */
    boolean pushAndroid(PushBean pushBean, String... registids);


    /**
     * 剔除无效registed
     * @param registids
     * @return
     */
    String[] checkRegistids(String[] registids);

}
