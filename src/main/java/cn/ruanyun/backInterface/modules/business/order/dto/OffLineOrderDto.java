package cn.ruanyun.backInterface.modules.business.order.dto;

import cn.ruanyun.backInterface.common.enums.OrderTypeEnum;
import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
import cn.ruanyun.backInterface.common.enums.RentTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author Administrator
 */
@Data
@Accessors(chain = true)
public class OffLineOrderDto {


    @ApiModelProperty(value = "销售员工id")
    private String staffId;

    @ApiModelProperty(value = "商品描述")
    private String goodDesc;

    @ApiModelProperty(value = "总价格")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "定金")
    private BigDecimal goodDeposit;

    @ApiModelProperty(value = "支付类型")
    private PayTypeEnum payTypeEnum;

    @ApiModelProperty("尾款支付类型")
    private RentTypeEnum rentTypeEnum;

    @ApiModelProperty("订单编号")
    private String orderNum;

    @ApiModelProperty(value = "支付密码")
    private String payPassword;


}
