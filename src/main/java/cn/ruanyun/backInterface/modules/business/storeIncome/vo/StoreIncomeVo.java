package cn.ruanyun.backInterface.modules.business.storeIncome.vo;

import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Administrator
 */
@Data
@Accessors(chain = true)
public class StoreIncomeVo {


    private String id;

    /**
     * 订单号
     */
    private String orderNum;

    /**
     * 支付类型
     */
    private PayTypeEnum payType;

    /**
     * 购买人
     */
    private String buyerName;

    /**
     * 购买人手机号
     */
    private String buyerMobile;

    /**
     * 购买时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date buyTime;

    /**
     * 收入金额
     */
    private BigDecimal incomeMoney;

    /**
     * 收入类型
     */
    private PayTypeEnum incomeType;

    /**
     * 到账时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date incomeTime;
}
