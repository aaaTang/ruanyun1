package cn.ruanyun.backInterface.modules.base.vo;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Administrator
 */
@Data
@Accessors(chain = true)
public class WechatVo {


    /**
     * 微信openId
     */
    private String openId;

    /**
     * token
     */
    private String accessToken;
}
