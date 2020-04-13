package cn.ruanyun.backInterface.modules.business.good.VO;

import cn.ruanyun.backInterface.modules.business.discountMy.pojo.DiscountMy;
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


}
