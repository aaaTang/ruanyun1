package cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.DTO.CompereAuctionCalendarDTO;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.pojo.CompereAuctionCalendar;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.service.ICompereAuctionCalendarService;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

/**
 * @author z
 * 主持人特殊档期价格管理接口
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
     * @param goodsId  商品id
     * @return scheduleTime  档期时间
    */
    @PostMapping(value = "/removeCompereAuctionCalendar")
    public Result<Object> removeCompereAuctionCalendar(String goodsId,String scheduleTime){

        try {

            iCompereAuctionCalendarService.removeCompereAuctionCalendar(goodsId,scheduleTime);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    @PostMapping("/AppGetCompereAuctionCalendar")
    @ApiOperation("APP查询某天是否有档期")
    public Result<Object> AppGetCompereAuctionCalendar(String goodsId,String scheduleTime) {

        return new ResultUtil<>().setData(iCompereAuctionCalendarService.AppGetCompereAuctionCalendar(goodsId,scheduleTime),"获取数据成功！");
    }


    /**
     * 后台获取特殊档期价格列表
     * @param pageVo
     * @return
     */
    @PostMapping(value = "/PcGetCompereAuctionCalendar")
    public Result<Object> PcGetCompereAuctionCalendar(PageVo pageVo,CompereAuctionCalendarDTO compereAuctionCalendarDTO){

        return Optional.ofNullable(iCompereAuctionCalendarService.PcGetCompereAuctionCalendar(compereAuctionCalendarDTO))
                .map(compereAuctionCalendarVOList->{
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",compereAuctionCalendarVOList.size());
                    result.put("data", PageUtil.listToPage(pageVo,compereAuctionCalendarVOList));

                    return new ResultUtil<>().setData(result,"后台获取特殊档期价格列表成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据"));
    }


}
