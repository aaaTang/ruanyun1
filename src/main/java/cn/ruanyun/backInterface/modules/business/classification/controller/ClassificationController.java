package cn.ruanyun.backInterface.modules.business.classification.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.classification.pojo.Classification;
import cn.ruanyun.backInterface.modules.business.classification.service.IClassificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author fei
 * 分类管理管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/classification")
@Transactional
public class ClassificationController {

    @Autowired
    private IClassificationService iClassificationService;


   /**
     * 更新或者插入数据
     * @param classification
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateClassification")
    public Result<Object> insertOrderUpdateClassification(Classification classification){

        try {

            iClassificationService.insertOrderUpdateClassification(classification);
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
    @PostMapping(value = "/removeClassification")
    public Result<Object> removeClassification(String ids){

        try {

            iClassificationService.removeClassification(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

}
