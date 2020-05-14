package cn.ruanyun.backInterface.modules.business.profitDetail.vo;

import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
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
public class ProfitDetailVo {


    /**
     * 分红人
     */
    private String nickName;

    /**
     * 分红人手机号
     */
    private String mobile;

    /**
     * 订单编号
     */
    private String orderNum;

    /**
     * 分红总金额
     */
    private BigDecimal profitTotalMoney;

    /**
     * 分红金额
     */
    private BigDecimal profitMoney;

    /**
     * 分红类型
     */
    private PayTypeEnum payType;

    /**
     * 是否分佣成功
     */
    private BooleanTypeEnum profitStatus;


    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
