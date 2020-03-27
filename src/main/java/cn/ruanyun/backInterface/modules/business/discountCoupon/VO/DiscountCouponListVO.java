package cn.ruanyun.backInterface.modules.business.discountCoupon.VO;

import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
import cn.ruanyun.backInterface.common.enums.DisCouponTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DiscountCouponListVO {


    private String id;
    /**
     * 名称
     */
    private String title;

    /**
     * 优惠券类型
     */
    private DisCouponTypeEnum disCouponType;

    /**
     * 商品id
     */
    private String goodsPackageId;

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
    private LocalDateTime validityPeriod;

    /**
     * 是否过期
     */
    private BooleanTypeEnum pastDue = BooleanTypeEnum.NO;
}
