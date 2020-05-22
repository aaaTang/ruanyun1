package cn.ruanyun.backInterface.modules.business.orderDetail.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.modules.business.discountMy.pojo.DiscountMy;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 子订单
 * @author wj
 */
@Data
@Entity
@Table(name = "t_order_detail")
@TableName("t_order_detail")
public class OrderDetail extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    private String orderId;

    @ApiModelProperty(value = "商品id")
    private String goodId;

    /**
     * 商品名称
     */
    private String goodName;

    /**
     * 商品图片
     */
    private String goodPics;

    /**
     * 商品新价格
     */
    private BigDecimal goodNewPrice;

    /**
     * 积分
     */
    private Integer integral;

    /**
     * 属性
     */
    private String attrSymbolPath;

    /**
     * 购买数量
     */
    private Integer buyCount;

    /**
     *
     */
    private String discountMyId;

    /**
     * 满多少
     */
    private BigDecimal fullMoney;
    /**
     * 减多少
     */
    private BigDecimal subtractMoney;

}