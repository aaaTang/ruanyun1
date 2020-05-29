package cn.ruanyun.backInterface.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fei
 */
@Data
public class PageVo implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "页号")
    private int pageNumber;

    @ApiModelProperty(value = "页面大小")
    private int pageSize;

    @ApiModelProperty(value = "排序字段",hidden = true)
    private String sort;

    @ApiModelProperty(value = "排序方式 asc/desc", hidden = true)
    private String order;
}
