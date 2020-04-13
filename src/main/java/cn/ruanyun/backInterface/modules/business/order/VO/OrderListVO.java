package cn.ruanyun.backInterface.modules.business.order.VO;

import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: xboot-plus
 * @description:
 * @author: fei
 * @create: 2020-02-12 18:05
 **/
@Data
@Accessors(chain = true)
public class OrderListVO {

    private String id;

    /**
     * 订单编号
     */
    private String orderNum;

    /*
      商品名称
    */
    private String goodName;

    /*
    商品图片（，隔开）
     */
    private String goodPics;

    /**
     * 颜色名称
     */
    private List<String> attrSymbolPath;

    /**
     * 商品新价格
     */
    private BigDecimal goodNewPrice;

    /**
     * 购买数量
     */
    private Long buyCount;

    /**
     * 购买总金额
     */
    private BigDecimal totalPrice;

    /**
     * 订单状态
     */
    private String orderStatus ;

    private int orderStatusInt ;
}
