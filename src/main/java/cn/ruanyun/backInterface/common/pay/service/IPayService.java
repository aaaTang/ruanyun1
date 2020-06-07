package cn.ruanyun.backInterface.common.pay.service;


import cn.ruanyun.backInterface.common.pay.dto.TransferDto;
import cn.ruanyun.backInterface.common.pay.model.PayModel;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;
import com.alipay.api.AlipayApiException;

import javax.servlet.http.HttpServletRequest;

/**
 * 支付接口
 * @author Sangsang
 */
public interface IPayService {

	/**
	 * 支付宝支付
	 * @param payModel payModel
	 * @return Object
	 */
	Result<Object> aliPayMethod(PayModel payModel);

	/**
	 * 支付宝转账
	 * @param transferDto transferDto
	 * @return String
	 */
	String aliPayTransfer(TransferDto transferDto) throws AlipayApiException;

	/**
	 * 微信支付
	 * @param payModel payModel
	 * @return Object
	 */
	Result<Object> wxPayMethod(PayModel payModel) ;
}