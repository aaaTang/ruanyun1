package cn.ruanyun.backInterface.modules.business.order.VO;

import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.enums.OrderTypeEnum;
import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
import cn.ruanyun.backInterface.common.utils.CommonUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
public class PCgetShopOrderListVO {

    private String id;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 订单号
     */
    private String orderNum = CommonUtil.getRandomNum();
    /**
     * 订单状态
     */
    private OrderStatusEnum orderStatus;
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
}
