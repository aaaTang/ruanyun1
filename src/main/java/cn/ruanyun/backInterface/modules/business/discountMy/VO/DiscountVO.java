package cn.ruanyun.backInterface.modules.business.discountMy.VO;

import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
import cn.ruanyun.backInterface.common.enums.DisCouponTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class DiscountVO {

    /**
     * 优惠券id
     */
    private String id;

    /**
     * 优惠券标题
     */
    private String title;

    /**
     * 优惠券类型  1全场满减 2商品满减
     */
    private DisCouponTypeEnum disCouponType;

    /**
     * 满多少
     */
    private BigDecimal fullMoney;
    /**
     * 减多少
     */
    private BigDecimal subtractMoney;
    /**
     * 有效期
     */
    private String validityPeriod;

    private Integer status;

}
