package cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.pojo.SiteAuctionCalendar;

import java.util.List;

/**
 * 场地档期接口
 * @author fei
 */
public interface ISiteAuctionCalendarService extends IService<SiteAuctionCalendar> {


      /**
        * 插入或者更新siteAuctionCalendar
        * @param siteAuctionCalendar
       */
     void insertOrderUpdateSiteAuctionCalendar(SiteAuctionCalendar siteAuctionCalendar);



      /**
       * 移除siteAuctionCalendar
       * @param ids
       */
     void removeSiteAuctionCalendar(String ids);
}