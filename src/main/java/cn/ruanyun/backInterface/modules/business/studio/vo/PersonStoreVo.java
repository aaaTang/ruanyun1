package cn.ruanyun.backInterface.modules.business.studio.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-05-29 11:47
 **/

@Data
@Accessors(chain = true)
public class PersonStoreVo {

    private String id;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "手机号")
    private String mobile;
}
