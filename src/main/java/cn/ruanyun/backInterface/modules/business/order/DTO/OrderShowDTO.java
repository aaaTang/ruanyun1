package cn.ruanyun.backInterface.modules.business.order.DTO;


import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.enums.OrderTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @program: xboot-plus
 * @description:
 * @author: fei
 * @create: 2020-02-12 18:14
 **/
@Data
@Accessors(chain = true)
public class OrderShowDTO {


    /*1购物车直接下单
    * 2直接购买
    * 3直接购买套餐商品*/
    private int type;

    private String goodId;

    /**
     * 属性
     */
    private String attrSymbolPath;


    private Integer count;

    /**
     * 购物车ids
     */
    private String shoppingCartIds;

    /**
     * 地址id
     */
    private String  addressId;

    /**
     * 优惠券id
     */
    private String discountCouponId;




}
