package cn.ruanyun.backInterface.modules.business.bookingOrder.service;

import cn.ruanyun.backInterface.modules.business.bookingOrder.VO.BookingOrderVO;
import cn.ruanyun.backInterface.modules.business.bookingOrder.VO.WhetherBookingOrderVO;
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
        * @param bookingOrder
       */
     void insertOrderUpdatebookingOrder(BookingOrder bookingOrder);



      /**
       * 移除bookingOrder
       * @param ids
       */
     void removebookingOrder(String ids);

    /**
     * 获取预约订单列表
     */
     List<BookingOrderVO> bookingOrderList(String classId);

    /**
     * 按店铺id和用户id查询数据
     * @param storeId
     * @param userid
     * @return
     */
     WhetherBookingOrderVO  getWhetherBookingOrder(String storeId , String userid);
}