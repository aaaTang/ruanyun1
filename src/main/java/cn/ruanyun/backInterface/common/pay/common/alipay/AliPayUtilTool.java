package cn.ruanyun.backInterface.common.pay.common.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @ClassTitle: AliPayUtilTool
 * @ProjectName fresh_project
 * @Description: 阿里支付工具类
 * @Author Sangsang
 * @Date 2020/2/8
 * @Time 22:59
 */
public class AliPayUtilTool {


	/**
	 * @Description TD: 传入model参数，获取阿里支付的响应参数(APP支付)
	 * @param model
	 * @Return com.alipay.api.response.AlipayTradeAppPayResponse
	 * @Author sangsang
	 * @Date 2020/2/9 11:31
	 **/
	public static AlipayTradeAppPayResponse getResponse(AlipayTradeAppPayModel model) throws AlipayApiException {
		AlipayTradeAppPayRequest alipayRequest = new AlipayTradeAppPayRequest();
//		alipayRequest.setReturnUrl(AlipayConfig.RETURN_URL);
		alipayRequest.setNotifyUrl(AlipayConfig.NOTIFY_URL);
		alipayRequest.setBizModel(model);
		AlipayClient  alipayClient = new DefaultAlipayClient(AlipayConfig.GATEWAY_URL, AlipayConfig.APP_ID, AlipayConfig.MERCHANT_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGN_TYPE); ;
		return alipayClient.sdkExecute(alipayRequest);
	}

	public static void main(String[] args) {
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setTotalAmount("0.01");
		model.setTimeoutExpress("30m");
		model.setSubject("商品的标题/交易标题/订单标题/订单关键");
		model.setOutTradeNo("70501111111S001111119");

		try {
			AlipayTradeAppPayResponse response = getResponse(model);
			if(response.isSuccess()){
				System.out.println("调用成功");
			} else {
				System.out.println("调用失败");
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
	}
	public static Map<String, String> toMap(HttpServletRequest request) {
		Map<String, String> params = new HashMap();
		Map<String, String[]> requestParams = request.getParameterMap();
		Iterator iter = requestParams.keySet().iterator();

		while(iter.hasNext()) {
			String name = (String)iter.next();
			String[] values = (String[])requestParams.get(name);
			String valueStr = "";
			for(int i = 0; i < values.length; ++i) {
				valueStr = i == values.length - 1 ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		return params;
	}
}
