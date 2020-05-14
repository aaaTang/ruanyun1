package cn.ruanyun.backInterface.modules.business.profitPercent.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.business.profitPercent.pojo.ProfitPercent;
import cn.ruanyun.backInterface.modules.business.profitPercent.service.IProfitPercentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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


    /**
     * 获取分红列表
     * @param pageVo
     * @return
     */
    @PostMapping("/getProfitPercentList")
    public Result<Object> getProfitPercentList(PageVo pageVo) {

        return Optional.ofNullable(iProfitPercentService.getProfitPercentList())
                .map(profitPercentVos -> {

                    DataVo<ProfitPercent> result = new DataVo<>();
                    result.setTotalNumber(profitPercentVos.size())
                            .setDataResult(PageUtil.listToPage(pageVo, profitPercentVos));

                    return new ResultUtil<>().setData(result, "获取分红列表成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));

    }




}
