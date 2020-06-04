package cn.ruanyun.backInterface.modules.auctionCalendar.sitePrice.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.auctionCalendar.sitePrice.DTO.SitePriceDTO;
import cn.ruanyun.backInterface.modules.auctionCalendar.sitePrice.pojo.SitePrice;
import cn.ruanyun.backInterface.modules.auctionCalendar.sitePrice.service.ISitePriceService;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author z
 * 设置场地档期价格管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/sitePrice")
@Transactional
public class SitePriceController {

    @Autowired
    private ISitePriceService iSitePriceService;


   /**
     * 更新或者插入数据
     * @param sitePrice
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateSitePrice")
    public Result<Object> insertOrderUpdateSitePrice(SitePrice sitePrice){

        try {

            iSitePriceService.insertOrderUpdateSitePrice(sitePrice);
            return new ResultUtil<>().setSuccessMsg("插入或者更新成功!");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 移除数据
     * @param siteId 场地id
     * @param scheduleTime 时间段
     * @return
    */
    @PostMapping(value = "/removeSitePrice")
    public Result<Object> removeSitePrice(String siteId,String scheduleTime){

        try {

            iSitePriceService.removeSitePrice(siteId,scheduleTime);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

    /**
     * 后端查询档期价格
     * sitePriceDTO.getScheduleTime()  时间
     *   sitePriceDTO.getSiteId() 场地id
     */
    @PostMapping(value = "/getSitePrice")
    public Result<Object> getSitePrice(SitePriceDTO sitePriceDTO){

        return Optional.ofNullable(iSitePriceService.getSitePrice(sitePriceDTO))
                .map(storeActivityVOList -> {
                    return new ResultUtil<>().setData(storeActivityVOList,"查询档期价格成功！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }



}
