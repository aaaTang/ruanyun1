package cn.ruanyun.backInterface.modules.base.dto;

import cn.ruanyun.backInterface.common.enums.UserTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: ruanyun
 * @description:
 * @author: fei
 * @create: 2020-02-14 16:42
 **/
@Data
@Accessors(chain = true)
public class UserDTO {


    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty(value = "用戶昵称", hidden = true)
    private String nickName;

    @ApiModelProperty(value = "姓名", hidden = true)
    private String username;

    @ApiModelProperty(value = "用户角色类型", hidden = true)
    private UserTypeEnum userType;

    @ApiModelProperty(value = "验证码", hidden = true)
    private String code;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty(value = "邀请码", hidden = true)
    private String invitationCode;

    @ApiModelProperty(value = "是否严选 0：不是 1：是", hidden = true)
    private Integer isBest;

    @ApiModelProperty(value = "用户地址", hidden = true)
    private String address;

    @ApiModelProperty("类型 1.个人 2.商家")
    private String accountType;

}
