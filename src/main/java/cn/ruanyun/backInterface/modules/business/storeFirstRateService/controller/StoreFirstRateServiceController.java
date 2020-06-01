package cn.ruanyun.backInterface.modules.business.storeFirstRateService.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.storeFirstRateService.pojo.StoreFirstRateService;
import cn.ruanyun.backInterface.modules.business.storeFirstRateService.service.IstoreFirstRateServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

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



}
