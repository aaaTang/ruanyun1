package cn.ruanyun.backInterface.modules.business.itemAttrVal.VO;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ItemAttrValVO {

    private String id;
    /**
     * 规格属性名称
     */
    private String attrValue;

}
