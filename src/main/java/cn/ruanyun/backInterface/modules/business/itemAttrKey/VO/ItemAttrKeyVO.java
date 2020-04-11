package cn.ruanyun.backInterface.modules.business.itemAttrKey.VO;

import cn.ruanyun.backInterface.modules.business.itemAttrVal.VO.ItemAttrValVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ItemAttrKeyVO {

    private String id;

    /**
     * 商品属性名称
     */
    private String attrName;

    /**
     * 规格属性
     */
    private List<ItemAttrValVO> val;


}
