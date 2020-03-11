package cn.ruanyun.backInterface.modules.business.advertising.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.advertising.pojo.Advertising;
import cn.ruanyun.backInterface.modules.business.advertising.service.IAdvertisingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

}
