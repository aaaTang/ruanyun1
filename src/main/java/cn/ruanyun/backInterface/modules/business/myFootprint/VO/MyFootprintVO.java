package cn.ruanyun.backInterface.modules.business.myFootprint.VO;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class MyFootprintVO {

    /**
     * 足迹id
     */
    private  String id;

    /**
     * 商品id
     */
    private String goodsId;
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
