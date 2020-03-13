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
     * 审核意见
     */
    private String checkAdvice;

}
