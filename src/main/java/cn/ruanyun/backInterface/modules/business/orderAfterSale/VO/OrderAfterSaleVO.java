package cn.ruanyun.backInterface.modules.business.orderAfterSale.VO;

import cn.ruanyun.backInterface.modules.business.orderAfterSale.pojo.OrderAfterSale;
import cn.ruanyun.backInterface.modules.business.orderDetail.VO.OrderDetailListVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class OrderAfterSaleVO extends OrderAfterSale {

    /**
     * 退款状态
     */
    private int statusCode;

    private int typeCode;

    /**
     * 购买的商品信息
     */
    private List<OrderDetailListVO> orderDetails;


}
