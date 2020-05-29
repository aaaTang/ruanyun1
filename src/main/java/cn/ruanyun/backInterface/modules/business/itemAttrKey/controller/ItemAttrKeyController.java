package cn.ruanyun.backInterface.modules.business.itemAttrKey.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.itemAttrKey.pojo.ItemAttrKey;
import cn.ruanyun.backInterface.modules.business.itemAttrKey.service.IItemAttrKeyService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author z
 * 规格管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/itemAttrKey")
@Transactional
public class ItemAttrKeyController {

    @Autowired
    private IItemAttrKeyService iItemAttrKeyService;


   /**
     * 更新或者插入数据
     * @param itemAttrKey
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateItemAttrKey")
    public Result<Object> insertOrderUpdateItemAttrKey(ItemAttrKey itemAttrKey){

        try {

            iItemAttrKeyService.insertOrderUpdateItemAttrKey(itemAttrKey);
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
    @PostMapping(value = "/removeItemAttrKey")
    public Result<Object> removeItemAttrKey(String ids){

        try {

            iItemAttrKeyService.removeItemAttrKey(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 获取规格列表
     * @return
     */
    @PostMapping(value = "/getItemAttrKeyList")
    public Result<Object> getItemAttrKeyList(String classId){

        return iItemAttrKeyService.getItemAttrKeyList(classId);
    }

}
