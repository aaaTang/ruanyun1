package cn.ruanyun.backInterface.modules.business.selectedStore.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.selectedStore.pojo.SelectedStore;
import cn.ruanyun.backInterface.modules.business.selectedStore.service.ISelectedStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author fei
 * 严选商家管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/selectedStore")
@Transactional
public class SelectedStoreController {

    @Autowired
    private ISelectedStoreService iSelectedStoreService;


   /**
     * 更新或者插入数据
     * @param selectedStore
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateSelectedStore")
    public Result<Object> insertOrderUpdateSelectedStore(SelectedStore selectedStore){

        try {

            iSelectedStoreService.insertOrderUpdateSelectedStore(selectedStore);
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
    @PostMapping(value = "/removeSelectedStore")
    public Result<Object> removeSelectedStore(String ids){

        try {

            iSelectedStoreService.removeSelectedStore(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

}
