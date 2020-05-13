package cn.ruanyun.backInterface.modules.business.profitPercent.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.profitPercent.pojo.ProfitPercent;
import cn.ruanyun.backInterface.modules.business.profitPercent.service.IProfitPercentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author z
 * 分红比例管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/profitPercent")
@Transactional
public class ProfitPercentController {

    @Autowired
    private IProfitPercentService iProfitPercentService;


   /**
     * 更新或者插入数据
     * @param profitPercent
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateProfitPercent")
    public Result<Object> insertOrderUpdateProfitPercent(ProfitPercent profitPercent){

        try {

            iProfitPercentService.insertOrderUpdateProfitPercent(profitPercent);
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
    @PostMapping(value = "/removeProfitPercent")
    public Result<Object> removeProfitPercent(String ids){

        try {

            iProfitPercentService.removeProfitPercent(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

}
