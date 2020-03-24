package cn.ruanyun.backInterface.modules.business.goodsPackage.VO;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;

@Data
@Accessors(chain = true)
public class GoodsPackageParticularsVO {

    private String id;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 套餐图片
     */
    @Column(length = 1000)
    private String pics;

    /**
     * 新价格
     */
    private String newPrice;

    /**
     * 旧价格
     */
    private String oldPrice;


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
}
