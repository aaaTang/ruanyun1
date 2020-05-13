package cn.ruanyun.backInterface.modules.base.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Administrator
 */
@Data
@Accessors(chain = true)
public class UserPayPasswordVo {


    /**
     * 手机验证码
     */
    private String code;

    /**
     * 支付密码
     */
    private String payPassword;

    /**
     * 旧支付密码
     */
    private String oldPayPassword;

}
