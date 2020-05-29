package cn.ruanyun.backInterface.modules.business.orderAfterSale.VO;

import cn.ruanyun.backInterface.modules.business.orderAfterSale.pojo.OrderAfterSale;
import cn.ruanyun.backInterface.modules.business.orderDetail.pojo.OrderDetail;
import cn.ruanyun.backInterface.modules.business.orderDetail.vo.OrderDetailVo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class OrderAfterSaleVO extends OrderAfterSale {

    /**
     * 购买的商品信息
     */
    private OrderDetailVo orderDetails;


}
