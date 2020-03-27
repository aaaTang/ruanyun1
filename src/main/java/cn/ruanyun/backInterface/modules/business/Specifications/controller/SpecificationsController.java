package cn.ruanyun.backInterface.modules.business.Specifications.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.Specifications.pojo.Specifications;
import cn.ruanyun.backInterface.modules.business.Specifications.service.ISpecificationsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhu
 * 商品规格管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/specifications")
@Transactional
public class SpecificationsController {

    @Autowired
    private ISpecificationsService iSpecificationsService;


   /**
     * 更新或者插入数据
     * @param specifications
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateSpecifications")
    public Result<Object> insertOrderUpdateSpecifications(Specifications specifications){

        try {

            iSpecificationsService.insertOrderUpdateSpecifications(specifications);
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
    @PostMapping(value = "/removeSpecifications")
    public Result<Object> removeSpecifications(String ids){

        try {

            iSpecificationsService.removeSpecifications(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

}
