package cn.ruanyun.backInterface.modules.business.advertising.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.advertising.pojo.Advertising;
import cn.ruanyun.backInterface.modules.business.advertising.service.IAdvertisingService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

/**
 * @author fei
 * 广告管理管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/advertising")
@Transactional
public class AdvertisingController {

    @Autowired
    private IAdvertisingService iAdvertisingService;


   /**
     * 更新或者插入数据
     * @param advertising
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateAdvertising")
    public Result<Object> insertOrderUpdateAdvertising(Advertising advertising){

        try {

            iAdvertisingService.insertOrderUpdateAdvertising(advertising);
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
    @PostMapping(value = "/removeAdvertising")
    public Result<Object> removeAdvertising(String ids){
        try {
            iAdvertisingService.removeAdvertising(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {
            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

    /**
     * App查询广告数据列表
     * @param advertisingType 1.开屏,  2.轮播
     * @param advertisingJumpType  1.编辑详情页  2.H5网页链接  3.活动页面  4.商家店铺首页  5.商品详情
     * @return
     */
    @PostMapping(value = "/APPgetAdvertisingList")
    public Result<Object> APPgetAdvertisingList(PageVo pageVo ,String advertisingType, String advertisingJumpType){

        return Optional.ofNullable(iAdvertisingService.APPgetAdvertisingList(advertisingType,advertisingJumpType))
                .map(advertisingList -> {
                    Map<String, Object> result = Maps.newHashMap();
                    result.put("size", advertisingList.size());
                    result.put("data",  PageUtil.listToPage(pageVo, advertisingList));

                    return new ResultUtil<>().setData(result, "获取APP广告数据成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }


    /**
     * 后端查询广告数据列表
     * @param advertisingType 1.开屏,  2.轮播
     * @param advertisingJumpType  1.编辑详情页  2.H5网页链接  3.活动页面  4.商家店铺首页
     * @return
     */
    @PostMapping(value = "/BackGetAdvertisingList")
    public Result<Object> BackGetAdvertisingList(PageVo pageVo ,String advertisingType, String advertisingJumpType){

        return Optional.ofNullable(iAdvertisingService.BackGetAdvertisingList(advertisingType,advertisingJumpType))
                .map(iAdvertisingService -> {
                    Map<String, Object> result = Maps.newHashMap();
                    result.put("size", iAdvertisingService.size());
                    result.put("data",  PageUtil.listToPage(pageVo, iAdvertisingService));

                    return new ResultUtil<>().setData(result, "获取后端广告数据成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }

}
