package cn.ruanyun.backInterface.modules.auctionCalendar.auctionCalendarOrder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.auctionCalendar.auctionCalendarOrder.pojo.AuctionCalendarOrder;

import java.util.List;

/**
 * 档期订单接口
 * @author fei
 */
public interface IAuctionCalendarOrderService extends IService<AuctionCalendarOrder> {


      /**
        * 插入或者更新auctionCalendarOrder
        * @param auctionCalendarOrder
       */
     void insertOrderUpdateAuctionCalendarOrder(AuctionCalendarOrder auctionCalendarOrder);



      /**
       * 移除auctionCalendarOrder
       * @param ids
       */
     void removeAuctionCalendarOrder(String ids);
}