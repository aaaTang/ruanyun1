package cn.ruanyun.backInterface.modules.base.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author Administrator
 */

@Data
@Accessors(chain = true)
public class UserProfitVO {

    private String id;

    /**
     * 用戶昵称
     */
    private String username;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 佣金总量
     */
    private BigDecimal totalProfitMoney;
}
