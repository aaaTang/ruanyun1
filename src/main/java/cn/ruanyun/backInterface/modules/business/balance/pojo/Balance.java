package cn.ruanyun.backInterface.modules.business.balance.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.AddOrSubtractTypeEnum;
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
     * 标题
     */
    private String title;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 加减
     */
    private AddOrSubtractTypeEnum addOrSubtractTypeEnum;


    /**
     * 金额
     */
    private BigDecimal price;
}