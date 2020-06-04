package cn.ruanyun.backInterface.modules.business.goodCategory.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;

@Data
@Accessors(chain = true)
public class CategoryVo {

    private String id;

    @ApiModelProperty(value = "分类名称")
    private String title;

    @ApiModelProperty(value = "是否有下级")
    private Boolean havaNext;
}
