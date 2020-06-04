package cn.ruanyun.backInterface.modules.auctionCalendar.compereNoCalendars.DTO;

import cn.ruanyun.backInterface.common.enums.DayTimeTypeEnum;
import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CompereNoCalendarsDTO {

    private String  id;

    @ApiModelProperty("商品id")
    private String  goodsId;

    @ApiModelProperty("主持人 套餐 ，商品")
    private GoodTypeEnum goodTypeEnum;

    @ApiModelProperty("上午&下午")
    private DayTimeTypeEnum dayTimeType;

    @ApiModelProperty("没有档期时间")
    private String noScheduleTime;
}
