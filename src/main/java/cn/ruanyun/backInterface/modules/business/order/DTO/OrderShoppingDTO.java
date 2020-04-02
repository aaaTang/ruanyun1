package cn.ruanyun.backInterface.modules.business.order.DTO;


import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.enums.OrderTypeEnum;
import cn.ruanyun.backInterface.modules.business.shoppingCart.entity.ShoppingCart;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: xboot-plus
 * @description:
 * @author: fei
 * @create: 2020-02-12 18:14
 **/
@Data
@Accessors(chain = true)
public class OrderShoppingDTO {


    //购物车ids
    private String shoppingCartOids;

    /**
     * 收获地址id
     */
    private String addressId;


    /**
     * 优惠券id
     */
    private String discountCouponId;

}
