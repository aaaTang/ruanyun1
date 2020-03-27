package cn.ruanyun.backInterface.modules.business.shoppingCart.VO;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

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
     * 颜色名称
     */
    private String colorName;


    /**
     * 尺寸名称
     */
    private String sizeName;


    /**
     * 购买数量
     */
    private Long count;


    /**
     * 购买总金额
     */
    private BigDecimal totalPrice;

}
