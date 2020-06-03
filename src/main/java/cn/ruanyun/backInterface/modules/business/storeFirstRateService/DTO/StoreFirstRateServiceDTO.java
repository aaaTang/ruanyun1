package cn.ruanyun.backInterface.modules.business.storeFirstRateService.DTO;

import cn.ruanyun.backInterface.common.enums.CheckEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class StoreFirstRateServiceDTO {

    private String id;

    @ApiModelProperty("创建人")
    private String createBy;
    @ApiModelProperty("优质服务Ids")
    private String firstRateServiceIds;

    @ApiModelProperty("审核状态")
    private CheckEnum checkStatus;
}
