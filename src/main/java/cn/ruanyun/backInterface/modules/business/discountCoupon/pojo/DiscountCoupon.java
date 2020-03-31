package cn.ruanyun.backInterface.modules.business.discountCoupon.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.DisCouponTypeEnum;
import cn.ruanyun.backInterface.common.enums.DisCouponUseTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

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
     * 指定商品id
     */
    private String goodId;


    /**
     * 标题
     */
    private String title;


    /**
     * 满多少
     */
    private BigDecimal fullMoney;


    /**
     * 减多少
     */
    private BigDecimal subMoney;


    /**
     * 有效时间
     */
    private String validTime;


    /**
     * 优惠券类型
     */
    private DisCouponTypeEnum disCouponType;


    /**
     * 优惠券使用状态
     */
    private DisCouponUseTypeEnum disCouponUseTypeEnum;

}