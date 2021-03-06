package cn.ruanyun.backInterface.modules.business.discountCoupon.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
import cn.ruanyun.backInterface.common.enums.DisCouponTypeEnum;
import cn.ruanyun.backInterface.common.enums.UsableRangeTypeEnum;
import cn.ruanyun.backInterface.modules.business.storeAudit.pojo.StoreAudit;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
     * 使用范围  CLASSIFY分类 ,  LABEL标签属性
     */
    private UsableRangeTypeEnum usableRangeTypeEnum;

    /**
     *  使用范围id
     */
    private String usableRangeId = "0";
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
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validityPeriod;


}