package cn.ruanyun.backInterface.common.pay.common.alipay;

import cn.hutool.core.map.MapUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayObject;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
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
	 * 支付宝支付
	 * @Description TD: 传入model参数，获取阿里支付的响应参数(APP支付)
	 * @param model
	 * @Return com.alipay.api.response.AlipayTradeAppPayResponse
	 * @Author sangsang
	 * @Date 2020/2/9 11:31
	 **/
	public static AlipayTradeAppPayResponse getResponse(AlipayTradeAppPayModel model) throws AlipayApiException {
		AlipayTradeAppPayRequest alipayRequest = new AlipayTradeAppPayRequest();
		alipayRequest.setNotifyUrl(AlipayConfig.NOTIFY_URL);
		alipayRequest.setBizModel(model);
		AlipayClient  alipayClient = new DefaultAlipayClient(AlipayConfig.GATEWAY_URL, AlipayConfig.APP_ID, AlipayConfig.MERCHANT_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGN_TYPE); ;
		return alipayClient.sdkExecute(alipayRequest);
	}

	/**
	 *支付宝转账
	 * @param model 实体
	 * @return AlipayFundTransUniTransferResponse
	 * @throws AlipayApiException 异常
	 */
	public static AlipayFundTransUniTransferResponse getTransferResponse(AlipayFundTransUniTransferModel model) throws AlipayApiException {
		AlipayFundTransUniTransferRequest request=new AlipayFundTransUniTransferRequest();
		request.setBizModel(model);
		AlipayClient  alipayClient = new DefaultAlipayClient(AlipayConfig.GATEWAY_URL,
				AlipayConfig.APP_ID, AlipayConfig.MERCHANT_PRIVATE_KEY, AlipayConfig.FORMAT,
				AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGN_TYPE); ;
		return alipayClient.sdkExecute(request);
	}


	public static void main(String[] args) throws AlipayApiException {
//		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
//		model.setTotalAmount("0.01");
//		model.setTimeoutExpress("30m");
//		model.setSubject("商品的标题/交易标题/订单标题/订单关键");
//		model.setOutTradeNo("70501111111S001111119");
//
//		try {
//			AlipayTradeAppPayResponse response = getResponse(model);
//			if(response.isSuccess()){
//				System.out.println("调用成功");
//			} else {
//				System.out.println("调用失败");
//			}
//		} catch (AlipayApiException e) {
//			e.printStackTrace();
//		}



//		request.setBizContent("{" +
//				"\"out_biz_no\":\"201806300001\"," +
//				"\"trans_amount\":23.00," +
//				"\"product_code\":\"TRANS_ACCOUNT_NO_PWD\"," +
//				"\"biz_scene\":\"DIRECT_TRANSFER\"," +
//				"\"order_title\":\"转账标题\"," +
//				"\"original_order_id\":\"20190620110075000006640000063056\"," +
//				"\"payee_info\":{" +
//				"\"identity\":\"208812*****41234\"," +
//				"\"identity_type\":\"ALIPAY_USER_ID\"," +
//				"\"name\":\"黄龙国际有限公司\"" +
//				"    }," +
//				"\"remark\":\"单笔转账\"," +
//				"\"business_params\":\"{\\\"sub_biz_scene\\\":\\\"REDPACKET\\\"}\"" +
//				"  }");

//		AlipayClient  alipayClient = new DefaultAlipayClient(AlipayConfig.GATEWAY_URL, AlipayConfig.APP_ID, AlipayConfig.MERCHANT_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGN_TYPE);
//
//		AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
//
//		AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
//		model.setOutBizNo("2002302302910329");
//		model.setPayeeType("ALIPAY_LOGONID");
//		model.setPayeeAccount("15240197822");
//		model.setAmount("0.1");
//		model.setPayerShowName("智商税");
//		model.setPayeeRealName("帅气阿桑");
//		model.setRemark("remark备注");
//
//		request.setBizModel(model);
//		try {
//			AlipayFundTransUniTransferResponse response = alipayClient.certificateExecute(request);
//			if(response.isSuccess()){
//				System.out.println(response.getBody());
//
//			} else {
//				System.out.println("调用失败");
//			}
//		} catch (AlipayApiException e) {
//			e.printStackTrace();
//		}

		AlipayClient  alipayClient = new DefaultAlipayClient(AlipayConfig.GATEWAY_URL, AlipayConfig.APP_ID, AlipayConfig.MERCHANT_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGN_TYPE);
//		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do","app_id","your private_key","json","GBK","alipay_public_key","RSA2");

		AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
		request.setBizContent("{" +
				"\"out_biz_no\":\"201806300001\"," +
				"\"trans_amount\":0.01," +
				"\"product_code\":\"TRANS_ACCOUNT_NO_PWD\"," +
				"\"biz_scene\":\"DIRECT_TRANSFER\"," +
				"\"order_title\":\"转账标题\"," +
				"\"original_order_id\":\"20190620110075000006640000063056\"," +
				"\"payee_info\":{" +
				"\"identity\":\"15240197822\"," +
				"\"identity_type\":\"ALIPAY_LOGON_ID\"," +
				"\"name\":\"阿giao\"" +
				"    }," +
				"\"remark\":\"单笔转账\"," +
				"\"business_params\":\"{\\\"sub_biz_scene\\\":\\\"REDPACKET\\\"}\"" +
				"  }");
		AlipayFundTransUniTransferResponse response = alipayClient.execute(request);
		if(response.isSuccess()){
			System.out.println("调用成功");
		} else {
			System.out.println("调用失败");
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
