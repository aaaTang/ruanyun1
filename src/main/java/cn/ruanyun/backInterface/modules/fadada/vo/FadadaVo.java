package cn.ruanyun.backInterface.modules.fadada.vo;

import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FadadaVo {

    @ApiModelProperty("乙方合同标题")
    private String partTwoDocTitle;

    @ApiModelProperty("甲方合同标题")
    private String partOneDocTitle;

    @ApiModelProperty(value = "合同编号")
    private String contractId;

    @ApiModelProperty("合同公网下载地址")
    private String docUrl;

    @ApiModelProperty("是否归档")
    private BooleanTypeEnum contractFiling = BooleanTypeEnum.NO;

    @ApiModelProperty(value = "乙方签署人")
    private String partTwoExtSignName;

}
