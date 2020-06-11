package cn.ruanyun.backInterface.modules.business.order.dto;

import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-05-28 11:48
 **/

@Data
@Accessors(chain = true)
@ApiModel(value = "查询订单的参数")
public class BackOrderListDto {

    @ApiModelProperty(value = "商家id, 如果为管理员可以不传")
    private String storeId;

    @ApiModelProperty(value = "所属店铺")
    private String storeName;

    @ApiModelProperty(value = "支付时间开始时间")
    private String payTimeBeginTime;

    @ApiModelProperty(value = "支付时间结束时间")
    private String payTimeEndTime;

    @ApiModelProperty(value = "客户手机号")
    private String phone;

    @ApiModelProperty(value = "订单状态")
    private OrderStatusEnum orderStatus;

    @ApiModelProperty(value = "购买状态 1购买 2租赁 3购买和租赁")
    private Integer buyState;

    @ApiModelProperty(value = "用户名称")
    private String nickName;
}
