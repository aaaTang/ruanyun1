package cn.ruanyun.backInterface.modules.business.good.VO;

import cn.ruanyun.backInterface.modules.business.goodsIntroduce.VO.GoodsIntroduceVO;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ActivityPackageVO {

    private String id;
    /**
     * 商品名称
     */
    private String goodName;
    /**
     * 套餐图片
     */
    @Column(length = 1000)
    private String pics;
    /**
     * 商品旧价格
     */
    private BigDecimal goodOldPrice;
    /**
     * 商品新价格
     */
    private BigDecimal goodNewPrice;

    /**
     * 商品介绍
     */
    private List productsIntroduction;

    /**
     * 购买须知
     */
    private List purchaseNotes;
}
