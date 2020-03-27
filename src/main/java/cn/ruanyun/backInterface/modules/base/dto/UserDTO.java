package cn.ruanyun.backInterface.modules.base.dto;

import cn.ruanyun.backInterface.common.enums.UserTypeEnum;
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
         * 姓名
     */
    private String username;


    /**
     * 用户角色类型
     */
    private UserTypeEnum userType;

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

    /**
     *是否严选 0：不是 1：是
     */
    private Integer isBest;
    /**
     *用户地址
     */
    private String address;

}
