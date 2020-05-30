package cn.ruanyun.backInterface.modules.merchant.trustIdentity.DTO;

import cn.ruanyun.backInterface.common.enums.CheckEnum;
import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class TrustIdentityDTO {


    private String id;

    /**
     * 状态
     */
    private CheckEnum status;
}
