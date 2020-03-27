package cn.ruanyun.backInterface.modules.business.commodity.VO;

import cn.ruanyun.backInterface.modules.business.Specifications.SpecificationsVO.SpecificationsListVO;
import cn.ruanyun.backInterface.modules.business.discountCoupon.VO.DiscountCouponListVO;
import lombok.Data;

import javax.persistence.Column;
import java.util.List;

@Data
public class CommodityListVO {

    private String id;
    /**
     * 商品名称
     */
    private String commodityName;

    /**
     * 商品满减
     */
    private List<DiscountCouponListVO> discountCouponList;
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
     * 商品规格
     */
    private List<SpecificationsListVO> Specifications;

    /**
     * 商品详情
     */
    @Column(length = 1000)
    private String details;

    /**
     * 分类id
     */
    private String classificationId;
//    /**
//     * 销售量
//     */
//    private Integer salesVolume;
}
