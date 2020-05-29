package cn.ruanyun.backInterface.modules.business.orderDetail.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.BuyTypeEnum;
import cn.ruanyun.backInterface.common.enums.ShopCartTypeEnum;
import cn.ruanyun.backInterface.modules.business.discountMy.pojo.DiscountMy;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 子订单
 * @author wj
 */
@Data
@Entity
@Table(name = "t_order_detail")
@TableName("t_order_detail")
public class OrderDetail extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单id")
    private String orderId;

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

    @ApiModelProperty(value = "优惠券id")
    private String discountId;

    @ApiModelProperty(value = "满减金额")
    private BigDecimal subtractMoney = new BigDecimal(0);



}