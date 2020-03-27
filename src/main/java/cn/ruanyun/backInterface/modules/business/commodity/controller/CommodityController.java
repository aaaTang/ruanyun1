package cn.ruanyun.backInterface.modules.business.commodity.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.commodity.pojo.Commodity;
import cn.ruanyun.backInterface.modules.business.commodity.service.ICommodityService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author zhu
 * 商品管理管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/commodity")
@Transactional
public class CommodityController {

    @Autowired
    private ICommodityService iCommodityService;


   /**
     * 更新或者插入数据
     * @param commodity
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateCommodity")
    public Result<Object> insertOrderUpdateCommodity(Commodity commodity){

        try {

            iCommodityService.insertOrderUpdateCommodity(commodity);
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
        @PostMapping(value = "/removeCommodity")
    public Result<Object> removeCommodity(String ids){

        try {

            iCommodityService.removeCommodity(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 获取App商品详情
     */
    @PostMapping("/AppCommodityDetails")
    public Result<Object> AppCommodityDetails(String ids) {

        return Optional.ofNullable(iCommodityService.AppCommodityDetails(ids))
                .map(CommodityDetails -> {

                    Map<String, Object> result = Maps.newHashMap();
                    result.put("data",CommodityDetails.getResult());
                    return new ResultUtil<>().setData(result, "获取获取商品详情成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }

    /**
     * 获取商品列表
     */
    @PostMapping("/CommodityList")
    public Result<Object> CommodityList(PageVo pageVo,Commodity commodity) {

        return Optional.ofNullable(iCommodityService.CommodityList(commodity))
                .map(iCommodityList -> {

                    Map<String, Object> result = Maps.newHashMap();
                    result.put("size",iCommodityList.size());
                    result.put("data",PageUtil.listToPage(pageVo, iCommodityList));
                    return new ResultUtil<>().setData(result, "获取商品列表成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }

}
