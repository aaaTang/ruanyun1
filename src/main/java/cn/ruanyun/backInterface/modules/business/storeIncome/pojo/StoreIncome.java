package cn.ruanyun.backInterface.modules.business.storeIncome.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 店铺收入
 * @author z
 */
@Data
@Entity
@Table(name = "t_store_income")
@TableName("t_store_income")
@Accessors(chain = true)
public class StoreIncome extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    private String orderId;


    /**
     * 收入金额
     */
    private BigDecimal incomeMoney;


    /**
     * 收入类型
     */
    private PayTypeEnum incomeType;

}