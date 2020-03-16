package cn.ruanyun.backInterface.modules.business.storeAudit.DTO;

import cn.ruanyun.backInterface.common.enums.CheckEnum;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 审核输入层
 */
@Data
@Accessors(chain = true)
public class StoreAuditDTO {

    private String id;


    /**
     * 审核状态
     */
    private CheckEnum checkEnum;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户手机号
     */
    private String mobile;


    /**
     * 审核意见
     */
    private String checkAdvice;

}
