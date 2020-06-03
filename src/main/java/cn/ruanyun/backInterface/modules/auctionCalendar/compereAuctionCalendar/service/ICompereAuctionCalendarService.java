package cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.service;

import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.VO.CompereAuctionCalendarVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.pojo.CompereAuctionCalendar;

import java.util.List;

/**
 * 主持人没有档期的时间接口
 * @author z
 */
public interface ICompereAuctionCalendarService extends IService<CompereAuctionCalendar> {


      /**
        * 插入或者更新compereAuctionCalendar
        * @param compereAuctionCalendar
       */
      Result<Object> insertOrderUpdateCompereAuctionCalendar(CompereAuctionCalendar compereAuctionCalendar);



      /**
       * 移除compereAuctionCalendar
       * @param ids
       */
     void removeCompereAuctionCalendar(String ids);




    Result<List<CompereAuctionCalendarVO>> AppGetCompereNoAuctionCalendar(String id);
}