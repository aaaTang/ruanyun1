package cn.ruanyun.backInterface.modules.business.order.DTO;


import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.enums.OrderTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: xboot-plus
 * @description:
 * @author: fei
 * @create: 2020-02-12 18:14
 **/
@Data
@Accessors(chain = true)
public class OrderDTO {


    private String id;

    /**
     * 是否是后台
     */
    private Boolean back;

    /**
     * 快递单号
     */
    private String  courierNumber;

    /**
     * 订单类型
     */
    private OrderTypeEnum orderType;

    /**
     * 订单状态
     */
    private OrderStatusEnum orderStatus;
}
