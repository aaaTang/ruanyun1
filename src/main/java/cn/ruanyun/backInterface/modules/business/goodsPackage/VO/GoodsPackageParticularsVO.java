package cn.ruanyun.backInterface.modules.business.goodsPackage.VO;

import cn.ruanyun.backInterface.modules.business.goodsIntroduce.VO.GoodsIntroduceVO;
import cn.ruanyun.backInterface.modules.business.storeAudit.VO.StoreAuditListVO;
import cn.ruanyun.backInterface.modules.business.storeAudit.VO.StoreAuditVO;
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
    private String goodsName;
    /**
     * 套餐图片
     */
    @Column(length = 1000)
    private String pics;

    /**
     * 新价格
     */
    private BigDecimal newPrice;

    /**
     * 旧价格
     */
    private BigDecimal oldPrice;

    /**
     * 商品介绍
     */
    private List<GoodsIntroduceVO> productsIntroduction;

    /**
     * 购买须知
     */
    private List<GoodsIntroduceVO> purchaseNotes;

    /**
     * 是否收藏此套餐
     */
    private Integer myFavorite;

    //商铺信息
    private StoreAuditListVO storeAuditVO;

    @ApiModelProperty(value = "购买状态 1购买 2租赁")
    private Integer buyState;

    @ApiModelProperty(value = "租赁状态 1尾款线上支付  2尾款线下支付 ")
    private Integer leaseState;
}
