package cn.ruanyun.backInterface.modules.business.orderAfterSale.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.AfterSaleStatusEnum;
import cn.ruanyun.backInterface.common.enums.AfterSaleTypeEnum;
import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.modules.business.orderDetail.pojo.OrderDetail;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 售后申请
 * @author wj
 */
@Data
@Entity
@Table(name = "t_order_after_sale")
@TableName("t_order_after_sale")
public class OrderAfterSale extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 订单di
     */
    private String orderId;

    /**
     * 订单di
     */
    private OrderStatusEnum orderStatus;

    /**
     * 退款类别
     */
    private AfterSaleTypeEnum type;


    /**
     * 退货原因id
     */
    private String returnReasonId;

    /**
     * 退货原因
     */
    private String returnReason;

    /**
     * 退款金额
     */
    private BigDecimal totalPrice;


    /**
     * 快递单号
     */
    private String expressCode;

    /**
     * 退款说明
     */
    private String expand;


    /**
     * 退款状态
     */
    private AfterSaleStatusEnum status = AfterSaleStatusEnum.APPLY;

}