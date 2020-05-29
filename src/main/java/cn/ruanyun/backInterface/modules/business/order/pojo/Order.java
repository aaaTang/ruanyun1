package cn.ruanyun.backInterface.modules.business.order.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.*;
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


    @ApiModelProperty(value = "商家id")
    private String userId;

    @ApiModelProperty(value = "员工id(销售员工)")
    private String staffId;

    @ApiModelProperty(value = "订单编号")
    private String orderNum = CommonUtil.getRandomNum();

    @ApiModelProperty(value = "订单状态")
    private OrderStatusEnum orderStatus = OrderStatusEnum.PRE_PAY;

    @ApiModelProperty(value = "订单类型")
    private OrderTypeEnum typeEnum;

    @ApiModelProperty(value = "支付类型")
    private PayTypeEnum payTypeEnum;

    @ApiModelProperty(value = "购买类型")
    private BuyTypeEnum buyType;

    @ApiModelProperty(value = "支付尾款类型")
    private RentTypeEnum rentType = RentTypeEnum.NO_PAY;

    @ApiModelProperty(value = "尾款支付类型")
    private PayTypeEnum rentPayType;

    @ApiModelProperty(value = "总价格")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "支付尾款金额")
    private BigDecimal payGoodBalancePayment = new BigDecimal(0);


    /*---------档期订单------------*/

    @ApiModelProperty(value = "场地id")
    private String siteId;

    @ApiModelProperty("上午&下午")
    private DayTimeTypeEnum dayTimeType;

    @ApiModelProperty("预约档期")
    private String scheduleAppointment;

    /*---------收货人信息------------*/

    @ApiModelProperty(value = "收货人")
    private String consignee;

    @ApiModelProperty(value = "收获手机号")
    private String phone;

    @ApiModelProperty(value = "收获手机号")
    private String address;

    /*-----------快递信息--------------*/

    @ApiModelProperty(value = "快递单号")
    private String expressCode;

    @ApiModelProperty(value = "运费")
    private BigDecimal freight = new BigDecimal(0);
}