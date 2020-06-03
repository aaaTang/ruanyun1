package cn.ruanyun.backInterface.modules.business.goodsPackage.VO;

import cn.ruanyun.backInterface.modules.business.bookingOrder.VO.WhetherBookingOrderVO;
import cn.ruanyun.backInterface.modules.business.discountCoupon.VO.DiscountCouponListVO;
import cn.ruanyun.backInterface.modules.business.storeActivity.VO.StoreActivityVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import java.util.List;


@Data
@Accessors(chain = true)
public class ShopParticularsVO {

    private String id;

    /**
     * 地址
     */
    private String address;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 距离
     */
    private String distance;

    /**
     * 轮播图
     */
    @Column(length = 1000)
    private String  pic ;

    /**
     * 商品视频
     */
    private String goodVideo;

    /**
     * 商品视频展示图
     */
    private String goodVideoPic;

    /**
     * 店铺评分
     */
    private String score;

    /**
     * 店铺优惠券
     */
    private List<DiscountCouponListVO> discountList;

    /**
     *  用户评论
     */
    private List userComment;

    /**
     * 店铺收藏
     */
    private Integer favroite;

    /**
     * 是否预约
     */
    private WhetherBookingOrderVO whetherBookingOrder;

    @ApiModelProperty("店铺营业时间")
    private String openTime;

    @ApiModelProperty("优质服务")
    private List<String> firstRateService;

    @ApiModelProperty("个人简介")
    @Column(length = 500)
    private String individualResume;

    @ApiModelProperty("门店活动")
    private List<StoreActivityVO> activity;

    @ApiModelProperty(value = "到店礼")
    @Column(length = 1000)
    private String toStoreGift;
}
