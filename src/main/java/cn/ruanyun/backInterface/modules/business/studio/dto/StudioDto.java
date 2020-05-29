package cn.ruanyun.backInterface.modules.business.studio.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-05-28 20:11
 **/

@Data
@Accessors(chain = true)
public class StudioDto {


    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "商家名称")
    private String nickname;

}
