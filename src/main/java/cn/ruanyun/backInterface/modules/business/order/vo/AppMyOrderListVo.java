package cn.ruanyun.backInterface.modules.business.order.vo;

import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.utils.CommonUtil;
import cn.ruanyun.backInterface.modules.business.discountCoupon.VO.DiscountCouponListVO;
import cn.ruanyun.backInterface.modules.business.orderDetail.vo.OrderDetailVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

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

    @ApiModelProperty(value = "商品或者套餐信息")
    private OrderDetailVo orderDetailVo;

}
