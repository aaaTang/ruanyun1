package cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.VO;

import cn.ruanyun.backInterface.common.enums.DayTimeTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author Administrator
 */
@Data
@Accessors(chain = true)
public class CompereAuctionCalendarVO {



    @ApiModelProperty("上午&下午")
    private DayTimeTypeEnum dayTimeType;

    @ApiModelProperty("档期时间")
    private String scheduleTime;

    @ApiModelProperty(value = "档期的价格")
    private BigDecimal sitePrice;

    @ApiModelProperty(value = "是否有档期 0无 1 有")
    private Integer status;


}
