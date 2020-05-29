package cn.ruanyun.backInterface.modules.business.itemAttrKey.VO;

import cn.ruanyun.backInterface.modules.business.itemAttrVal.vo.ItemAttrValVo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ItemAttrKeyVO {

    private String id;

    /**
     * 商品属性名称
     */
    private String attrName;

    /**
     * 规格属性
     */
    private List<ItemAttrValVo> val;


}
