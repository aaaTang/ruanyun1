package cn.ruanyun.backInterface.modules.base.dto;

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

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 验证码
     */
    private String code;

    /**
     * 密码
     */
    private String password;

    /**
     * 邀请码
     */
    private String invitationCode;

}
