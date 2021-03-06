package cn.ruanyun.backInterface.modules.business.orderDetail.vo;

import cn.ruanyun.backInterface.common.enums.*;
import cn.ruanyun.backInterface.modules.business.discountCoupon.VO.DiscountCouponListVO;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.vo.ItemAttrValVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-05-28 11:42
 **/
@Data
@Accessors(chain = true)
public class OrderDetailVo {

    @ApiModelProperty(value = "订单详情id")
    private String id;

    @ApiModelProperty(value = "商家id")
    private String userId;

    @ApiModelProperty(value = "商品id")
    private String goodId;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "商品图片")
    private String pic;

    @ApiModelProperty(value = "属性")
    private List<ItemAttrValVo> attrSymbolPath;

    @ApiModelProperty(value = "购买数量")
    private Integer buyCount;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "定金")
    private BigDecimal goodDeposit;

    @ApiModelProperty(value = "尾款")
    private BigDecimal goodBalancePayment;

    @ApiModelProperty(value = "购买类型")
    private BuyTypeEnum buyType;

    @ApiModelProperty(value = "商品类型")
    private ShopCartTypeEnum shopCartType;

    @ApiModelProperty(value = "满减金额")
    private BigDecimal subtractMoney = new BigDecimal(0);

    /**************************档期*****************************/

    @ApiModelProperty(value = "订单类型")
    private OrderTypeEnum typeEnum;

    @ApiModelProperty(value = "场地id")
    private String siteId;

    @ApiModelProperty("上午&下午")
    private DayTimeTypeEnum dayTimeType;

    @ApiModelProperty("预约档期")
    private String scheduleAppointment;

    @ApiModelProperty(value = "支付尾款类型")
    private RentTypeEnum rentType;

    @ApiModelProperty(value = "商品描述")
    private String goodDesc;

}
