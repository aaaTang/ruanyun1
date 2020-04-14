package cn.ruanyun.backInterface.modules.business.myFavorite.VO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsFavoriteVO {

    private String id;


    /**
     * 商品名称
     */
    private String goodName;


    /**
     * 商品图片
     */
    private String goodPics;


    /**
     * 商品新价格
     */
    private BigDecimal goodNewPrice;
}
