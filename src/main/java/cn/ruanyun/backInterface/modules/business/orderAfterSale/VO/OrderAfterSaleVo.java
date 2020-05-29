package cn.ruanyun.backInterface.modules.business.orderAfterSale.VO;

import cn.ruanyun.backInterface.common.enums.AfterSaleTypeEnum;
import cn.ruanyun.backInterface.modules.business.orderAfterSale.pojo.OrderAfterSale;
import cn.ruanyun.backInterface.modules.business.orderDetail.pojo.OrderDetail;
import cn.ruanyun.backInterface.modules.business.orderDetail.vo.OrderDetailVo;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author root
 */
@Data
@Accessors(chain = true)
public class OrderAfterSaleVo {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("订单id")
    private String orderId;

    @ApiModelProperty("退款类别")
    private AfterSaleTypeEnum type;

    @ApiModelProperty("退货原因")
    private String returnReason;

    @ApiModelProperty("退货原因id")
    private String returnReasonId;

    @ApiModelProperty("退款金额")
    private BigDecimal refundMoney;

    @ApiModelProperty("实际退款金额")
    private BigDecimal actualRefundMoney = new BigDecimal(0);

    @ApiModelProperty("快递单号")
    private String expressCode;

    @ApiModelProperty("退款说明")
    private String expand;

    @ApiModelProperty("购买的商品信息")
    private OrderDetailVo orderDetails;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "下单时间")
    private Date createTime;

}
