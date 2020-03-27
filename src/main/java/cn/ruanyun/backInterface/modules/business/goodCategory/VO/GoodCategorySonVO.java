package cn.ruanyun.backInterface.modules.business.goodCategory.VO;

import lombok.Data;

/**
 * @program: xboot-plus
 * @description: 二级分类集合
 * @author: fei
 * @create: 2020-02-10 15:15
 **/
@Data
public class GoodCategorySonVO {


    /**
     * 分类id
     */
    private String id;

    /**
     * 分类图片
     */
    private String pic;

    /**
     * 分类名称
     */
    private String title;
}
