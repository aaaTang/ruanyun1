package cn.ruanyun.backInterface.modules.merchant.authentication.DTO;

import cn.ruanyun.backInterface.common.enums.AuthenticationTypeEnum;
import cn.ruanyun.backInterface.common.enums.CheckEnum;
import lombok.Data;

@Data
public class AuthenticationDTO {

    private String id;
    /**
     * 连锁认证：品牌商家、品牌联盟
     */
    private AuthenticationTypeEnum authenticationTypeEnum ;

    /**
     * 状态
     */
    private CheckEnum status;
}
