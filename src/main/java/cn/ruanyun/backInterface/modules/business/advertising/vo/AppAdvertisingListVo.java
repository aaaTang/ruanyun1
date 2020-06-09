package cn.ruanyun.backInterface.modules.business.advertising.vo;

import cn.ruanyun.backInterface.common.enums.AdvertisingJumpTypeEnum;
import cn.ruanyun.backInterface.common.enums.AdvertisingTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors( chain = true)
public class AppAdvertisingListVo {

    private String id;

    @ApiModelProperty("广告类型")
    private Integer advertisingType;

    @ApiModelProperty("跳转类型")
    private Integer advertisingJumpType;

    @ApiModelProperty("图片")
    private String pic;

    @ApiModelProperty("链接")
    private String url;

    @ApiModelProperty("商家跳转模板")
    private Integer storeType;

}
