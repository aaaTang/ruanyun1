package cn.ruanyun.backInterface.modules.business.itemAttrKey.VO;

import cn.ruanyun.backInterface.modules.business.itemAttrVal.vo.WebItemAttrValVo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class WebItemAttrKeyVO {

    private String id;

    /**
     * 商品属性名称
     */
    private String title;

    /**
     * 是否父级
     */
    private Boolean isParent = true;
    /**
     * 分类id
     */
    private String classId;
    /**
     * 规格属性
     */
    private List<WebItemAttrValVo> children;
}
