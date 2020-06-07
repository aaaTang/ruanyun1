package cn.ruanyun.backInterface.modules.auctionCalendar.compereNoCalendars.VO;

import cn.ruanyun.backInterface.common.enums.DayTimeTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AppGetCompereNoCalendarsVO {

    @ApiModelProperty("商品id")
    private String  goodsId;

    @ApiModelProperty("上午&下午")
    private DayTimeTypeEnum dayTimeType;

    @ApiModelProperty("预约档期")
    private String scheduleAppointment;
}
