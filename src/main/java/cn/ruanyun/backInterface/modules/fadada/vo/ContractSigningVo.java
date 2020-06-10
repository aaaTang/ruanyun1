package cn.ruanyun.backInterface.modules.fadada.vo;

import cn.ruanyun.backInterface.common.enums.CheckEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-06-10 17:12
 **/
@Data
@Accessors(chain = true)
public class ContractSigningVo {

    private String id;

    @ApiModelProperty(value = "合同编号")
    private String contractId;

    @ApiModelProperty("甲方合同标题")
    private String partOneDocTitle;

    @ApiModelProperty("乙方合同标题")
    private String partTwoDocTitle;

    @ApiModelProperty("甲方交易号")
    private String partOneTransactionId;

    @ApiModelProperty("乙方交易号")
    private String partTwoTransactionId;

    @ApiModelProperty("审核状态")
    private Integer check;

    @ApiModelProperty("审核意见")
    private String checkReason;

}
