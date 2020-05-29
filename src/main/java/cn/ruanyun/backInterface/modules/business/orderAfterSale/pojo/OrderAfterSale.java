package cn.ruanyun.backInterface.modules.business.orderAfterSale.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.AfterSaleStatusEnum;
import cn.ruanyun.backInterface.common.enums.AfterSaleTypeEnum;
import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.modules.business.orderDetail.pojo.OrderDetail;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
public class OrderAfterSale extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("订单id")
    private String orderId;

    @ApiModelProperty("订单状态")
    private OrderStatusEnum orderStatus;

    @ApiModelProperty("退款类别")
    private AfterSaleTypeEnum type;

    @ApiModelProperty("退货原因id")
    private String returnReasonId;

    @ApiModelProperty("退货原因")
    private String returnReason;

    @ApiModelProperty("退款金额")
    private BigDecimal refundMoney;

    @ApiModelProperty("实际退款金额")
    private BigDecimal actualRefundMoney = new BigDecimal(0);

    @ApiModelProperty("快递单号")
    private String expressCode;

    @ApiModelProperty("退款说明")
    private String expand;

}