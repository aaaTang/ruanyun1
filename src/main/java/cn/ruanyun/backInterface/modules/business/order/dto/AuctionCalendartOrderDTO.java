package cn.ruanyun.backInterface.modules.business.order.dto;

import cn.ruanyun.backInterface.common.enums.DayTimeTypeEnum;
import cn.ruanyun.backInterface.common.enums.OrderTypeEnum;
import cn.ruanyun.backInterface.common.enums.ShopCartTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AuctionCalendartOrderDTO {



    @ApiModelProperty(value = "场地id")
    private String siteId;

    @ApiModelProperty(value = "商品或者套餐id")
    private String goodId;

    @ApiModelProperty("上午&下午")
    private DayTimeTypeEnum dayTimeType;

    @ApiModelProperty("预约档期")
    private String scheduleAppointment;

    @ApiModelProperty(value = "订单类型")
    private OrderTypeEnum typeEnum;

    @ApiModelProperty(value = "购物车商品类型")
    @NotNull
    private ShopCartTypeEnum shopCartType;

    @ApiModelProperty(value = "优惠券")
    private String discountId;


}
