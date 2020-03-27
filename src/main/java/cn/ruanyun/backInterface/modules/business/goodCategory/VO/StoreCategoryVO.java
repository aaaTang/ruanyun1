package cn.ruanyun.backInterface.modules.business.goodCategory.VO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: xboot-plus
 * @description: 店铺分类数据
 * @author: fei
 * @create: 2020-02-12 15:54
 **/
@Data
@Accessors(chain = true)
public class StoreCategoryVO {

    /**
     * 分类id
     */
    private String id;

    /**
     * 分类名字
     */
    private String title;

}
