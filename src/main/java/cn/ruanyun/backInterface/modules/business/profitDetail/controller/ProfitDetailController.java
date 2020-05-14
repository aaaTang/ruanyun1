package cn.ruanyun.backInterface.modules.business.profitDetail.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.business.profitDetail.pojo.ProfitDetail;
import cn.ruanyun.backInterface.modules.business.profitDetail.service.IProfitDetailService;
import cn.ruanyun.backInterface.modules.business.profitDetail.vo.ProfitDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author z
 * 分红明细管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/profitDetail")
@Transactional
public class ProfitDetailController {

    @Autowired
    private IProfitDetailService iProfitDetailService;


   /**
     * 更新或者插入数据
     * @param profitDetail
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateProfitDetail")
    public Result<Object> insertOrderUpdateProfitDetail(ProfitDetail profitDetail){

        try {

            iProfitDetailService.insertOrderUpdateProfitDetail(profitDetail);
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
    @PostMapping(value = "/removeProfitDetail")
    public Result<Object> removeProfitDetail(String ids){

        try {

            iProfitDetailService.removeProfitDetail(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 获取资金流向信息
     * @param pageVo
     * @return
     */
    @PostMapping("/getProfitDetailList")
    public Result<Object> getProfitDetailList(PageVo pageVo) {

        return Optional.ofNullable(iProfitDetailService.getProfitDetailList())
                .map(profitDetailVos -> {

                    DataVo<ProfitDetailVo> result = new DataVo<>();
                    result.setTotalNumber(profitDetailVos.size())
                            .setDataResult(PageUtil.listToPage(pageVo, profitDetailVos));

                    return new ResultUtil<>().setData(result, "获取资金流向信息成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));

    }
}
