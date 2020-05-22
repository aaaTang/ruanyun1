package cn.ruanyun.backInterface.modules.business.order.VO;

import cn.ruanyun.backInterface.modules.business.orderDetail.VO.OrderDetailListVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @program: xboot-plus
 * @description:
 * @author: fei
 * @create: 2020-02-12 18:11
 **/
@Data
@Accessors(chain = true)
public class OrderDetailVO {


    private String id;

    /**
     * 订单号
     */
    private String orderNum;
    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 支付类型
     */
    private String payTypeEnum;
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
     * 商品信息
     */
    private List<OrderDetailListVO> orderDetails;

    /**
     * 商品描述
     */
    private String goodDesc;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 商家地址
     */
    private String shopAddress;

}
