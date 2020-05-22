package cn.ruanyun.backInterface.modules.business.order.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.enums.OrderTypeEnum;
import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
import cn.ruanyun.backInterface.common.utils.CommonUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 订单
 * @author fei
 */
@Data
@Entity
@Table(name = "t_order")
@TableName("t_order")
@Accessors(chain = true)
public class Order extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    private String orderNum = CommonUtil.getRandomNum();
    /**
     * 订单状态
     */
    private OrderStatusEnum orderStatus = OrderStatusEnum.PRE_PAY;
    /**
     * 订单类型枚举
     */
    private OrderTypeEnum typeEnum;
    /**
     * 商家id
     */
    private String userId;
    /**
     * 支付类型
     */
    private PayTypeEnum payTypeEnum;
    /**
     * 总价格
     */
    private BigDecimal totalPrice = new BigDecimal(0);
    /**
     * 快递单号
     */
    private String expressCode;

    /**
     * 收获地址id
     */
    private String addressId;
    /**
     * 收货人
     */
    private String consignee;

    /**
     * 收获手机号
     */
    private String phone;

    /**
     * 收获地址
     */
    private String address;

    /**
     * 员工id(销售员工)
     */
    private String staffId;

    /**
     * 商品描述
     */
    private String goodDesc;

    @ApiModelProperty(value = "购买状态 1购买 2租赁 3购买和租赁")
    private Integer buyState;

    @ApiModelProperty(value = "租赁状态 1尾款线上支付  2尾款线下支付 ")
    private Integer leaseState;
}