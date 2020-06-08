package cn.ruanyun.backInterface.modules.business.good.VO;

import cn.ruanyun.backInterface.modules.business.goodService.GoodServerVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ActivityGoodVO {

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
     * 商品旧价格
     */
    private BigDecimal goodOldPrice;


    /**
     * 商品新价格
     */
    private BigDecimal goodNewPrice;


    /**
     * 商品详情
     */
    private String goodDetails;

    /**
     * 商品服务
     */
    private List<GoodServerVO> goodsService;

}
