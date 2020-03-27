package cn.ruanyun.backInterface.modules.business.order.service;


import cn.ruanyun.backInterface.modules.business.comment.entity.Comment;
import cn.ruanyun.backInterface.modules.business.order.DTO.OrderDTO;
import cn.ruanyun.backInterface.modules.business.order.VO.BackOrderListVO;
import cn.ruanyun.backInterface.modules.business.order.VO.OrderDetailVO;
import cn.ruanyun.backInterface.modules.business.order.VO.OrderListVO;
import cn.ruanyun.backInterface.modules.business.order.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 订单接口
 * @author fei
 */
public interface IOrderService extends IService<Order> {


    /**
     * 插入订单*
     * @param order
     * @return
     */
    Order insertOrder(Order order);


    /**
     * 发货
     * @param orderDTO
     */
    void sendGood(OrderDTO orderDTO);

    /**
     * 确认收获
     * @param id
     */
    void confirmOrder(String id);

    /**
     * 评价订单
     * @param comment
     */
    void addComment(Comment comment);


    /**
     * app获取我的订单列表
     * @param orderDTO
     * @return
     */
    List<OrderListVO> myOrderList(OrderDTO orderDTO);

    /**
     * 后台获取订单列表
     * @param orderDTO
     * @return
     */
    List<BackOrderListVO> getBackOrderList(OrderDTO orderDTO);

    /**
     * 获取订单列表字段
     * @param id
     * @return
     */
    OrderListVO getOrderListVO(String id);

    /**
     * 获取订单详情字段
     * @param id
     * @return
     */
    OrderDetailVO getOrderDetailVO(String id);

    /**
     * 获取后台管理系统订单列表字段
     * @param id
     * @return
     */
    BackOrderListVO getBackOrderListVO(String id);


    /**
     * 获取商品的销量
     * @param goodId
     * @return
     */
    Integer getGoodSalesVolume(String goodId);


    /**
     * 获取商品总价格
     * @param order
     * @return
     */
    BigDecimal getGoodTotalMoney(Order order);


    /**
     * 小程序微信支付
     * @param order
     * @return
     */
    Map<String,String> wxAppletPayOrder(Order order, String ids);


    /**
     * 支付回调
     * @param orderNum
     */
    void orderNotify(String orderNum);


}