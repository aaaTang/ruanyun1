package cn.ruanyun.backInterface.modules.business.good.VO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AppForSaleGoodsVO {

    private  String id;

    /**
     * 商品名称
     */
    private String goodName;

    /**
     * 商品图片
     */
    private String goodPics;

    /**
     * 商品旧价格
     */
    private BigDecimal goodOldPrice;


    /**
     * 商品新价格
     */
    private BigDecimal goodNewPrice;
}
