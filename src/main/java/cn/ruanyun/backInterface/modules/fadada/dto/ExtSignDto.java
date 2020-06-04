package cn.ruanyun.backInterface.modules.fadada.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Administrator
 */
@Data
@Accessors(chain = true)
public class ExtSignDto {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("文档标题")
    private String docTitle;

}
