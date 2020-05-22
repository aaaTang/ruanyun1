package cn.ruanyun.backInterface.modules.business.platformServicer.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.platformServicer.pojo.PlatformServicer;
import cn.ruanyun.backInterface.modules.business.platformServicer.service.IPlatformServicerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author z
 * 平台客服管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/platformServicer")
@Transactional
public class PlatformServicerController {

    @Autowired
    private IPlatformServicerService iPlatformServicerService;


   /**
     * 更新或者插入数据
     * @param platformServicer
     * @return
    */
    @PostMapping(value = "/insertOrderUpdatePlatformServicer")
    public Result<Object> insertOrderUpdatePlatformServicer(PlatformServicer platformServicer){

        try {

            iPlatformServicerService.insertOrderUpdatePlatformServicer(platformServicer);
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
    @PostMapping(value = "/removePlatformServicer")
    public Result<Object> removePlatformServicer(String ids){

        try {

            iPlatformServicerService.removePlatformServicer(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

}
