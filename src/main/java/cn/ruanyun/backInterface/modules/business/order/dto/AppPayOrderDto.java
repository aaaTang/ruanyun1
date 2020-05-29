package cn.ruanyun.backInterface.modules.business.order.dto;

import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-05-28 14:22
 **/
@Data
@Accessors(chain = true)
public class AppPayOrderDto {


    @ApiModelProperty(value = "订单ids")
    private String ids;

    @ApiModelProperty(value = "支付类型")
    private PayTypeEnum payType;

    @ApiModelProperty(value = "支付密码")
    private String payPassword;
}
