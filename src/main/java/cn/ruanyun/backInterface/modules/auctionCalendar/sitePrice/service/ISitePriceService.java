package cn.ruanyun.backInterface.modules.auctionCalendar.sitePrice.service;

import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.auctionCalendar.sitePrice.DTO.SitePriceDTO;
import cn.ruanyun.backInterface.modules.auctionCalendar.sitePrice.VO.SitePriceVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.auctionCalendar.sitePrice.pojo.SitePrice;

import java.util.List;

/**
 * 设置场地档期价格接口
 * @author z
 */
public interface ISitePriceService extends IService<SitePrice> {


      /**
        * 插入或者更新sitePrice
        * @param sitePrice
       */
     void insertOrderUpdateSitePrice(SitePrice sitePrice);



      /**
       * 移除sitePrice
       */
     void removeSitePrice(String siteId,String scheduleTime);


    /**
     * 后端查询档期价格
     */
    List<SitePriceVO> getSitePrice(SitePriceDTO sitePriceDTO);
}