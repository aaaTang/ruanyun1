package cn.ruanyun.backInterface.common.utils;

import org.apache.commons.codec.Charsets;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

/**
 * @program: ruanyun
 * @description: 微信支付的统一下单工具类
 * @author: fei
 * @create: 2020-02-13 16:53
 **/
public class PaymentKit {

    /**
     * 组装签名的字段
     * @param params 参数
     * @param urlEncoder 是否urlEncoder
     * @return String
     */
    public static String packageSign(Map<String, String> params, boolean urlEncoder) {
        // 先将参数以其参数名的字典序升序进行排序
        TreeMap<String, String> sortedParams = new TreeMap<String, String>(params);
        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> param : sortedParams.entrySet()) {
            String value = param.getValue();
            if (ToolUtil.isEmpty(value)) {
                continue;
            }
            if (first) {
                first = false;
            } else {
                sb.append("&");
            }
            sb.append(param.getKey()).append("=");
            if (urlEncoder) {
                try { value = urlEncode(value); } catch (UnsupportedEncodingException ignored) {}
            }
            sb.append(value);
        }
        return sb.toString();
    }

    /**
     * urlEncode
     * @param src 微信参数
     * @return String
     * @throws UnsupportedEncodingException 编码错误
     */
    public static String urlEncode(String src) throws UnsupportedEncodingException {
        return URLEncoder.encode(src, Charsets.UTF_8.name()).replace("+", "%20");
    }

    /**
     * 生成签名
     * @param params 参数
     * @param paternerKey 支付密钥
     * @return sign
     */
    public static String createSign(Map<String, String> params, String paternerKey) {
        // 生成签名前先去除sign
        params.remove("sign");
        String stringA = packageSign(params, false);
        String stringSignTemp = stringA + "&key=" + paternerKey;
        return ToolUtil.md5(stringSignTemp).toUpperCase();
    }

    /**
     * 支付异步通知时校验sign
     * @param params 参数
     * @param paternerKey 支付密钥
     * @return {boolean}
     */
    public static boolean verifyNotify(Map<String, String> params, String paternerKey){
        String sign = params.get("sign");
        String localSign = PaymentKit.createSign(params, paternerKey);
        return sign.equals(localSign);
    }

    /**
     * 微信下单，map to xml
     * @param params 参数
     * @return String
     */
    public static String toXml(Map<String, String> params) {
        StringBuilder xml = new StringBuilder();
        xml.append("<xml>");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key   = entry.getKey();
            String value = entry.getValue();
            // 略过空值
            if (ToolUtil.isEmpty(value)) continue;
            xml.append("<").append(key).append(">");
            xml.append(entry.getValue());
            xml.append("</").append(key).append(">");
        }
        xml.append("</xml>");
        return xml.toString();
    }

    /**
     * 针对支付的xml，没有嵌套节点的简单处理
     * @param xmlStr xml字符串
     * @return map集合
     */
    public static Map<String, String> xmlToMap(String xmlStr) {
       XmlHelper xmlHelper = XmlHelper.of(xmlStr);
        return xmlHelper.toMap();
    }


    /**
     * 读取request
     * @param request
     * @return
     */
    public static String readData(HttpServletRequest request) {
        BufferedReader br = null;

        try {
            StringBuilder e = new StringBuilder();
            br = request.getReader();
            String line;

            while((line = br.readLine()) != null) {
                e.append(line).append("\n");
            }

            line = e.toString();
            return line;
        } catch (IOException var12) {
            throw new RuntimeException(var12);
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException ignored) {
                }
            }

        }
    }
}
