package cn.ruanyun.backInterface.modules.business.bookingOrder.service;

import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.bookingOrder.dto.BookingDTO;
import cn.ruanyun.backInterface.modules.business.bookingOrder.vo.BackBookingOrderListVO;
import cn.ruanyun.backInterface.modules.business.bookingOrder.vo.BookingOrderVO;
import cn.ruanyun.backInterface.modules.business.bookingOrder.vo.WhetherBookingOrderVO;
import cn.ruanyun.backInterface.modules.business.bookingOrder.pojo.BookingOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 预约订单接口
 * @author fei
 */
public interface IBookingOrderService extends IService<BookingOrder> {


    /**
     * 插入或者更新bookingOrder
     * @param bookingOrder bookingOrder
     */
    Result<Object> insertOrderUpdatebookingOrder(BookingOrder bookingOrder);


    /**
     * 移除bookingOrder
     * @param ids ids
     */
    void removebookingOrder(String ids);


    /**
     * 后端商家处理预约
     * @return Object
     */
    Result<Object> checkBookingOrder(BookingDTO bookingDTO);


    /**
     * 获取预约订单列表
     */
    List<BookingOrderVO> bookingOrderList(String classId);


    /**
     * 查詢我是否预约这个店铺
     * @param storeId 商家id
     * @param userid 用户id
     * @return WhetherBookingOrderVO
     */
    WhetherBookingOrderVO  getWhetherBookingOrder(String storeId , String userid);


    /**
     * 后端获取商家预约订单列表
     * @return BackBookingOrderListVO
     */
    List<BackBookingOrderListVO> backBookingOrderList(BookingDTO bookingDTO);


    /**
     * 获取待审核的预约订单数量
     * @return Integer
     */
    Integer getPrePayBookingOrderListCount();

}