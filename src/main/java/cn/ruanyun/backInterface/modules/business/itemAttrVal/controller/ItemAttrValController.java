package cn.ruanyun.backInterface.modules.business.itemAttrVal.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.pojo.ItemAttrVal;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.service.IItemAttrValService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author z
 * 规格属性管理管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/itemAttrVal")
@Transactional
public class ItemAttrValController {

    @Autowired
    private IItemAttrValService iItemAttrValService;


   /**
     * 更新或者插入数据
     * @param itemAttrVal
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateItemAttrVal")
    public Result<Object> insertOrderUpdateItemAttrVal(ItemAttrVal itemAttrVal){

        try {

            iItemAttrValService.insertOrderUpdateItemAttrVal(itemAttrVal);
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
    @PostMapping(value = "/removeItemAttrVal")
    public Result<Object> removeItemAttrVal(String ids){

        try {

            iItemAttrValService.removeItemAttrVal(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

}
