package cn.ruanyun.backInterface.modules.business.order.VO;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class MyOrderVO {

    /**
     * 订单id
     */
    private String id;
    /**
     * 订单编号
     */
    private String orderNum;

    /**
     * 支付资金
     */
    private BigDecimal payMoney;

    /**
     * 订单状态
     */
    private String orderStatus;

    private Object orderDetails;

    private String createTime;

}
