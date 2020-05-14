package cn.ruanyun.backInterface.modules.business.storeIncome.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author Administrator
 */
@Data
@Accessors(chain = true)
public class StoreIncomeCountVo {

    /**
     * 日收入
     */
    private BigDecimal dayIncome = new BigDecimal(0);


    /**
     * 月收入
     */
    private BigDecimal monthIncome = new BigDecimal(0);


    /**
     * 年收入
     */
    private BigDecimal yearIncome = new BigDecimal(0);


    /**
     * 总收入
     */
    private BigDecimal totalIncome = new BigDecimal(0);
}
