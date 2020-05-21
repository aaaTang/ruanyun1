package cn.ruanyun.backInterface.modules.business.discountCoupon.VO;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
import cn.ruanyun.backInterface.common.enums.DisCouponTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
public class PcGetDiscountCouponListVO {

    private String id;

    /**
     * 创建人名称
     */
    private String createName;
    /**
     * 名称
     */
    private String title;

    /**
     * 优惠券类型 ALL_USE全场满减 ONE_PRODUCT商品满减
     */
    private DisCouponTypeEnum disCouponType;

    /**
     * 商家id
     */
    private String storeAuditOid;

    /**
     * 商家名称
     */
    private String shopName;

    /**
     * 商品id
     */
    private String goodsPackageId;

    /**
     * 商品名称
     */
    private String goodsName;

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
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date validityPeriod;

    /**
     * 是否过期
     */
    private BooleanTypeEnum pastDue ;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "删除标志 默认0")
    private Integer delFlag ;
}
