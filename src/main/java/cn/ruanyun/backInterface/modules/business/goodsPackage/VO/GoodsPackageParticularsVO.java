package cn.ruanyun.backInterface.modules.business.goodsPackage.VO;

import cn.ruanyun.backInterface.modules.business.bookingOrder.vo.WhetherBookingOrderVO;
import cn.ruanyun.backInterface.modules.business.goodsIntroduce.VO.GoodsIntroduceVO;
import cn.ruanyun.backInterface.modules.business.storeAudit.vo.StoreAuditListVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class GoodsPackageParticularsVO {

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
     * 商品视频
     */
    private String goodVideo;
    /**
     * 商品视频展示图
     */
    private String goodVideoPic;
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
    private List<GoodsIntroduceVO> productsIntroduction;

    /**
     * 购买须知
     */
    private List<GoodsIntroduceVO> purchaseNotes;

    /**
     * 是否预约
     */
    private WhetherBookingOrderVO whetherBookingOrder;
    /**
     * 是否收藏此套餐
     */
    private Integer myFavorite;

    //商铺信息
    private StoreAuditListVo storeAuditVO;

    @ApiModelProperty(value = "购买状态 1购买 2租赁 3购买和租赁")
    private Integer buyState;

    @ApiModelProperty(value = "租赁状态 1尾款线上支付  2尾款线下支付 ")
    private Integer leaseState;

    /**
     * 是否是四大金刚  0否   1是
     */
    private Integer devarajas;
}
