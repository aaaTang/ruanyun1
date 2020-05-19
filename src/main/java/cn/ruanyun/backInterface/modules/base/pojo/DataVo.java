package cn.ruanyun.backInterface.modules.base.pojo;

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

    /**
     * mybatisPlus 分页
     */
    private Long totalSize;

    /**
     * 当前页
     */
    private Long currentPageNum;

    /**
     * 数据集合
     */
    private List<T> dataResult;
}
