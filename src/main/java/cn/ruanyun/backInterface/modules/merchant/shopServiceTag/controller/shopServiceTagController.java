package cn.ruanyun.backInterface.modules.merchant.shopServiceTag.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.merchant.shopServiceTag.pojo.shopServiceTag;
import cn.ruanyun.backInterface.modules.merchant.shopServiceTag.service.IshopServiceTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author z
 * 商家优质服务标签管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/shopServiceTag")
@Transactional
public class shopServiceTagController {

    @Autowired
    private IshopServiceTagService ishopServiceTagService;


   /**
     * 更新或者插入数据
     * @param shopServiceTag
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateshopServiceTag")
    public Result<Object> insertOrderUpdateshopServiceTag(shopServiceTag shopServiceTag){

        try {

            ishopServiceTagService.insertOrderUpdateshopServiceTag(shopServiceTag);
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
    @PostMapping(value = "/removeshopServiceTag")
    public Result<Object> removeshopServiceTag(String ids){

        try {

            ishopServiceTagService.removeshopServiceTag(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

}
