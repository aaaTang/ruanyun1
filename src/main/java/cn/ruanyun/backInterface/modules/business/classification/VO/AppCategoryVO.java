package cn.ruanyun.backInterface.modules.business.classification.VO;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AppCategoryVO {


    private String id;

    /**
     * 分类名称
     */
    private String title;


    /**
     * 图片
     */
    private String pic;
}
