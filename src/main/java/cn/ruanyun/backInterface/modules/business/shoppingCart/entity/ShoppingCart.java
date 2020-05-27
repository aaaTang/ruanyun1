package cn.ruanyun.backInterface.modules.business.shoppingCart.entity;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
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

    /**
     * 商品id
     */
    private String goodId;

    /**
     * 规格和规格属性id
     */
    private String attrSymbolPath;

    /**
     * 购买数量
     */
    private Integer count;

    /**
     * 优惠券id
     */
    private String discountCouponId;

    /**
     * 商品新价格
     */
    private BigDecimal goodNewPrice;

    /**
     * 总价格
     */
    private BigDecimal totalPrice = new BigDecimal(0);


    /**
     *  商品定金
     */
    private BigDecimal goodDeposit;

    /**
     *  商品尾款
     */
    private BigDecimal goodDalancePayment;

    @ApiModelProperty(value = "购买状态 1购买 2租赁 3购买和租赁")
    private Integer buyState;

    @ApiModelProperty(value = "租赁状态 1尾款线上支付  2尾款线下支付 ")
    private Integer leaseState;
}