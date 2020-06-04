package cn.ruanyun.backInterface.modules.auctionCalendar.site.vo;

import cn.ruanyun.backInterface.common.enums.DayTimeTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class SiteDetailTimeVO {

    @ApiModelProperty(value = "场地价格")
    private BigDecimal sitePrice;

    @ApiModelProperty("上午&下午")
    private DayTimeTypeEnum dayTimeType;

    @ApiModelProperty("是否有档期 0 无  1 有")
    private Integer status;
}
