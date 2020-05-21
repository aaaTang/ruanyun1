package cn.ruanyun.backInterface.modules.jpush.util;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.PushPayload;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-05-20 14:39
 **/
@Component
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
}
