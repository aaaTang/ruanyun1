package cn.ruanyun.backInterface.modules.business.grade.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.grade.pojo.Grade;
import cn.ruanyun.backInterface.modules.business.grade.service.IGradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wj
 * 评分管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/grade")
@Transactional
public class GradeController {

    @Autowired
    private IGradeService iGradeService;


   /**
     * 更新或者插入数据
     * @param grade
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateGrade")
    public Result<Object> insertOrderUpdateGrade(Grade grade){

        try {

            iGradeService.insertOrderUpdateGrade(grade);
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
    @PostMapping(value = "/removeGrade")
    public Result<Object> removeGrade(String ids){

        try {

            iGradeService.removeGrade(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

}
