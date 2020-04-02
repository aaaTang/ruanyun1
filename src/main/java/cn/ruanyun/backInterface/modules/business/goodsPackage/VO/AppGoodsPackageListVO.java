package cn.ruanyun.backInterface.modules.business.goodsPackage.VO;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;

@Data
@Accessors(chain = true)
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
    @Column(length = 1000)
    private String pics;

}
