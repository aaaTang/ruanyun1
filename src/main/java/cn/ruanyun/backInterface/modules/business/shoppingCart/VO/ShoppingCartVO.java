package cn.ruanyun.backInterface.modules.business.shoppingCart.VO;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.BuyTypeEnum;
import cn.ruanyun.backInterface.common.enums.ShopCartTypeEnum;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.vo.ItemAttrValVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: ruanyun-plus
 * @description:
 * @author: fei
 * @create: 2020-02-12 19:14
 **/
@Data
@Accessors(chain = true)
public class ShoppingCartVO {

    private String id;

    @ApiModelProperty(value = "商品id")
    private String goodId;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "商品图片")
    private String pic;

    @ApiModelProperty(value = "属性信息")
    private List<ItemAttrValVo> itemAttrKeys;

    @ApiModelProperty(value = "购买数量")
    private Integer buyCount;

    @ApiModelProperty(value = "定金")
    private BigDecimal goodDeposit;

    @ApiModelProperty(value = "尾款")
    private BigDecimal goodBalancePayment;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "购买类型")
    private BuyTypeEnum buyType;

    @ApiModelProperty(value = "租赁状态 1尾款线上支付  2尾款线下支付 ")
    private Integer leaseState = CommonConstant.STATUS_NORMAL;

    @ApiModelProperty(value = "购物车商品类型")
    private ShopCartTypeEnum shopCartType;

}
