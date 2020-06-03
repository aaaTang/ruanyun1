package cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.VO;

import cn.ruanyun.backInterface.common.enums.DayTimeTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class CompereAuctionCalendarVO {


    private String id;

    @ApiModelProperty("上午&下午")
    private DayTimeTypeEnum dayTimeType;

    @ApiModelProperty("没有档期时间")
    private String noScheduleTime;
}
