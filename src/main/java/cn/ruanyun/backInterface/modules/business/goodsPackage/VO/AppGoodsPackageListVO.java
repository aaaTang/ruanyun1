package cn.ruanyun.backInterface.modules.business.goodsPackage.VO;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class AppGoodsPackageListVO {

    /**
     * 套餐id
     */
    private String id;
    /**
     * 店铺名称
     */
    private String nickName;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品价格
     */
    private BigDecimal newPrice;
    /**
     * 旧价格
     */
    private BigDecimal oldPrice;
    /**
     * 套餐图片
     */
    @Column(length = 1000)
    private String pics;

}
