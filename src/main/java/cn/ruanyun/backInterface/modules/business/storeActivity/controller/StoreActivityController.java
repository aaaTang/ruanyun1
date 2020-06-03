package cn.ruanyun.backInterface.modules.business.storeActivity.controller;

import cn.ruanyun.backInterface.common.enums.UserTypeEnum;
import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.storeActivity.DTO.StoreActivityDTO;
import cn.ruanyun.backInterface.modules.business.storeActivity.pojo.StoreActivity;
import cn.ruanyun.backInterface.modules.business.storeActivity.service.IStoreActivityService;
import cn.ruanyun.backInterface.modules.business.storeFirstRateService.DTO.StoreFirstRateServiceDTO;
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
 * 店铺活动管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/storeActivity")
@Transactional
public class StoreActivityController {

    @Autowired
    private IStoreActivityService iStoreActivityService;


   /**
     * 更新或者插入数据
     * @param storeActivity
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateStoreActivity")
    public Result<Object> insertOrderUpdateStoreActivity(StoreActivity storeActivity){

        try {

            iStoreActivityService.insertOrderUpdateStoreActivity(storeActivity);
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
    @PostMapping(value = "/removeStoreActivity")
    public Result<Object> removeStoreActivity(String ids){

        try {

            iStoreActivityService.removeStoreActivity(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

    @PostMapping("/APPgetStoreActivity")
    @ApiOperation(value = "App获取商家活动列表")
    public Result<Object> getStoreActivity(PageVo pageVo, String createBy) {

        return Optional.ofNullable(iStoreActivityService.getStoreActivity(createBy, null))
                .map(storeActivityVOList -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",storeActivityVOList.size());
                    result.put("data", PageUtil.listToPage(pageVo,storeActivityVOList));
                    return new ResultUtil<>().setData(result,"App获取商家活动列表！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }


    @PostMapping("/APPgetTerraceActivity")
    @ApiOperation(value = "App获取平台活动列表")
    public Result<Object> APPgetTerraceActivity(PageVo pageVo) {

        return Optional.ofNullable(iStoreActivityService.getStoreActivity(null, UserTypeEnum.ADMIN))
                .map(storeActivityVOList -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",storeActivityVOList.size());
                    result.put("data", PageUtil.listToPage(pageVo,storeActivityVOList));
                    return new ResultUtil<>().setData(result,"App获取平台活动列表！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }



    @PostMapping("/getStoreActivityList")
    @ApiOperation(value = "获取商家活动列表")
    public Result<Object> getStoreActivityList(PageVo pageVo, StoreActivityDTO storeActivityDTO) {

        return Optional.ofNullable(iStoreActivityService.getStoreActivityList(storeActivityDTO))
                .map(storeActivityVOList -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",storeActivityVOList.size());
                    result.put("data", PageUtil.listToPage(pageVo,storeActivityVOList));
                    return new ResultUtil<>().setData(result,"获取商家活动列表成功！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }

}
