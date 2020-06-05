package cn.ruanyun.backInterface.modules.business.storeAudit.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.business.storeAudit.dto.StoreAuditDTO;
import cn.ruanyun.backInterface.modules.business.storeAudit.vo.StoreAuditVo;
import cn.ruanyun.backInterface.modules.business.storeAudit.pojo.StoreAudit;
import cn.ruanyun.backInterface.modules.business.storeAudit.service.IStoreAuditService;
import io.swagger.annotations.ApiOperation;
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

       return iStoreAuditService.insertOrderUpdateStoreAudit(storeAudit);

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

    /**
     * 获取申请记录列表
     */
    @PostMapping("/getList")
    public Result<Object> getList(StoreAuditDTO storeAuditDTO, PageVo pageVo){
        List<StoreAuditVo> list = iStoreAuditService.getStoreAuditList(storeAuditDTO);
        DataVo<StoreAuditVo> result = new DataVo<>();
        result.setTotalNumber(list.size())
                .setDataResult(PageUtil.listToPage(pageVo,list));
        return new ResultUtil<>().setData(result);
    }

    /**
     * 审核申请
     */
    @PostMapping("/checkStoreAudit")
    public Result<Object> checkStoreAudit(StoreAuditDTO storeAuditDTO){return iStoreAuditService.checkStoreAudit(storeAuditDTO);}



    /*----------------------更改流程部分----------------------------*/

    @PostMapping("/getMyStoreAudit")
    @ApiOperation("获取我的审核记录")
    public Result<StoreAuditVo> getMyStoreAudit() {

        return iStoreAuditService.getMyStoreAudit();
    }

    @PostMapping("/cancelStoreAudit")
    @ApiOperation("撤销我的申请")
    public Result<Object> cancelStoreAudit() {

        return iStoreAuditService.cancelStoreAudit();
    }

    @PostMapping("/updateStoreAudit")
    @ApiOperation("更新我的申请")
    public Result<Object> updateStoreAudit(StoreAuditVo storeAuditVo) {

        return iStoreAuditService.updateStoreAudit(storeAuditVo);
    }

}
