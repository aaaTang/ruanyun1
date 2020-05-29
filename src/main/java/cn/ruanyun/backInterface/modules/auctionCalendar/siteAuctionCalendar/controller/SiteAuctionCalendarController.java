package cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.pojo.SiteAuctionCalendar;
import cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.service.ISiteAuctionCalendarService;
import cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.vo.SiteAuctionCalendarVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "场所档期管理接口")
public class SiteAuctionCalendarController {

    @Autowired
    private ISiteAuctionCalendarService iSiteAuctionCalendarService;


    @PostMapping(value = "/insertOrderUpdateSiteAuctionCalendar")
    @ApiOperation("插入或者更新档期")
    public Result<Object> insertOrderUpdateSiteAuctionCalendar(SiteAuctionCalendar siteAuctionCalendar){

        try {

            iSiteAuctionCalendarService.insertOrderUpdateSiteAuctionCalendar(siteAuctionCalendar);
            return new ResultUtil<>().setSuccessMsg("插入或者更新成功!");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    @PostMapping(value = "/removeSiteAuctionCalendar")
    @ApiOperation("移除档期")
    public Result<Object> removeSiteAuctionCalendar(String ids){

        try {

            iSiteAuctionCalendarService.removeSiteAuctionCalendar(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

    @PostMapping("/getSiteNoAuctionCalendar")
    @ApiOperation("获取该场地没有档期的时间")
    @ApiImplicitParams(@ApiImplicitParam(name = "siteId", value = "场所id", dataType = "string", paramType = "query"))
    public Result<List<SiteAuctionCalendarVo>> getSiteNoAuctionCalendar(String siteId) {

        return iSiteAuctionCalendarService.getSiteNoAuctionCalendar(siteId);
    }
}
