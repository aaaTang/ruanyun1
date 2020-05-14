package cn.ruanyun.backInterface.modules.business.profitPercent.vo;

import cn.ruanyun.backInterface.common.enums.ProfitTypeEnum;
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
public class ProfitPercentVo {


    private String id;

    /**
     * 分佣类型
     */
    private ProfitTypeEnum profitTypeEnum;

    /**
     * 平台分成
     */
    private BigDecimal platformProfit;

    /**
     * 商家分成
     */
    private BigDecimal storeProfit;

    /**
     * 个人分成
     */
    private BigDecimal personProfit;

    /**
     * 推荐人分成
     */
    private BigDecimal recommendProfit;


    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
