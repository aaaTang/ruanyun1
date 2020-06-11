package cn.ruanyun.backInterface.modules.business.recommendedPackage.VO;

import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;

@Data
public class RecommendedListVO {

    private String id;

    private GoodTypeEnum typeEnum;

    /**
     * 商品名称
     */
    private String goodName;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 用户头像
     */
    @Column(length = 1000)
    private String avatar;

    /**
     * 商品图片
     */
    private String goodPic;


    /**
     * 商品新价格
     */
    private BigDecimal goodNewPrice;


    /**
     * 评分
     */
    private Double grade;


    /**
     * 评论数
     */
    private Integer commentNum;


    /**
     * 销量
     */
    private Integer SaleVolume = 0;
}
