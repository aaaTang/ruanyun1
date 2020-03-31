package cn.ruanyun.backInterface.modules.business.order.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 订单
 * @author fei
 */
@Data
@Entity
@Table(name = "t_order")
@TableName("t_order")
public class Order extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 商品id
     */
    private String goodId;


    /**
     * 颜色id
     */
    private String colorId;


    /**
     * 规格id
     */
    private String sizeId;


    /**
     * 数量
     */
    private Integer count;


    /**
     * 总价格
     */
    private BigDecimal totalPrice;


    /**
     * 收获地址id
     */
    private String addressId;


    /**
     * 优惠券id
     */
    private String discountCouponId;


    /**
     * 订单号
     */
    private String orderNum;


    /**
     * 订单状态
     */
    private OrderStatusEnum orderStatus = OrderStatusEnum.PRE_PAY;


    /**
     * 支付类型
     */
    private PayTypeEnum payTypeEnum;

}