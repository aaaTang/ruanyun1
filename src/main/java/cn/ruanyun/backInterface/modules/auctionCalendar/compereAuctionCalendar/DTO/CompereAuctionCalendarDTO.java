package cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.DTO;

import cn.ruanyun.backInterface.common.enums.DayTimeTypeEnum;
import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CompereAuctionCalendarDTO {

    private String id;

    @ApiModelProperty("商品id")
    private String  goodsId;

    @ApiModelProperty("主持人 套餐 ，商品")
    private GoodTypeEnum goodTypeEnum;

    @ApiModelProperty("上午&下午")
    private DayTimeTypeEnum dayTimeType;

    @ApiModelProperty("档期时间")
    private String scheduleTime;
}
