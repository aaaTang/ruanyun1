package cn.ruanyun.backInterface.modules.business.inviteMessage.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-05-28 20:35
 **/
@Data
@Accessors(chain = true)
public class InviteMessageListVo {


    private String id;

    @ApiModelProperty("工作室id")
    private String studioId;

    @ApiModelProperty("邀请人名称")
    private String nickName;

    @ApiModelProperty("邀请人头像")
    private String avatar;

    @ApiModelProperty(value = "邀请人手机号")
    private String mobile;
}
