package cn.ruanyun.backInterface.modules.auctionCalendar.sitePrice.VO;

import cn.ruanyun.backInterface.common.enums.DayTimeTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class SitePriceVO {

    private String id;

    @ApiModelProperty("场地id")
    private String siteId;

    @ApiModelProperty("上午&下午")
    private DayTimeTypeEnum dayTimeType;

    @ApiModelProperty("档期时间")
    private String scheduleTime;

    @ApiModelProperty(value = "场地价格")
    private String sitePrice;
}
