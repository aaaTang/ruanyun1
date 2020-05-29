package cn.ruanyun.backInterface.modules.business.order.dto;

import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
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

    @ApiModelProperty(value = "支付类型")
    private PayTypeEnum payTypeEnum;
}
