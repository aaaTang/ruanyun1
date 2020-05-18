package cn.ruanyun.backInterface.modules.business.staffManagement.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StaffDto {


    private String id;

    /**
     * 手机验证码
     */
    private String code;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用戶昵称
     */
    private String nickName;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 性别
     */
    private String sex;

    /**
     * 个人简介
     */
    private String individualResume;


}
