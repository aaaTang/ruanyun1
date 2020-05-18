package cn.ruanyun.backInterface.modules.business.order.VO;

import cn.ruanyun.backInterface.modules.business.good.VO.AppGoodOrderVO;
import cn.ruanyun.backInterface.modules.business.harvestAddress.VO.HarvestAddressVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GoodsPackageOrderVO {
    /**
     * 收货地址
     */
    private HarvestAddressVO harvestAddressVO;

    /**
     * 商品的信息
     */
    private List<AppGoodOrderVO> appGoodOrderVOS;

    /**
     * 商品的总金额
     */
    private BigDecimal sumMoney;

    /**
     * 运费
     */
    private BigDecimal freightMoney;

    /**
     * 积分
     */
    private Integer integral;

    /**
     * 优惠券id
     */
    private String discountCouponId;

    /**
     * 满多少
     */
    private BigDecimal fullMoney;
    /**
     * 减多少
     */
    private BigDecimal subtractMoney;

    /**
     * 商品描述
     */
    private String goodDesc;
}
