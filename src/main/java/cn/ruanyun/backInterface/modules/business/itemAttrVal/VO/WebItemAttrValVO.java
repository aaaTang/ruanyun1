package cn.ruanyun.backInterface.modules.business.itemAttrVal.VO;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WebItemAttrValVO {

    private String id;
    /**
     * 规格属性名称
     */
    private String title;

    /**
     * 是否父级
     */
    private Boolean isParent = false;
}
