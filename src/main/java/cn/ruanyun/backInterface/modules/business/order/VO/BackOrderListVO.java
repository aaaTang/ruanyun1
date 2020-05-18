package cn.ruanyun.backInterface.modules.business.order.VO;


import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.enums.OrderTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: xboot-plus
 * @description: 后台管理订单列表
 * @author: fei
 * @create: 2020-02-13 10:18
 **/
@Data
@Accessors(chain = true)
public class BackOrderListVO extends OrderListVO{

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 收获手机号
     */
    private String phone;

    /**
     * 收获地址
     */
    private String address;

    /**
     * 订单类型
     */
    private OrderTypeEnum orderType;

    /**
     * 买家留言
     */
    private String buyerMessage;

    /**
     * 订单状态
     */
    private String orderStatus;


    /**
     * 商品描述
     */
    private String goodDesc;
}
