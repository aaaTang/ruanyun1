package cn.ruanyun.backInterface.modules.business.shopWorks.DTO;

import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class ShopWorksDTO {

    private String id;


    /**
     * 创建者
     */
    private String createBy;

    /**
     * 是否删除
     */
    private Integer delFlag;



}
