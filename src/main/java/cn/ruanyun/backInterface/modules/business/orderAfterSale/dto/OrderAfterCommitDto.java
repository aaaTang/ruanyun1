package cn.ruanyun.backInterface.modules.business.orderAfterSale.dto;

import cn.ruanyun.backInterface.common.enums.AfterSaleTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-05-29 18:46
 **/

@Data
@Accessors(chain = true)
public class OrderAfterCommitDto {


    private String id;

    @ApiModelProperty("订单id")
    private String orderId;

    @ApiModelProperty("退款类别")
    private AfterSaleTypeEnum type;

    @ApiModelProperty("退货原因id")
    private String returnReasonId;

    @ApiModelProperty("快递单号")
    private String expressCode;

    @ApiModelProperty("退款说明")
    private String expand;

}
