package cn.ruanyun.backInterface.modules.auctionCalendar.site.vo;

import cn.ruanyun.backInterface.modules.business.itemAttrVal.vo.ItemAttrValVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

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

    @ApiModelProperty("场地规格参数")
    private List<ItemAttrValVo> siteItemValue;

    @ApiModelProperty(value = "场地介绍图片")
    private String sitePic;
}
