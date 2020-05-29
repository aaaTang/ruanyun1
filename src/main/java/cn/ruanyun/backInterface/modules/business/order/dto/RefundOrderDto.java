package cn.ruanyun.backInterface.modules.business.order.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-05-27 19:58
 **/
@Data
@Accessors(chain = true)
public class RefundOrderDto {


    private String id;

    @ApiModelProperty(value = "实际退款金额")
    private BigDecimal actualRefundMoney;
}
