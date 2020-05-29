package cn.ruanyun.backInterface.modules.business.order.dto;

import cn.ruanyun.backInterface.common.enums.BuyTypeEnum;
import cn.ruanyun.backInterface.common.enums.ShopCartTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-05-28 13:22
 **/

@Data
@Accessors(chain = true)
public class AppOrderGoodInfoDto {


    @ApiModelProperty(value = "商品id")
    private String goodId;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "商品图片")
    private String pic;

    @ApiModelProperty(value = "属性")
    private String attrSymbolPath;

    @ApiModelProperty(value = "购买数量")
    private Integer buyCount;

    @ApiModelProperty(value = "优惠券id")
    private String discountId;

    @ApiModelProperty(value = "定金")
    private BigDecimal goodDeposit;

    @ApiModelProperty(value = "尾款")
    private BigDecimal goodBalancePayment;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "购买类型")
    private BuyTypeEnum buyType;

    @ApiModelProperty(value = "购物车商品类型")
    private ShopCartTypeEnum shopCartType;
}
