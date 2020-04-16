package cn.ruanyun.backInterface.common.pay.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PayModel {

	/**
	 * 订单编号 可能是多个，逗号隔开
	 */
	private String orderNums;

	/**
	 * 支付金额
	 */
	private BigDecimal totalPrice;

}
