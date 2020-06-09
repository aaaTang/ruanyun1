package cn.ruanyun.backInterface.modules.business.discountCoupon.VO;

import cn.ruanyun.backInterface.common.enums.DisCouponTypeEnum;
import cn.ruanyun.backInterface.common.enums.UsableRangeTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AppDiscountCouponListVO {


    private String id;

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
    private String usableRangeId;
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
