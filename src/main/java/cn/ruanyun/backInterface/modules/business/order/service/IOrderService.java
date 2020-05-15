package cn.ruanyun.backInterface.modules.business.order.service;

import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.order.DTO.OrderDTO;
import cn.ruanyun.backInterface.modules.business.order.DTO.OrderShowDTO;
import cn.ruanyun.backInterface.modules.business.order.DTO.PcOrderDTO;
import cn.ruanyun.backInterface.modules.business.order.VO.MyOrderVO;
import cn.ruanyun.backInterface.modules.business.order.VO.OrderDetailVO;
import cn.ruanyun.backInterface.modules.business.order.VO.OrderListVO;
import cn.ruanyun.backInterface.modules.business.order.VO.ShowOrderVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;

import javax.servlet.http.HttpServletRequest;
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
     * @param ids
     * @param payTypeEnum
     */
    Result<Object> payOrder(String ids, PayTypeEnum payTypeEnum, String payPassword);

    /**
     * 移除order
     *
     * @param ids
     */
    void removeOrder(String ids);

    /**
     * 移除order
     *
     * @param orderShowDTO
     */
    ShowOrderVO showOrder(OrderShowDTO orderShowDTO);

    /**
     * 下单之前获取订单的信息  直接购买套餐商品
     * @param orderShowDTO
     * @return
     */
    Object showGoodsPackageOrder(OrderShowDTO orderShowDTO);

    /**
     * 获取 我的订单
     * @param order
     * @return
     */
    List<OrderListVO> getOrderList(Order order);

    /**
     * 获取订单详情
     * @param id
     * @return
     */
    Object getAppGoodDetail(String id);

    /**
     * 改变订单状态  取消订单 确认收货
     * @param order
     * @return
     */
    Object changeStatus(Order order);


    /**
     * 确认收货
     * @param orderId 订单id
     * @return Result<Object>
     */
    Result<Object> confirmReceive(String orderId);


    /**
     * 微信回调
     * @param request
     * @return
     */
    String wxPayNotify(HttpServletRequest request);

    /**
     * 支付宝回调
     * @param request
     * @return
     */
    String aliPayNotify(HttpServletRequest request);


    /**
     * 根据商家id查询订单列表
     * @param storeId 商家id
     * @return  Order
     */
    List<Order> getOrderListByStoreId(String storeId);

    /**
     *后端获取订单信息列表
     */
    List PCgetShopOrderList(PcOrderDTO pcOrderDTO);


}