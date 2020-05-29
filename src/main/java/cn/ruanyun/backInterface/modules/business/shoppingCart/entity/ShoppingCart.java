package cn.ruanyun.backInterface.modules.business.shoppingCart.entity;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.BuyTypeEnum;
import cn.ruanyun.backInterface.common.enums.ShopCartTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author fei
 */
@Data
@Entity
@Table(name = "t_shopping_cart")
@TableName("t_shopping_cart")
@ApiModel(value = "购物车")
@Accessors(chain = true)
public class ShoppingCart extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

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