package cn.ruanyun.backInterface.modules.business.storeFirstRateService.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.storeFirstRateService.DTO.StoreFirstRateServiceDTO;
import cn.ruanyun.backInterface.modules.business.storeFirstRateService.pojo.StoreFirstRateService;
import cn.ruanyun.backInterface.modules.business.storeFirstRateService.service.IstoreFirstRateServiceService;
import cn.ruanyun.backInterface.modules.merchant.authentication.DTO.AuthenticationDTO;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

/**
 * @author fei
 * 商家优质服务管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/storeFirstRateService")
@Transactional
public class StoreFirstRateServiceController {

    @Autowired
    private IstoreFirstRateServiceService istoreFirstRateServiceService;


    /**
     * 更新或者插入数据
     * @param storeFirstRateService storeFirstRateService
     * @return Object
     */
    @PostMapping(value = "/insertOrderUpdateStoreFirstRateService")
    public Result<Object> insertOrderUpdateStoreFirstRateService(StoreFirstRateService storeFirstRateService){

        try {

            istoreFirstRateServiceService.insertOrderUpdateStoreFirstRateService(storeFirstRateService);
            return new ResultUtil<>().setSuccessMsg("插入或者更新成功!");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 移除数据
     * @param ids ids
     * @return Object
     */
    @PostMapping(value = "/removeStoreFirstRateService")
    public Result<Object> removeStoreFirstRateService(String ids){

        try {

            istoreFirstRateServiceService.removeStoreFirstRateService(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

    /**
     * 审核商家优质服务
     * @param storeFirstRateService 实体类
     * @return
     */
    @PostMapping(value = "/checkStoreFirstRate")
    public Result<Object> checkStoreFirstRate(StoreFirstRateService storeFirstRateService){

        return istoreFirstRateServiceService.checkStoreFirstRate(storeFirstRateService);
    }


    @PostMapping("/getStoreFirstRateService")
    @ApiOperation(value = "获取商家请申请优质服务的记录列表")
    public Result<Object> getStoreFirstRateService(PageVo pageVo, StoreFirstRateServiceDTO storeFirstRateServiceDTO) {

        return Optional.ofNullable(istoreFirstRateServiceService.getStoreFirstRateService(storeFirstRateServiceDTO))
                .map(firstRateServiceList -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",firstRateServiceList.size());
                    result.put("data", PageUtil.listToPage(pageVo,firstRateServiceList));
                    return new ResultUtil<>().setData(result,"获取商家请申请优质服务的记录列表成功！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }


}
