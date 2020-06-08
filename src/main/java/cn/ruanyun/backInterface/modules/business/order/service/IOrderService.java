package cn.ruanyun.backInterface.modules.business.order.service;

import cn.ruanyun.backInterface.common.enums.DayTimeTypeEnum;
import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.business.comment.DTO.CommentDTO;
import cn.ruanyun.backInterface.modules.business.order.dto.*;
import cn.ruanyun.backInterface.modules.business.order.vo.*;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * 订单接口
 *
 * @author fei
 */
public interface IOrderService extends IService<Order> {


    /*-----------------------------创建订单----------------------*/

    /**
     * 下单
     *
     * @param orderDTO orderDTO
     */
    Result<Object> insertOrder(OrderDto orderDTO);

    /**
     * 档期下单
     * @return Object
     */
    Result<Object> inserAuctionCalendartOrder(AuctionCalendartOrderDTO auctionCalendartOrderDTO);

    /**
     * 获取主持人的档期价格
     * @param goodIs 商品id
     * @param scheduleAppointment 档期时间
     * @param dayTimeTypeEnum  档期类型
     * @return BigDecimal
     */
    BigDecimal inserGoodOrder(String goodIs, String scheduleAppointment, DayTimeTypeEnum dayTimeTypeEnum);

    /**
     * 新增线下订单
     * @param offLineOrderDto  offLineOrderDto
     * @return Object
     */
    Result<Object> insertOffLineOrder(OffLineOrderDto offLineOrderDto);


    /**
     * 新增线下尾款支付订单
     * @param staffId 员工id
     * @param orderNum 订单编号
     * @return Object
     */
    Result<Object> insertOffLinePayTheBalanceOrder(String staffId, String orderNum);



    /*-----------------------------支付----------------------*/

    /**
     * 支付
     * @param appPayOrder 支付参数
     * @return Object
     */
    Result<Object> payOrder(AppPayOrderDto appPayOrder);


    /**
     * 根据订单类型计算实际支付价格
     * @param order order
     * @return BigDecimal
     */
    BigDecimal getTotalPriceByOrderType(Order order);


    /**
     * 微信回调
     * @param request request
     * @return String
     */
    String wxPayNotify(HttpServletRequest request);


    /**
     * 支付宝回调
     * @param request request
     * @return String
     */
    String aliPayNotify(HttpServletRequest request);


    /*-----------------------------订单操作----------------------*/


    /**
     * 订单发货
     * @param orderOperateDto 实体
     * @return Object
     */
    Result<Object> sendGood(OrderOperateDto orderOperateDto);


    /**
     * 确认收货
     * @param orderId 订单id
     * @return Result<Object>
     */
    Result<Object> confirmReceive(String orderId);


    /**
     * 支付尾款
     * @param orderOperateDto 实体
     * @return Object
     */
    Result<Object> payTheBalance(OrderOperateDto orderOperateDto);


    /**
     * 支付尾款逻辑
     * @param orderOperateDto 实体
     */
    void payTheBalanceCallBack(OrderOperateDto orderOperateDto);

    /**
     * 确认尾款
     * @param orderOperateDto 实体
     * @return Object
     */
    Result<Object> confirmTheBalance(OrderOperateDto orderOperateDto);


    /**
     *去评价订单
     * @param commentDTO 实体
     * @return Object
     */
    Result<Object> toEvaluate(CommentDTO commentDTO);


    /**
     * 取消订单
     * @param orderOperateDto 实体
     * @return Object
     */
    Result<Object> cancelOrder(OrderOperateDto orderOperateDto);



    /*-----------------------------查询订单----------------------*/

    /**
     * 获取我的订单列表
     * @param pageVo 分页参数
     * @return DataVo<AppMyOrderListVo
     */
    Result<DataVo<AppMyOrderListVo>> getMyOrderList(PageVo pageVo, OrderStatusEnum orderStatus);


    /**
     * 获取我的订单详情
     * @param id 订单id
     * @return AppMyOrderDetailVo
     */
    Result<AppMyOrderDetailVo> getMyOrderDetail(String id);


    /**
     * 获取后台订单列表
     * @param backOrderListDto 查询订单参数
     * @param pageVo 分页参数
     * @return 订单列表
     */
    Result<DataVo<BackOrderListVO>> getBackOrderList(BackOrderListDto backOrderListDto, PageVo pageVo);




    /*-----------------------------辅助类----------------------*/

    /**
     * 获取员工的销售额
     * @param staffId 员工id
     * @return BigDecimal
     */
    BigDecimal getStaffSaleAmount(String staffId);


    /**
     * 判断订单的冻结期
     * @param orderId 订单id
     * @return 是 否
     */
    Boolean judgeOrderFreeze(String orderId);


    /**
     * 获取该商家下的订单数据
     * @param storeId 商家id
     * @return Order
     */
    List<Order> getOrderListByStoreId(String storeId);
}