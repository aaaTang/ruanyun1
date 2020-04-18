package cn.ruanyun.backInterface.modules.business.orderDetail.VO;

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

}