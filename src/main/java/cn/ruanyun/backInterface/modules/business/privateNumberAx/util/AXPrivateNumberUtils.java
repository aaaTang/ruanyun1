package cn.ruanyun.backInterface.modules.business.privateNumberAx.util;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author zengqifeng
 * @version 1.0
 * @date 2019/11/6 14:18
 * 华为号码保护工具类AX方式
 */
public class AXPrivateNumberUtils {

    //号码绑定和解绑等接口详细信息请参考华为官方文档地址
    //地址如下：https://support.huaweicloud.com/api-PrivateNumber/privatenumber_02_0012.html
    /**
     * 必填,请登录管理控制台,从"应用管理"页获取
     */
    private final static String OMPAPPKEY = "***"; // APP_Key
    private final static String OMPAPPSECRET = "***"; // APP_Secret
    private final static String OMPDOMAINNAME = "***"; // APP接入地址
    /**
     * 隐私号码不同绑定模式的URL
     */
    private final static String AXURL = "/rest/provision/caas/privatenumber/v1.0";//AX模式绑定信息修改接口访问URI

    /**
     * AXB 模式号码绑定
     * @param jsonObject
     * @return
     */
    public static String axBindNumber(JSONObject jsonObject) {
        //获取真正的访问url
        String realUrl = OMPDOMAINNAME + AXURL;
        // 封装JOSN请求
        /**
         * 前三项必填 后面选填,各参数要求请参考"AX模式绑定接口"
         */
//        json.put("origNum", origNum); // A方真实号码(手机或固话)
//        json.put("privateNum", privateNum); // 已订购的隐私号码(X号码)
//        json.put("calleeNumDisplay", calleeNumDisplay); // 设置非A用户呼叫X时,A接到呼叫时的主显号码

//        json.put("privateNumType", "mobile-virtual"); //固定为mobile-virtual
//        json.put("areaCode", "0755"); //城市码
//        json.put("recordFlag", "true"); ////是否通话录音
//        json.put("recordHintTone", "recordHintTone.wav"); //录音提示音
//        json.put("privateSms", "true"); //是否支持短信功能
//        JSONObject preVoice = new JSONObject();
//        preVoice.put("callerHintTone", "callerHintTone.wav"); //设置A拨打X号码时的通话前等待音
//        preVoice.put("calleeHintTone", "calleeHintTone.wav"); //设置非A用户拨打X号码时的通话前等待音
//        json.put("preVoice", preVoice); //个性化通话前等待音

        //发送请求
        String result = HttpUtil.sendPost(OMPAPPKEY, OMPAPPSECRET, realUrl, jsonObject.toString());
        return result;
    }

    /**
     * AXB 修改绑定信息
     * @param jsonObject
     * @return
     */
    public static String axModifyNumber(JSONObject jsonObject) {
        //获取真正的访问url
        String realUrl = OMPDOMAINNAME + AXURL;
        /**
         * 选填,各参数要求请参考"AXB模式绑定信息修改接口"
         */
        // 封装JOSN请求
        /*JSONObject json = new JSONObject();
        if (StringUtils.isNotBlank(subscriptionId)) {
            json.put("subscriptionId", subscriptionId); // 绑定关系ID
        }
        if (StringUtils.isNotBlank(origNum)) {
            json.put("origNum", origNum); // AX中的A号码
        }
        if (StringUtils.isNotBlank(privateNum)) {
            json.put("privateNum", privateNum); // AX中的X号码
        }
        json.put("privateSms", privateSms); // 是否支持短信功能*/
        //发送请求
        String result = HttpUtil.sendPut(OMPAPPKEY, OMPAPPSECRET, realUrl, jsonObject.toString());
        return result;
    }

    /**
     * AXB 修改绑定信息
     * @return
     */
    public static String axUnbindNumber(Map map) {
        // 必填,AX模式解绑接口访问URI
        String realUrl = OMPDOMAINNAME + AXURL;
       /* // 申明对象   相关参数说明
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(subscriptionId)) {
            map.put("subscriptionId", subscriptionId); // 绑定关系ID
        }
        if (StringUtils.isNotBlank(origNum)) {
            map.put("origNum", origNum); // AX中的A号码
        }
        if (StringUtils.isNotBlank(privateNum)) {
            map.put("privateNum", privateNum); // AX中的X号码
        }*/
        String result = HttpUtil.sendDelete(OMPAPPKEY, OMPAPPSECRET, realUrl, HttpUtil.map2UrlEncodeString(map));
        return result;
    }
}
