package cn.ruanyun.backInterface.modules.business.myCollect.VO;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class MyCollectListVO {

    /**
     * 收藏id
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
