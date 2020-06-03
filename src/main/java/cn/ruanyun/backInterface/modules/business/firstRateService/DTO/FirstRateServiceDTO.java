package cn.ruanyun.backInterface.modules.business.firstRateService.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class FirstRateServiceDTO {


    private String id;

    @ApiModelProperty("分类id")
    private String goodCategoryId;

    @ApiModelProperty("标签名称")
    private String itemName;
}
