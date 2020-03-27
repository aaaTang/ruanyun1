package cn.ruanyun.backInterface.modules.business.order.entity;


import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.enums.OrderTypeEnum;
import cn.ruanyun.backInterface.common.utils.CommonUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author fei
 */
@Data
@Entity
@Table(name = "t_order")
@TableName("t_order")
@ApiModel(value = "订单")
@Accessors(chain = true)
public class Order extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
    private String orderNum = CommonUtil.getRandomNum();

    /**
     * 商品id
     */
    private String goodId;

    /**
     * 尺寸id
     */
    private String sizeId;

    /**
     * 颜色id
     */
    private String colorId;

    /**
     * 购买数量
     */
    private Long count;

    /**
     * 订单类型
     */
    private OrderTypeEnum orderType;

    /**
     * 地址
     */
    private String addressId;


    /**
     * 购买总金额
     */
    private BigDecimal totalPrice;

    /**
     * 优惠券id
     */
    private String discountCouponId;

    /**
     * 快递单号
     */
    private String  courierNumber;

    /**
     * 订单状态
     */
    private OrderStatusEnum orderStatus = OrderStatusEnum.PRE_PAY;

}