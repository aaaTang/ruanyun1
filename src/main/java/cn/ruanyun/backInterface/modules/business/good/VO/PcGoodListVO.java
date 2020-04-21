package cn.ruanyun.backInterface.modules.business.good.VO;

import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;

@Data
public class PcGoodListVO {


    private  String id;
    private GoodTypeEnum typeEnum;

    /**
     * 分类id
     */
    private String goodCategoryId;


    /**
     * 商品名称
     */
    private String goodName;


    /**
     * 商品图片
     */
    private String goodPics;


    /**
     * 商品详情
     */
    private String goodDetails;


    /**
     * 商品旧价格
     */
    private BigDecimal goodOldPrice;


    /**
     * 商品新价格
     */
    private BigDecimal goodNewPrice;

    /**
     * 积分
     */
    private Integer integral;

    /**
     * 商品介绍
     */
    private String productsIntroduction;


    /**
     * 商品亮点
     */
    private String productLightspot;


    /**
     * 拍摄特色
     */
    private String shootCharacteristics;

    /**
     * 图文详情
     */
    @Column(length = 1000)
    private String graphicDetails;


    /**
     * 购买须知
     */
    private String purchaseNotes;


    /**
     * 温馨提示
     */
    private String warmPrompt;

    /***
     * 分类名称
     */
    private String goodCategoryName;
}
