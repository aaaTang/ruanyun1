package cn.ruanyun.backInterface.modules.business.order.vo;


import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.enums.OrderTypeEnum;
import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: xboot-plus
 * @description: 后台管理订单列表
 * @author: fei
 * @create: 2020-02-13 10:18
 **/
@Data
@Accessors(chain = true)
public class  BackOrderListVO {

    private String id;

    @ApiModelProperty(value = "所属店铺")
    private String storeName;

    @ApiModelProperty(value = "订单编号")
    private String orderNum;

    @ApiModelProperty(value = "订单状态")
    private OrderStatusEnum orderStatus;

    @ApiModelProperty(value = "订单总金额")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "订单类型")
    private OrderTypeEnum typeEnum;

    @ApiModelProperty(value = "支付方式")
    private PayTypeEnum payTypeEnum;

    @ApiModelProperty(value = "尾款支付类型")
    private PayTypeEnum rentPayType;

    @ApiModelProperty(value = "收货人")
    private String consignee;

    @ApiModelProperty(value = "收获地址")
    private String address;

    @ApiModelProperty(value = "联系方式")
    private String phone;

    @ApiModelProperty(value = "运费金额")
    private BigDecimal freightPrice;


    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
