package cn.ruanyun.backInterface.modules.business.order.DTO;


import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.enums.OrderTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * @program: xboot-plus
 * @description:
 * @author: fei
 * @create: 2020-02-12 18:14
 **/
@Data
@Accessors(chain = true)
public class OrderDTO {

    /**
     *商品信息json
     */
    private String goods;

    /**
     * 地址id
     */
    private String  addressId;

    /**
     * 优惠券id
     */
    private String discountCouponId;

}
