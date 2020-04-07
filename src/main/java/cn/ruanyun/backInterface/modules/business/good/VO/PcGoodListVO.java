package cn.ruanyun.backInterface.modules.business.good.VO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PcGoodListVO {


    private  String id;
    /**
     * 分类名称
     */
    private String goodCategoryName;


    /**
     * 商品名称
     */
    private String goodName;


    /**
     * 商品图片
     */
    private String goodPics;


    /**
     * 商品详情
     */
    private String goodDetails;


    /**
     * 商品旧价格
     */
    private BigDecimal goodOldPrice;


    /**
     * 商品新价格
     */
    private BigDecimal goodNewPrice;


    /**
     * 颜色数据
     */
    private String colorIds;


    /**
     * 尺寸信息
     */
    private String sizeIds;


    /**
     * 商品库存
     */
    private Integer inventory;


    /**
     * 积分
     */
    private Integer integral;
}
