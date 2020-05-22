package cn.ruanyun.backInterface.modules.business.good.VO;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.modules.business.discountMy.pojo.DiscountMy;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-03-27 23:22
 **/
@Data
@Accessors(chain = true)
public class AppGoodOrderVO {

    private String goodId;


    /**
     * 商品名称
     */
    private String goodName;


    /**
     * 商品图片
     */
    private String goodPic;


    /**
     * 属性
     */
    private String attrSymbolPath;


    /**
     * 颜色
     */
    private List<String> itemAttrKeys;

    /**
     * 数量
     */
    private Integer buyCount;

    /**
     * 商品新价格
     */
    private BigDecimal goodNewPrice= new BigDecimal(0);

    /**
     * 积分
     */
    private Integer integral;

    /**
     * 我的优惠券id
     */
    private String discountMyId;

    /**
     * 满多少
     */
    private BigDecimal fullMoney = new BigDecimal(0);

    /**
     * 减多少
     */
    private BigDecimal subtractMoney= new BigDecimal(0);

    /**
     * 运费
     */
    private BigDecimal freightMoney = new BigDecimal(0);

    @ApiModelProperty(value = "购买状态 1购买 2租赁 3购买和租赁")
    private Integer buyState;

    @ApiModelProperty(value = "租赁状态 1尾款线上支付  2尾款线下支付 ")
    private Integer leaseState;

}
