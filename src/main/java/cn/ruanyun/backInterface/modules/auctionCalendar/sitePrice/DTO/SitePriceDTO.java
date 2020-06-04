package cn.ruanyun.backInterface.modules.auctionCalendar.sitePrice.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class SitePriceDTO {

    private String id;

    @ApiModelProperty("场地id")
    private String siteId;

    @ApiModelProperty("档期时间")
    private String scheduleTime;

    @ApiModelProperty(value = "场地价格")
    private String sitePrice;

}
