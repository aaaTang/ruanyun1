package cn.ruanyun.backInterface.modules.fadada.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.CheckEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.zip.Checksum;

/**
 * 合同签署
 * @author z
 */
@Data
@Entity
@Table(name = "t_contract_signing")
@TableName("t_contract_signing")
@Accessors(chain = true)
public class ContractSigning extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

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
    private CheckEnum checkStatus;

    @ApiModelProperty("审核意见")
    private String checkReason;
}