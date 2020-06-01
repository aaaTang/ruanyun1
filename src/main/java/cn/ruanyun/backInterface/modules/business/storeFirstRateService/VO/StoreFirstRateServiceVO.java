package cn.ruanyun.backInterface.modules.business.storeFirstRateService.VO;

import cn.ruanyun.backInterface.common.enums.CheckEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Administrator
 */
@Data
public class StoreFirstRateServiceVO {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("优质服务Ids")
    private String firstRateServiceIds;

    @ApiModelProperty("审核状态")
    private CheckEnum checkStatus = CheckEnum.PRE_CHECK;

    @ApiModelProperty("审核原因")
    private String checkReason;

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("标签名称")
    private String itemName;
}
