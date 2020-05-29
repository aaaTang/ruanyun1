package cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.vo;

import cn.ruanyun.backInterface.common.enums.DayTimeTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-05-29 15:43
 **/

@Data
@Accessors(chain = true)
public class SiteAuctionCalendarVo {

    private String id;

    @ApiModelProperty("场地id")
    private String siteId;

    @ApiModelProperty("上午&下午")
    private DayTimeTypeEnum dayTimeType;

    @ApiModelProperty("没有档期时间")
    private String noScheduleTime;
}
