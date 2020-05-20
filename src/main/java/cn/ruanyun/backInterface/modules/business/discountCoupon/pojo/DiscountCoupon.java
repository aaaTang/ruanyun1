package cn.ruanyun.backInterface.modules.business.discountCoupon.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
import cn.ruanyun.backInterface.common.enums.DisCouponTypeEnum;
import cn.ruanyun.backInterface.modules.business.storeAudit.pojo.StoreAudit;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

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
     * 优惠券类型 ALL_SHOP全场满减 ALL_USE指定商家满减  ONE_PRODUCT商品满减
     */
    private DisCouponTypeEnum disCouponType;

    /**
     * 商家id
     */
    private String storeAuditOid;
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
    private Date validityPeriod;

    /**
     * 是否过期
     */
    private BooleanTypeEnum pastDue = BooleanTypeEnum.NO;
}