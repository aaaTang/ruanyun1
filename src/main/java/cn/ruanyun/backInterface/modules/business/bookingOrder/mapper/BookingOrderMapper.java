package cn.ruanyun.backInterface.modules.business.bookingOrder.mapper;

import cn.ruanyun.backInterface.modules.business.bookingOrder.VO.BookingOrderVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.ruanyun.backInterface.modules.business.bookingOrder.pojo.BookingOrder;

/**
 * 预约订单数据处理层
 * @author fei
 */
public interface BookingOrderMapper extends BaseMapper<BookingOrder> {

    BookingOrderVO bookingOrderList (String ids,String userId);
}