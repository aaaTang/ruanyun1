package cn.ruanyun.backInterface.modules.business.goodCategory.VO;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @program: xboot-plus
 * @description: app分类列表
 * @author: fei
 * @create: 2020-02-10 15:14
 **/
@Data
@Accessors(chain = true)
public class GoodCategoryListVO {

    /**
     * 父级id
     */
    private String parentId;

    /**
     * 父级分类名称
     */
    private String parentName;

    /**
     * 子集分类列表
     */
    private List<GoodCategorySonVO> goodCategorySonVOS;
}
