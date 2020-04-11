package cn.ruanyun.backInterface.modules.business.shoppingCart.VO;

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

}
