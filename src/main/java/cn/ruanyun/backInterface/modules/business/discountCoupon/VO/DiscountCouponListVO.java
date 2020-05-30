package cn.ruanyun.backInterface.modules.business.discountCoupon.VO;

import cn.ruanyun.backInterface.common.enums.DisCouponTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

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
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String validityPeriod;

    /**
     * 是否使用
     */
    private Boolean isReceive;


}
