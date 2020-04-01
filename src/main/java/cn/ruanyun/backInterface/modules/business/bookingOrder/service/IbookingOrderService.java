package cn.ruanyun.backInterface.modules.business.bookingOrder.service;

import cn.ruanyun.backInterface.modules.business.bookingOrder.VO.BookingOrderVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.bookingOrder.pojo.bookingOrder;

import java.util.List;

/**
 * 预约订单接口
 * @author fei
 */
public interface IbookingOrderService extends IService<bookingOrder> {


      /**
        * 插入或者更新bookingOrder
        * @param bookingOrder
       */
     void insertOrderUpdatebookingOrder(bookingOrder bookingOrder);



      /**
       * 移除bookingOrder
       * @param ids
       */
     void removebookingOrder(String ids);

    /**
     * 获取预约订单列表
     */
     List<BookingOrderVO> bookingOrderList(String classId);
}