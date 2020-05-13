package cn.ruanyun.backInterface.modules.business.profitPercent.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.ProfitTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 分红比例
 * @author z
 */
@Data
@Entity
@Table(name = "t_profit_percent")
@TableName("t_profit_percent")
public class ProfitPercent extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 分佣类型
     */
    private ProfitTypeEnum profitTypeEnum;

    /**
     * 平台分成
     */
    private BigDecimal platformProfit = new BigDecimal(0);

    /**
     * 商家分成
     */
    private BigDecimal storeProfit = new BigDecimal(0);

    /**
     * 个人分成
     */
    private BigDecimal personProfit = new BigDecimal(0);


    /**
     * 推荐人分成
     */
    private BigDecimal recommendProfit = new BigDecimal(0);

}