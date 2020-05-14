package cn.ruanyun.backInterface.modules.business.profitDetail.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 分红明细
 * @author z
 */
@Data
@Entity
@Table(name = "t_profit_detail")
@TableName("t_profit_detail")
@Accessors(chain = true)
public class ProfitDetail extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    private String orderId;


    /**
     * 分红金额
     */
    private BigDecimal profitMoney;


    /**
     * 到账类型
     */
    private PayTypeEnum payType;


    /**
     * 是否分佣成功
     */
    private BooleanTypeEnum profitStatus;
}