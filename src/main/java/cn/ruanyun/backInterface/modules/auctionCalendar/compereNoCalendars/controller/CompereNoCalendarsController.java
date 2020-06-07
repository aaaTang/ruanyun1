package cn.ruanyun.backInterface.modules.auctionCalendar.compereNoCalendars.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.DTO.CompereAuctionCalendarDTO;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereNoCalendars.DTO.CompereNoCalendarsDTO;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereNoCalendars.VO.CompereNoCalendarsVO;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereNoCalendars.pojo.CompereNoCalendars;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereNoCalendars.service.ICompereNoCalendarsService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author z
 * 设置主持人没有档期的时间管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/compereNoCalendars")
@Transactional
public class CompereNoCalendarsController {

    @Autowired
    private ICompereNoCalendarsService iCompereNoCalendarsService;


   /**
     * 更新或者插入数据
     * @param compereNoCalendars
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateCompereNoCalendars")
    public Result<Object> insertOrderUpdateCompereNoCalendars(CompereNoCalendars compereNoCalendars){

        try {

            iCompereNoCalendarsService.insertOrderUpdateCompereNoCalendars(compereNoCalendars);
            return new ResultUtil<>().setSuccessMsg("插入或者更新成功!");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 移除数据
     * @return
    */
    @PostMapping(value = "/removeCompereNoCalendars")
    public Result<Object> removeCompereNoCalendars(CompereNoCalendars compereNoCalendars){

        try {

            iCompereNoCalendarsService.removeCompereNoCalendars(compereNoCalendars);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }



    /**
     * 后端获取主持人没有档期的列表
     * @param pageVo
     * @param compereNoCalendarsDTO
     * @return
     */
    @PostMapping(value = "/PcGetCompereNoCalendars")
    public Result<Object> PcGetCompereNoCalendars(PageVo pageVo, CompereNoCalendarsDTO compereNoCalendarsDTO){

        return Optional.ofNullable(iCompereNoCalendarsService.PcGetCompereNoCalendars(compereNoCalendarsDTO))
                .map(compereAuctionCalendarVOList->{
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",compereAuctionCalendarVOList.size());
                    result.put("data", PageUtil.listToPage(pageVo,compereAuctionCalendarVOList));

                    return new ResultUtil<>().setData(result,"后端获取主持人没有档期的列表成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据"));
    }


    /**
     * 获取主持人商品或者套餐已经被购买的档期列表
     * @return
     */
    @PostMapping(value = "/AppGetCompereNoCalendars")
    public Result<Object> AppGetCompereNoCalendars(CompereNoCalendarsDTO compereNoCalendarsDTO){

        return Optional.ofNullable(iCompereNoCalendarsService.AppGetCompereNoCalendars(compereNoCalendarsDTO))
                .map(compereAuctionCalendarVOList->{

                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",compereAuctionCalendarVOList.size());
                    result.put("data", compereAuctionCalendarVOList);
                    return new ResultUtil<>().setData(compereAuctionCalendarVOList,"获取主持人商品或者套餐已经被购买的档期列表成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据"));
    }





}
