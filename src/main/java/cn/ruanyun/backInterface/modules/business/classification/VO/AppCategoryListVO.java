package cn.ruanyun.backInterface.modules.business.classification.VO;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class AppCategoryListVO {

    private String id;

    /**
     * 分类名称
     */
    private String title;

    /**
     * 一級分类图片
     */
    private String pic;

    /**
     * 二级分类集合
     */
    private List<AppCategoryVO> categoryVOS;

}
