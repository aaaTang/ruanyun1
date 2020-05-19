package cn.ruanyun.backInterface.modules.business.privateNumberAx.util;

import cn.ruanyun.backInterface.common.utils.ToolUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

import javax.activation.MailcapCommandMap;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zengqifeng
 * @version 1.0
 * @date 2019/11/6 14:18
 * 华为号码保护工具类AXB方式
 */
public class AXBPrivateNumberUtils {

    //号码绑定和解绑等接口详细信息请参考华为官方文档地址
    //地址如下：https://support.huaweicloud.com/api-PrivateNumber/privatenumber_02_0012.html
    /**
     * 必填,请登录管理控制台,从"应用管理"页获取
     */
    // APP_Key
    private final static String OMPAPPKEY = "MR6LZ858cYDFLy59a4T649yK44sX";

    // APP_Secret
    private final static String OMPAPPSECRET = "w75OY021kD5umA9mZgY4h2mv2U49";

    // APP接入地址
    private final static String OMPDOMAINNAME = "https://rtcapi.cn-north-1.myhuaweicloud.com:12543";

    /**
     * 隐私号码不同绑定模式的URL
     * AXB模式绑定信息修改接口访问URI
     */
    private final static String AXBURL = "/rest/caas/relationnumber/partners/v1.0";


    /**
     *
     * @param callerNum A方真实号码(手机或固话)
     * @param calleeNum B方真实号码(手机或固话)
     * @param relationNum X号码(关系号码)
     * @return string
     */
    public static String axbBindNumber(String callerNum, String calleeNum, String relationNum, String cityCode) {
        //获取真正的访问url
        String realUrl = OMPDOMAINNAME + AXBURL;
        JSONObject param = new JSONObject();

        // X号码(关系号码)
        param.put("relationNum", relationNum);

        // A方真实号码(手机或固话)
        param.put("callerNum", callerNum);

        // B方真实号码(手机或固话)
        param.put("calleeNum", calleeNum);

        //城市码
        param.put("areaCode", cityCode);
//         json.put("callDirection", 0); //允许呼叫的方向
//         json.put("duration", 86400); //绑定关系保持时间
//         json.put("recordFlag", "false"); //是否通话录音
//         json.put("recordHintTone", "recordHintTone.wav"); //录音提示音
//         json.put("maxDuration", 60); //单次通话最长时间
//         json.put("lastMinVoice", "lastMinVoice.wav"); //通话最后一分钟提示音
//         json.put("privateSms", "true"); //是否支持短信功能
//         JSONObject preVoice = new JSONObject();
//         preVoice.put("callerHintTone", "callerHintTone.wav"); //设置A拨打X号码时的通话前等待音
//         preVoice.put("calleeHintTone", "calleeHintTone.wav"); //设置B拨打X号码时的通话前等待音
//         json.put("preVoice", preVoice); //个性化通话前等待音

        //发送请求
        return HttpUtil.sendPost(OMPAPPKEY, OMPAPPSECRET, realUrl, param.toString());
    }

    /**
     * AXB 修改绑定信息
     * @param jsonObject
     * @return
     */
    public static String axbModifyNumber(JSONObject jsonObject) {
        //获取真正的访问url
        String realUrl = OMPDOMAINNAME + AXBURL;
        /**
         * 选填,各参数要求请参考"AXB模式绑定信息修改接口"
         */
        // 封装JOSN请求
//        JSONObject json = new JSONObject();
//        json.put("subscriptionId", subscriptionId); // 绑定关系ID
//        if (StringUtils.isNotBlank(callerNum)) {
//            json.put("callerNum", callerNum); // 将A方修改为新的号码(手机或固话)
//        }
//        if (StringUtils.isNotBlank(calleeNum)) {
//            json.put("calleeNum", calleeNum); // 将B方修改为新的号码(手机或固话)
//        }
//         json.put("callDirection", 0); //允许呼叫的方向
//         json.put("duration", 86400); //绑定关系保持时间
//         json.put("maxDuration", 90); //单次通话最长时间
//         json.put("lastMinVoice", "lastMinVoice.wav"); //通话最后一分钟提示音
//         json.put("privateSms", "true"); //是否支持短信功能
//         JSONObject preVoice = new JSONObject();
//         preVoice.put("callerHintTone", "callerHintTone.wav"); //设置A拨打X号码时的通话前等待音
//         preVoice.put("calleeHintTone", "calleeHintTone.wav"); //设置B拨打X号码时的通话前等待音
//         json.put("preVoice", preVoice); //个性化通话前等待音

        //发送请求
        String result = HttpUtil.sendPut(OMPAPPKEY, OMPAPPSECRET, realUrl, jsonObject.toString());
        return result;
    }


    /**
     * AXB 修改绑定信息
     * @param relationNum 号码
     * @param subscriptionId id
     * @return String
     */
    public static String axbUnbindNumber(String relationNum, String subscriptionId) {
        // 必填,AXB模式解绑接口访问URI
        String realUrl = OMPDOMAINNAME + AXBURL;
        // 申明对象 先关参数说明
        Map<String, Object> map = Maps.newHashMap();
        if (ToolUtil.isNotEmpty(subscriptionId)) {

            // 绑定关系ID
            map.put("subscriptionId", subscriptionId);
        }
        if (ToolUtil.isNotEmpty(relationNum)) {

            // X号码(关系号码)
            map.put("relationNum", relationNum);
        }
        //发送请求
        return HttpUtil.sendDelete(OMPAPPKEY, OMPAPPSECRET, realUrl, HttpUtil.map2UrlEncodeString(map));
    }


    public static void main(String[] args) {

        axbUnbindNumber("+8616508765178", null);
    }
}