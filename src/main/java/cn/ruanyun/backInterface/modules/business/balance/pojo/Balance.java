package cn.ruanyun.backInterface.modules.business.balance.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 余额明细
 * @author zhu
 */
@Data
@Entity
@Table(name = "t_balance")
@TableName("t_balance")
@Accessors(chain = true)
public class Balance extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 订单id
     */
    private String orderId;

    /**
     * 商品总价格
     */
    private BigDecimal totalPrice;

    /**
     * 满多少
     */
    private BigDecimal fullMoney;

    /**
     * 减多少
     */
    private BigDecimal subtractMoney;

    /**
     * 付钱之前的余额
     */
    private BigDecimal payMoney;

    /**
     * 付钱之前的余额
     */
    private BigDecimal surplusMoney;


}