package cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.VO.CompereAuctionCalendarVO;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.pojo.CompereAuctionCalendar;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.service.ICompereAuctionCalendarService;
import cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.vo.SiteAuctionCalendarVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author z
 * 主持人没有档期的时间管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/compereAuctionCalendar")
@Transactional
public class CompereAuctionCalendarController {

    @Autowired
    private ICompereAuctionCalendarService iCompereAuctionCalendarService;


   /**
     * 更新或者插入数据
     * @param compereAuctionCalendar
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateCompereAuctionCalendar")
    public Result<Object> insertOrderUpdateCompereAuctionCalendar(CompereAuctionCalendar compereAuctionCalendar){

        return iCompereAuctionCalendarService.insertOrderUpdateCompereAuctionCalendar(compereAuctionCalendar);
    }


    /**
     * 移除数据
     * @param ids
     * @return
    */
    @PostMapping(value = "/removeCompereAuctionCalendar")
    public Result<Object> removeCompereAuctionCalendar(String ids){

        try {

            iCompereAuctionCalendarService.removeCompereAuctionCalendar(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    @PostMapping("/AppGetCompereNoAuctionCalendar")
    @ApiOperation("获取该主持人没有档期的时间")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "主持人id", dataType = "string", paramType = "query"))
    public Result<List<CompereAuctionCalendarVO>> AppGetCompereNoAuctionCalendar(String id) {

        return iCompereAuctionCalendarService.AppGetCompereNoAuctionCalendar(id);
    }



}
