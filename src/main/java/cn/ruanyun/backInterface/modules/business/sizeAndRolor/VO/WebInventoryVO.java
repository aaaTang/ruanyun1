package cn.ruanyun.backInterface.modules.business.sizeAndRolor.VO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WebInventoryVO {


    private String id;
    /**
     * 商品id
     */
    private String goodsId;

    /**
     * 规格和规格属性id
     */
    private String attrSymbolPath;

    /**
     * 规格属性名称
     */
    private String attrSymbolPathName;

    /**
     *  商品价格
     */
    private BigDecimal goodPrice;

    /**
     * 商品数量
     */
    private Integer inventory;

    /**
     *图片
     */
    private String pic;

    /**
     *  商品定金
     */
    private BigDecimal goodDeposit;

    /**
     *  商品尾款
     */
    private BigDecimal goodDalancePayment;
}
