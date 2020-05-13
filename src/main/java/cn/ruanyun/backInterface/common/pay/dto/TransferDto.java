package cn.ruanyun.backInterface.common.pay.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author Administrator
 */
@Data
@Accessors(chain = true)
public class TransferDto {


    /**
     * 订单编号
     */
    private String orderNo;


    /**
     * 转账金额
     */
    private BigDecimal amount;


    /**
     * 支付宝账户
     */
    private String aliPayAcount;

}
