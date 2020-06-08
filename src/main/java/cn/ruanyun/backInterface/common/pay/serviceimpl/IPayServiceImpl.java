package cn.ruanyun.backInterface.common.pay.serviceimpl;

import cn.hutool.core.util.RandomUtil;
import cn.ruanyun.backInterface.common.pay.common.alipay.AliPayUtilTool;
import cn.ruanyun.backInterface.common.pay.common.alipay.AlipayConfig;
import cn.ruanyun.backInterface.common.pay.common.wxpay.WeChatConfig;
import cn.ruanyun.backInterface.common.pay.dto.TransferDto;
import cn.ruanyun.backInterface.common.pay.model.PayModel;
import cn.ruanyun.backInterface.common.pay.service.IPayService;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.Participant;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.WxPayApiConfigKit;
import com.ijpay.wxpay.model.UnifiedOrderModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付接口实现
 *
 * @author Sangsang
 */
@Slf4j
@Service
@Transactional
public class IPayServiceImpl implements IPayService {


	@Override
	public Result<Object> aliPayMethod(PayModel payModel) {
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();

		model.setBody(AlipayConfig.BODY);
		model.setSubject(AlipayConfig.SUBJECT);
		model.setOutTradeNo(payModel.getOrderNums());
		model.setTimeoutExpress("15m");
		model.setTotalAmount("0.01");

		try {

			AlipayTradeAppPayResponse response = AliPayUtilTool.getResponse(model);
			if (response.isSuccess()) {

				Map<String, Object> map = new HashMap<>();
				map.put("paySign", response.getBody());
				return new ResultUtil<>().setData(map);
			} else {

				return new ResultUtil<>().setErrorMsg("fail");
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return new ResultUtil<>().setErrorMsg("fail");
	}

	@Override
	public String aliPayTransfer(TransferDto transferDto) throws AlipayApiException {

		AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel ();

		//1.封装实体
		model.setOutBizNo(transferDto.getOrderNo());
		model.setBizScene("DIRECT_TRANSFER");
		model.setTransAmount(/*transferDto.getAmount().toString()*/ "0.01");
		model.setProductCode("TRANS_ACCOUNT_NO_PWD");

		//2.付款方账户
		Participant payer = new Participant();

		//标识类型，ALIPAY_USER_ID：支付宝的会员ID ALIPAY_LOGON_ID：支付宝登录号，邮箱、手机等
		payer.setIdentityType("ALIPAY_LOGON_ID");

		//支付宝账号
		payer.setIdentity("448963818@qq.com");

		payer.setName("宁波梵莎芙珠宝有限公司");

		model.setPayerInfo(payer);

		//3.收款方账户
		Participant payee=new Participant();

		//标识类型，ALIPAY_USER_ID：支付宝的会员ID ALIPAY_LOGON_ID：支付宝登录号，邮箱、手机等
		payee.setIdentityType("ALIPAY_LOGON_ID");

		//支付宝账号
		payee.setIdentity("m13865911804@163.com");

		//标识类型为ALIPAY_LOGON_ID需设置name
		payee.setName("于小飞");

		model.setPayeeInfo(payee);


		AlipayFundTransUniTransferResponse response=AliPayUtilTool.getTransferResponse(model);

		log.info("当前支付宝的状态是：" + response.getStatus() + response.getOrderId() + response.getOutBizNo() + response.getPayFundOrderId());
		return response.getBody();

	}

	/**
	 * 微信支付
	 *
	 * @param payModel 支付模型
	 * @return Result<Object>
	 */
	@Override
	public Result<Object> wxPayMethod(PayModel payModel) {


		WxPayApiConfig wxPayApiConfig = WeChatConfig.getWxPayApiConfig();

		payModel.setTotalPrice(new BigDecimal("0.01"));

		log.info("当前的支付钱数是:" + ToolUtil.getMoney(payModel.getTotalPrice().toString()));

		Map<String, String> params = UnifiedOrderModel
				.builder()

				//"应用ID"
				.appid(WeChatConfig.APP_ID)

				//"商户号"
				.mch_id(WeChatConfig.MCH_ID)

				//"随机字符串"
				.nonce_str(RandomUtil.randomNumbers(10))

				.body("商品支付")
				.attach("附加数据")

				//"商户订单号"
				.out_trade_no(payModel.getOrderNums())

				//“价格”
				.total_fee(ToolUtil.getMoney(payModel.getTotalPrice().toString()))
				.notify_url(WeChatConfig.NOTIFY_URL)

				//"APP = 交易方式"
				.trade_type(WeChatConfig.TRADE_TYPE)
				.build()

				//"商户密钥"
				.createSign(WeChatConfig.KEY, SignType.HMACSHA256);

		String xmlResult = WxPayApi.pushOrder(false, params);

		log.info("当前xml回调是" + xmlResult);
		Map<String, String> result = WxPayKit.xmlToMap(xmlResult);

		String returnCode = result.get("return_code");
		String returnMsg = result.get("return_msg");
		if (!WxPayKit.codeIsOk(returnCode)) {
			return new ResultUtil<>().setErrorMsg("调取失败");
		}
		String resultCode = result.get("result_code");
		String errCodeDes = result.get("err_code_des");
		if (!WxPayKit.codeIsOk(resultCode)) {
			return new ResultUtil<>().setErrorMsg(errCodeDes);
		}
		// 以下字段在 return_code 和 result_code 都为 SUCCESS 的时候有返回
		String prepayId = result.get("prepay_id");

		Map<String, String> packageParams = WxPayKit.appPrepayIdCreateSign(wxPayApiConfig.getAppId(), wxPayApiConfig.getMchId(), prepayId,
				wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);
		Map<String, Object> map = new HashMap<>();
		map.put("wxPaySign", packageParams);
		return new ResultUtil<>().setData(map, "successful" + returnMsg);

	}
}