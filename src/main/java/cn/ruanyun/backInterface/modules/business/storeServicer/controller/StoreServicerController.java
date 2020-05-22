package cn.ruanyun.backInterface.modules.business.storeServicer.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.storeServicer.pojo.StoreServicer;
import cn.ruanyun.backInterface.modules.business.storeServicer.service.IStoreServicerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author z
 * 店铺客服管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/storeServicer")
@Transactional
public class StoreServicerController {

    @Autowired
    private IStoreServicerService iStoreServicerService;


   /**
     * 更新或者插入数据
     * @param storeServicer
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateStoreServicer")
    public Result<Object> insertOrderUpdateStoreServicer(StoreServicer storeServicer){

        try {

            iStoreServicerService.insertOrderUpdateStoreServicer(storeServicer);
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
    @PostMapping(value = "/removeStoreServicer")
    public Result<Object> removeStoreServicer(String ids){

        try {

            iStoreServicerService.removeStoreServicer(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

}
