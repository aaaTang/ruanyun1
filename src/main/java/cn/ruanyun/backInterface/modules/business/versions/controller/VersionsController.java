package cn.ruanyun.backInterface.modules.business.versions.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.versions.pojo.Versions;
import cn.ruanyun.backInterface.modules.business.versions.service.IVersionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author z
 * 设备版本管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/versions")
@Transactional
public class VersionsController {

    @Autowired
    private IVersionsService iVersionsService;


   /**
     * 更新或者插入数据
     * @param versions
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateVersions")
    public Result<Object> insertOrderUpdateVersions(Versions versions){

        try {

            iVersionsService.insertOrderUpdateVersions(versions);
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
    @PostMapping(value = "/removeVersions")
    public Result<Object> removeVersions(String ids){

        try {

            iVersionsService.removeVersions(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

}
