package cn.ruanyun.backInterface.modules.business.order.dto;

import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-05-28 17:15
 **/

@Data
@Accessors(chain = true)
public class OrderOperateDto {


    @ApiModelProperty(value = "订单id")
    private String orderId;

    @ApiModelProperty(value = "快递单号")
    private String expressCode;

    @ApiModelProperty(value = "支付方式")
    private PayTypeEnum payType;

    @ApiModelProperty(value = "支付密码")
    private String payPassword;
}
