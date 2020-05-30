package cn.ruanyun.backInterface.modules.business.discountMy.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 我领取的优惠券
 * @author wj
 */
@Data
@Entity
@Table(name = "t_discount_my")
@TableName("t_discount_my")
public class DiscountMy extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "优惠券id")
    private String discountCouponId;

    @ApiModelProperty(value = "0未使用，1已使用，2过期")
    private Integer status = CommonConstant.STATUS_NORMAL;

    /*@ApiModelProperty(value = "优惠卷有效时间")
    private Date discountCouponTime;*/
}