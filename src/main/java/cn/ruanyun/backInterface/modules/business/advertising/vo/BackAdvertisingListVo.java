package cn.ruanyun.backInterface.modules.business.advertising.vo;

import cn.ruanyun.backInterface.common.enums.AdvertisingJumpTypeEnum;
import cn.ruanyun.backInterface.common.enums.AdvertisingTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: ruanyun
 * @description: 后台轮播图列表
 * @author: fei
 * @create: 2020-06-09 19:46
 **/
@Data
@Accessors(chain = true)
public class BackAdvertisingListVo {


    private String id;

    @ApiModelProperty("广告类型")
    private AdvertisingTypeEnum advertisingType;

    @ApiModelProperty("跳转类型")
    private AdvertisingJumpTypeEnum advertisingJumpType;

    @ApiModelProperty("跳转描述")
    private String jumpDesc;

    @ApiModelProperty("图片")
    private String pic;

    @ApiModelProperty("链接")
    private String url;
}
