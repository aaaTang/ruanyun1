package cn.ruanyun.backInterface.common.pay.common.wxpay;

import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.WxPayApiConfigKit;
import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * @ClassTitle: WeChatConfig
 * @ProjectName fresh_project
 * @Description: ;参数
 * @Author Sangsang
 * @Date 2020/2/5
 * @Time 19:42
 */
@Component
@Getter
public class WeChatConfig {

	/**
	 * 公众账号ID
	 */
	public static final String APP_ID = "wxdf4ca1e524b376de";

	/**
	 * 商户号
	 */
	public static final String MCH_ID = "1594501541";

	/**
	 * 商户密钥 API密钥
	 */
	public static final String KEY = "3RGR1M0vug41Yov82N82a2xafUwChESI";

	/**
	 * 微信支付 - 回调地址 : 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。（需要配置）
	 */
	public static final String NOTIFY_URL = "http://47.99.50.236:8085/ruanyun/order/wxPayNotify";

	/**
	 * 	支付方式   JSAPI，NATIVE，APP
	 */
	public static final String TRADE_TYPE = "APP";

	/**
	 * 微信支付 - 统一下单地址
	 */
	public static final String PLACEANORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";


	public  static WxPayApiConfig getWxPayApiConfig(){
		WxPayApiConfig apiConfig ;
		try {
			apiConfig = WxPayApiConfigKit.getApiConfig(WeChatConfig.APP_ID);
		} catch (Exception e) {
			apiConfig = WxPayApiConfig.builder()
					.appId(WeChatConfig.APP_ID)
					.mchId(WeChatConfig.MCH_ID)
					.partnerKey(WeChatConfig.KEY)
					.certPath(null)
					.domain(null)
					.build();
		}
		return apiConfig;

	}

}
