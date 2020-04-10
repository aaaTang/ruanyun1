package cn.ruanyun.backInterface.modules.business.order.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.enums.OrderTypeEnum;
import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

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
public class Order extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    private String orderNum = ToolUtil.getRandomString(6);
    /**
     * 订单状态
     */
    private OrderStatusEnum orderStatus = OrderStatusEnum.PRE_PAY;
    /**
     * 1商品，2套餐
     */
    private OrderTypeEnum typeEnum;
    /**
     * 1商品，2套餐
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


    public double getTotalPrice(){
        return this.totalPrice.doubleValue();
    }



}