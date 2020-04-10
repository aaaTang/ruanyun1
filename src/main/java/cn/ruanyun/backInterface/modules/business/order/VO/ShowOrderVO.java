package cn.ruanyun.backInterface.modules.business.order.VO;

import cn.ruanyun.backInterface.modules.business.good.VO.AppGoodInfoVO;
import cn.ruanyun.backInterface.modules.business.good.VO.AppGoodOrderVO;
import cn.ruanyun.backInterface.modules.business.harvestAddress.VO.HarvestAddressVO;
import cn.ruanyun.backInterface.modules.business.shoppingCart.VO.ShoppingCartVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class ShowOrderVO {

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

    //确认下单的时候需要传的商品的数据
    private  String goods;

}
