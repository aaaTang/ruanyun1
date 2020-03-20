package cn.ruanyun.backInterface.modules.business.goodsPackage.VO;

import lombok.Data;

@Data
public class AppGoodsPackageListVO {

    /**
     * 商品id
     */
    private String id;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品价格
     */
    private String newPrice;
    /**
     * 套餐图片
     */
    private String pics;

}
