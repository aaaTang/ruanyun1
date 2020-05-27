package cn.ruanyun.backInterface.modules.business.shoppingCart.VO;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
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

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品id
     */
    private String goodsId;

    /**
     * 商品图片
     */
    private String pic;

    /**
     * 属性信息
     */
    private List<String> itemAttrKeys;

    /**
     * 购买数量
     */
    private Long count;

    /**
     * 购买总金额
     */
    private BigDecimal totalPrice = new BigDecimal(0);

    /**
     * 商品单价
     */
    private BigDecimal goodPrice = new BigDecimal(0);
    /**
     * 商品库存
     */
    private Integer inventory;

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
    private Integer leaseState = CommonConstant.STATUS_NORMAL;
}
