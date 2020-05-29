package cn.ruanyun.backInterface.modules.auctionCalendar.site.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-05-29 15:31
 **/

@Data
@Accessors(chain = true)
public class SiteDetailVo {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("场地名称")
    private String siteName;

    @ApiModelProperty("场地规格参数")
    private List<String> siteItemValues;

    @ApiModelProperty("场地图片")
    private List<String> sitePics;
}
