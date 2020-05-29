package cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.pojo.SiteAuctionCalendar;
import cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.service.ISiteAuctionCalendarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author fei
 * 场地档期管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/siteAuctionCalendar")
@Transactional
public class SiteAuctionCalendarController {

    @Autowired
    private ISiteAuctionCalendarService iSiteAuctionCalendarService;


   /**
     * 更新或者插入数据
     * @param siteAuctionCalendar
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateSiteAuctionCalendar")
    public Result<Object> insertOrderUpdateSiteAuctionCalendar(SiteAuctionCalendar siteAuctionCalendar){

        try {

            iSiteAuctionCalendarService.insertOrderUpdateSiteAuctionCalendar(siteAuctionCalendar);
            return new ResultUtil<>().setSuccessMsg("插入或者更新成功!");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 移除数据
     * @param ids
     * @return
    */
    @PostMapping(value = "/removeSiteAuctionCalendar")
    public Result<Object> removeSiteAuctionCalendar(String ids){

        try {

            iSiteAuctionCalendarService.removeSiteAuctionCalendar(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

}
