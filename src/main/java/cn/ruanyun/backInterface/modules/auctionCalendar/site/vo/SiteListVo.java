package cn.ruanyun.backInterface.modules.auctionCalendar.site.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-05-29 15:29
 **/

@Data
@Accessors(chain = true)
public class SiteListVo {

    @ApiModelProperty(value = "场地id")
    private String id;

    @ApiModelProperty("场地名称")
    private String siteName;

    @ApiModelProperty(value = "场地桌数")
    private String siteItemValue;
}
