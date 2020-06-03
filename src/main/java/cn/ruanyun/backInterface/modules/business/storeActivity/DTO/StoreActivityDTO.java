package cn.ruanyun.backInterface.modules.business.storeActivity.DTO;

import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class StoreActivityDTO {

    private String id;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 活动名称
     */
    private String title;

}
