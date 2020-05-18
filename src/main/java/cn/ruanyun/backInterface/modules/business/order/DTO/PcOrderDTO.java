package cn.ruanyun.backInterface.modules.business.order.DTO;

import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
import lombok.Data;

@Data
public class PcOrderDTO {


    private String id;

    /**
     * 订单状态
     */
    private OrderStatusEnum orderStatus;
    /**
     * 用户类型
     */
    private String commonConstant;

    /**
     * 商家名称
     */
    private String shopName;

    /**
     * 商品名称
     */
    private String goodName;

    /**
     * 支付类型
     */
    private Integer payTypeEnum;

}
