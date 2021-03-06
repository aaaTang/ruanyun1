package cn.ruanyun.backInterface.modules.business.order.dto;


import cn.ruanyun.backInterface.common.enums.DayTimeTypeEnum;
import cn.ruanyun.backInterface.common.enums.OrderTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: xboot-plus
 * @description:
 * @author: fei
 * @create: 2020-02-12 18:14
 **/
@Data
@Accessors(chain = true)
public class OrderDto {

    @ApiModelProperty(value = "商品信息json串")
    private String goodStr;

    @ApiModelProperty(value = "地址id")
    private String  addressId;

    @ApiModelProperty(value = "场地id")
    private String siteId;

    @ApiModelProperty("上午&下午")
    private DayTimeTypeEnum dayTimeType;

    @ApiModelProperty("预约档期")
    private String scheduleAppointment;

    @ApiModelProperty(value = "订单类型")
    private OrderTypeEnum typeEnum;

}
