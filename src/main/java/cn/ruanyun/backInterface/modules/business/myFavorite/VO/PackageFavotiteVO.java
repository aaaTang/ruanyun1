package cn.ruanyun.backInterface.modules.business.myFavorite.VO;

import lombok.Data;

import javax.persistence.Column;

@Data
public class PackageFavotiteVO {

    private String id;
    /**
     * 套餐名称
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
     * 商品的ids
     */
    private String goodIds;

}
