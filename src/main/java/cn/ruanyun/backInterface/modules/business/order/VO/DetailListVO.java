package cn.ruanyun.backInterface.modules.business.order.VO;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;

import java.math.BigDecimal;

@Data
public class DetailListVO {

    private String orderId;

    @ApiModelProperty(value = "商品id")
    private String goodId;

    @ApiModelProperty(value = "创建者")
    @CreatedBy
    private String createBy;
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
     * 积分
     */
    private Integer integral;

    /**
     * 属性
     */
    private String attrSymbolPath;

    /**
     * 购买数量
     */
    private Integer buyCount;

    /**
     *
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

    @ApiModelProperty(value = "购买状态 1购买 2租赁 3购买和租赁")
    private Integer buyState;

    @ApiModelProperty(value = "租赁状态 1尾款线上支付  2尾款线下支付 ")
    private Integer leaseState = CommonConstant.STATUS_NORMAL;
}
