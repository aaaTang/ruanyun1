package cn.ruanyun.backInterface.modules.business.myFavorite.VO;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
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
    private BigDecimal newPrice;

    /**
     * 商品的ids
     */
    private String goodIds;

    /**
     * 店铺名称
     */
    private String shopName;
}
