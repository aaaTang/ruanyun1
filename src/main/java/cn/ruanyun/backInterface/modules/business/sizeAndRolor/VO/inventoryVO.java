package cn.ruanyun.backInterface.modules.business.sizeAndRolor.VO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class inventoryVO {



    /**
     * 商品数量
     */
    private Integer inventory;

    /**
     *  商品价格
     */
    private BigDecimal goodsPrice;

    /**
     *  商品图片
     */
    private String pic;

}
