package cn.ruanyun.backInterface.modules.auctionCalendar.auctionCalendarOrder.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.auctionCalendar.auctionCalendarOrder.pojo.AuctionCalendarOrder;
import cn.ruanyun.backInterface.modules.auctionCalendar.auctionCalendarOrder.service.IAuctionCalendarOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author fei
 * 档期订单管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/auctionCalendarOrder")
@Transactional
public class AuctionCalendarOrderController {

    @Autowired
    private IAuctionCalendarOrderService iAuctionCalendarOrderService;


   /**
     * 更新或者插入数据
     * @param auctionCalendarOrder
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateAuctionCalendarOrder")
    public Result<Object> insertOrderUpdateAuctionCalendarOrder(AuctionCalendarOrder auctionCalendarOrder){

        try {

            iAuctionCalendarOrderService.insertOrderUpdateAuctionCalendarOrder(auctionCalendarOrder);
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
    @PostMapping(value = "/removeAuctionCalendarOrder")
    public Result<Object> removeAuctionCalendarOrder(String ids){

        try {

            iAuctionCalendarOrderService.removeAuctionCalendarOrder(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

}
