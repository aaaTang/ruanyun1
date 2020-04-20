package cn.ruanyun.backInterface.modules.business.goodCategory.VO;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class WebGoodCategoryVO {

    private String id;
    @ApiModelProperty(value = "分类名称")
    private String title;

    @ApiModelProperty(value = "父id")
    private String parentId;

    @ApiModelProperty(value = "分类图片")
    private String pic;

    @ApiModelProperty(value = "是否为父节点(含子节点) 默认false")
    private Boolean isParent = false;

    @ApiModelProperty(value = "排序值")
    @Column(precision = 10, scale = 2)
    private BigDecimal sortOrder;

    @ApiModelProperty(value = "是否启用 0启用 -1禁用")
    private Integer status = CommonConstant.STATUS_NORMAL;


}
