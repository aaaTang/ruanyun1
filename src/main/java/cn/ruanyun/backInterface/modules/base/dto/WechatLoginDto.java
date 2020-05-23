package cn.ruanyun.backInterface.modules.base.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Administrator
 */
@Data
@Accessors(chain = true)
public class WechatLoginDto {



    private String id;

    /**
     * 微信openId
     */
    private String openId;

    /**
     * 微信认证code
     */
    private String code;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 验证码
     */
    private String messageCode;


    /**
     * token
     */
    private String accessToken;

}
