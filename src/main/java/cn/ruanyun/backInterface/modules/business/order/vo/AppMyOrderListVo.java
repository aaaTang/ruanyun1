package cn.ruanyun.backInterface.modules.business.order.vo;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.*;
import cn.ruanyun.backInterface.common.utils.CommonUtil;
import cn.ruanyun.backInterface.modules.business.discountCoupon.VO.DiscountCouponListVO;
import cn.ruanyun.backInterface.modules.business.orderDetail.vo.OrderDetailVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-05-28 16:19
 **/
@Data
@Accessors(chain = true)
public class AppMyOrderListVo {

    @ApiModelProperty(value = "订单id")
    private String id;

    @ApiModelProperty(value = "订单编号")
    private String orderNum;

    @ApiModelProperty(value = "订单状态")
    private OrderStatusEnum orderStatus;

    @ApiModelProperty(value = "订单状态code")
    private Integer orderStatusCode;

    @ApiModelProperty(value = "总价格")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "商品或者套餐信息")
    private OrderDetailVo orderDetailVo;

    @ApiModelProperty(value = "定金")
    private BigDecimal goodDeposit;

    @ApiModelProperty(value = "支付尾款金额")
    private BigDecimal payGoodBalancePayment = new BigDecimal(0);

    @ApiModelProperty(value = "商品描述")
    private String goodDesc;

    @ApiModelProperty(value = "支付尾款类型")
    private RentTypeEnum rentType = RentTypeEnum.NO_PAY;

    @ApiModelProperty(value = "尾款支付类型")
    private PayTypeEnum rentPayType;


    /**************************档期*****************************/

    @ApiModelProperty(value = "订单类型")
    private OrderTypeEnum typeEnum;

    @ApiModelProperty(value = "场地id")
    private String siteId;

    @ApiModelProperty("上午&下午")
    private DayTimeTypeEnum dayTimeType;

    @ApiModelProperty("预约档期")
    private String scheduleAppointment;

    @ApiModelProperty(value = "租赁尾款类型 1尾款线上支付  2尾款线下支付 ")
    private Integer leaseState;
}
