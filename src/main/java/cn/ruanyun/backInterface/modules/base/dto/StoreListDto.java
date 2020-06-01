package cn.ruanyun.backInterface.modules.base.dto;

import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Administrator
 */
@Data
@Accessors(chain = true)
public class StoreListDto {

    @ApiModelProperty(value = "分类id")
    private String goodCategoryId;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;

    @ApiModelProperty(value = "数据总大小")
    private Long totalSize;

    @ApiModelProperty(value = "当前页")
    private Long currentPageNum;

}
