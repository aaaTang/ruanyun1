package cn.ruanyun.backInterface.modules.business.itemAttrVal.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ItemAttrValVo {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("规格属性名称")
    private String attrValue;

}
