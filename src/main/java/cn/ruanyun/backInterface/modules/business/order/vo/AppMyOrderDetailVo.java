package cn.ruanyun.backInterface.modules.business.order.vo;

import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
import cn.ruanyun.backInterface.modules.business.orderDetail.vo.OrderDetailVo;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-05-28 16:20
 **/

@Data
@Accessors(chain = true)
public class AppMyOrderDetailVo {


    @ApiModelProperty(value = "订单id")
    private String id;

    /*-----------收货地址----------*/

    @ApiModelProperty(value = "收货人")
    private String consignee;

    @ApiModelProperty(value = "收获手机号")
    private String phone;

    @ApiModelProperty(value = "收获手机号")
    private String address;

    @ApiModelProperty(value = "运费")
    private BigDecimal freight;


    /*------------商品信息-----------*/

    @ApiModelProperty(value = "商品详细信息")
    private OrderDetailVo orderDetailVo;

    /*------------订单信息-----------*/

    @ApiModelProperty(value = "订单编号")
    private String orderNum;

    @ApiModelProperty(value = "支付类型")
    private PayTypeEnum payTypeEnum;

    @ApiModelProperty(value = "尾款支付类型")
    private PayTypeEnum rentPayType;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "下单时间")
    private Date createTime;


}