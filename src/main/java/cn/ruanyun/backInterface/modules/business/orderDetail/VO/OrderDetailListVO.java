package cn.ruanyun.backInterface.modules.business.orderDetail.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.List;

/**
 * 子订单
 * @author wj
 */
@Data
public class OrderDetailListVO  {

    /**
     * 商品名称
     */
    private String goodName;

    /**
     * 商品图片
     */
    private String goodPics;

    /**
     * 商品新价格
     */
    private BigDecimal goodNewPrice;

    /**
     * 商品新价格
     */
    private String goodId;

    /**
     * 积分
     */
    private Integer integral;

    /**
     * 属性 字符串
     */
    private String attrSymbolPath;

    private List<String> itemAttrKeys;

    /**
     * 购买数量
     */
    private Integer buyCount;

    /**
     * 购买数量
     */
    private String discountMyId;

    /**
     * 满多少
     */
    private BigDecimal fullMoney;
    /**
     * 减多少
     */
    private BigDecimal subtractMoney;

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