package cn.ruanyun.backInterface.modules.business.storeAudit.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.storeAudit.pojo.StoreAudit;
import cn.ruanyun.backInterface.modules.business.storeAudit.service.IStoreAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author fei
 * 商家入驻审核管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/storeAudit")
@Transactional
public class StoreAuditController {

    @Autowired
    private IStoreAuditService iStoreAuditService;


   /**
     * 更新或者插入数据
     * @param storeAudit
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateStoreAudit")
    public Result<Object> insertOrderUpdateStoreAudit(StoreAudit storeAudit){

        try {

            iStoreAuditService.insertOrderUpdateStoreAudit(storeAudit);
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
    @PostMapping(value = "/removeStoreAudit")
    public Result<Object> removeStoreAudit(String ids){

        try {

            iStoreAuditService.removeStoreAudit(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

}
