package cn.ruanyun.backInterface.common.pay.service;


import cn.ruanyun.backInterface.common.pay.model.PayModel;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;

import javax.servlet.http.HttpServletRequest;

/**
 * 支付接口
 * @author Sangsang
 */
public interface IPayService {

	/**
	 * 支付宝支付
	 * @param payModel
	 * @return
	 */
	Result<Object> aliPayMethod(PayModel payModel);


	/**
	 * 微信支付
	 * @param payModel
	 * @return
	 */
	Result<Object> wxPayMethod(PayModel payModel) ;


	/**
	 * 余额支付
	 * @param payModel
	 * @return
	 */
	Result<Object> accountMoney(PayModel payModel);


	/**
	 * 微信退款
	 * @param request
	 * @return
	 */
	Result<Object> wxRefundNotify(HttpServletRequest request);


	/**
	 * 支付宝退款
	 * @param order
	 * @return
	 */
	Result<Object> aliRefundNotify(Order order);

}