package cn.ruanyun.backInterface.modules.business.discountMy.VO;

import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
import cn.ruanyun.backInterface.common.enums.DisCouponTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
public class DiscountVO {

    /**
     * 优惠券id
     */
    private String id;


    @ApiModelProperty(value = "创建者", hidden = true)
    @CreatedBy
    private String createBy;

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
