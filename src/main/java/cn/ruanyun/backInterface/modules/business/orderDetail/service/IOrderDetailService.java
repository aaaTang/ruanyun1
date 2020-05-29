package cn.ruanyun.backInterface.modules.business.orderDetail.service;

import cn.ruanyun.backInterface.modules.business.orderDetail.vo.OrderDetailVo;
import cn.ruanyun.backInterface.modules.business.orderDetail.pojo.OrderDetail;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 子订单接口
 * @author wj
 */
public interface IOrderDetailService extends IService<OrderDetail> {


    /**
     * 获取销量
     *
     * @param id id
     * @return Integer
     */
    Integer getGoodSalesVolume(String id);


    /**
     * 通过订单id获取订单商品详情
     * @param orderId 订单id
     * @return OrderDetailVo
     */
    OrderDetailVo getOrderDetailByOrderId(String orderId);

}

