package cn.ruanyun.backInterface.modules.business.commodity.VO;

import lombok.Data;

import javax.persistence.Column;

@Data
public class CommodityDetailsVO {

    private String id;
    /**
     * 商品名称
     */
    private String commodityName;

    /**
     * 商品新价格
     */
    private String newprice;
    /**
     * 商品旧价格
     */
    private String oldPrice;

    /**
     * 商品轮播图
     */
    @Column(length = 1000)
    private String graphicDetails;

    /**
     * 商品详情
     */
    @Column(length = 1000)
    private String details;


}
