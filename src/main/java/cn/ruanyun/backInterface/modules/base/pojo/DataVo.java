package cn.ruanyun.backInterface.modules.base.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class DataVo<T> {
    /**
     * 数据大小
     */
    private Integer totalNumber;

    @ApiModelProperty(value = "数据总大小")
    private Long totalSize;

    @ApiModelProperty(value = "当前页")
    private Long currentPageNum;

    @ApiModelProperty(value = "数据集合")
    private List<T> dataResult;

    @ApiModelProperty(value = "总页数")
    private Long totalPage;
}
