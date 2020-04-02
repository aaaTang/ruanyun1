package cn.ruanyun.backInterface.modules.business.order.service;

import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.order.DTO.OrderDTO;
import cn.ruanyun.backInterface.modules.business.order.DTO.OrderShowDTO;
import cn.ruanyun.backInterface.modules.business.order.VO.MyOrderVO;
import cn.ruanyun.backInterface.modules.business.order.VO.ShowOrderVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 订单接口
 *
 * @author fei
 */
public interface IOrderService extends IService<Order> {


    /**
     * 下单
     *
     * @param orderDTO
     */
    Result<Object> insertOrderUpdateOrder(OrderDTO orderDTO);

    /**
     * 支付
     *
     * @param id
     * @param payTypeEnum
     */
    Result<Object> payOrder(String id, PayTypeEnum payTypeEnum);

    /**
     * 移除order
     *
     * @param ids
     */
    void removeOrder(String ids);


    //1.后台店铺查询订单
//    List<ProductVO> getOrderList(PageVo pageVo);

    //3.我的订单（待付款，待发货，待收货，已完成）
    List<MyOrderVO> getMyOrderList(Order order, PageVo pageVo);

    //4.管理员看到每个店铺的订单数据

    //5.确认收货(处理个人积分)
    void confirmReceipt(String orderId);

    //6.发货
    void sendOrder(String orderId, String logistics);


    //7.判断所选优惠券是否可用
    Boolean judgeCouponCanUse(Order order);


    //8.商家查看待结算 和 已结算金额
    Map<String, BigDecimal> getStoreMoney(String userId);


    //9.修改当前订单结算状态
    void updateClearingOrder(String userId, Integer code);


    //支付时获取订单编号和需要支付总金额
    Map<String, Object> payInfo(Order order);


    //支付时插入订单
//    void insertPayOrder(String outTradeNo, BigDecimal totalFee, Order order, String key, PayType payType);

    //直接下单
//    boolean insertProduct(Order order, List<OrderDetail> orderDetail);

    Object getInfo(String productId);

    void insertOrderByShopping(OrderDTO orderDTO);

    ShowOrderVO showOrder(OrderShowDTO orderShowDTO);
}