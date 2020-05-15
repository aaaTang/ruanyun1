package cn.ruanyun.backInterface.modules.business.goodCategory.VO;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.annotation.Resource;
import javax.persistence.Column;

@Data
@Accessors(chain = true)
public class CategoryShopVO {


    private String id;
    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 轮播图
     */
    @Column(length = 1000)
    private String  pic ;

    /**
     * 评论数
     */
    private Integer commentNum;

    /**
     * 评分
     */
    private Double grade;
}
