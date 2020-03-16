package cn.ruanyun.backInterface.modules.business.discountCoupon.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
import cn.ruanyun.backInterface.common.enums.DisCouponTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券
 * @author fei
 */
@Data
@Entity
@Table(name = "t_discount_coupon")
@TableName("t_discount_coupon")
public class DiscountCoupon extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


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