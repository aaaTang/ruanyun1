package cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.service;

import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.vo.SiteAuctionCalendarVo;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
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
     * @param siteAuctionCalendar siteAuctionCalendar
     */
    void insertOrderUpdateSiteAuctionCalendar(SiteAuctionCalendar siteAuctionCalendar);


    /**
     * 移除siteAuctionCalendar
     * @param ids ids
     */
    void removeSiteAuctionCalendar(String ids);


    /**
     * 获取该场地没有档期的时间
     * @param siteId 场地id
     * @return SiteAuctionCalendarVo
     */
    Result<List<SiteAuctionCalendarVo>> getSiteNoAuctionCalendar(String siteId);

}