package cn.ruanyun.backInterface.modules.business.balance.VO;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class AppBalanceVO {

    private BigDecimal balance;

    private List<BalanceVO>  balanceVOList;
}
