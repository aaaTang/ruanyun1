package cn.ruanyun.backInterface.modules.auctionCalendar.site.vo;

import cn.ruanyun.backInterface.modules.business.itemAttrVal.vo.ItemAttrValVo;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-05-29 17:54
 **/

@Data
@Accessors(chain = true)
public class BackSiteListVo {


    private String id;

    @ApiModelProperty("场地名称")
    private String siteName;

    @ApiModelProperty("场地规格参数")
    private List<ItemAttrValVo> siteItemValue;

    @ApiModelProperty(value = "场地介绍图片")
    private List<String> sitePics;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createTime;

    @ApiModelProperty(value = "场地价格")
    private String sitePrice;
}
