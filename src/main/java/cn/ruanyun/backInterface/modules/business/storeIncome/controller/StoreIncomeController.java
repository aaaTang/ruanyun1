package cn.ruanyun.backInterface.modules.business.storeIncome.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.business.storeIncome.pojo.StoreIncome;
import cn.ruanyun.backInterface.modules.business.storeIncome.service.IStoreIncomeService;
import cn.ruanyun.backInterface.modules.business.storeIncome.vo.StoreIncomeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author z
 * 店铺收入管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/storeIncome")
@Transactional
public class StoreIncomeController {

    @Autowired
    private IStoreIncomeService iStoreIncomeService;


   /**
     * 更新或者插入数据
     * @param storeIncome
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateStoreIncome")
    public Result<Object> insertOrderUpdateStoreIncome(StoreIncome storeIncome){

        try {

            iStoreIncomeService.insertOrderUpdateStoreIncome(storeIncome);
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
    @PostMapping(value = "/removeStoreIncome")
    public Result<Object> removeStoreIncome(String ids){

        try {

            iStoreIncomeService.removeStoreIncome(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 获取店铺收入明细
     * @param pageVo 分页
     * @return Object
     */
    @PostMapping("/getStoreIncomeList")
    public Result<Object> getStoreIncomeList(PageVo pageVo) {

        return Optional.ofNullable(iStoreIncomeService.getStoreIncomeList())
                .map(storeIncomeVos -> {

                    DataVo<StoreIncomeVo> result = new DataVo<>();
                    result.setTotalNumber(storeIncomeVos.size())
                            .setDataResult(PageUtil.listToPage(pageVo, storeIncomeVos));

                    return new ResultUtil<>().setData(result, "获取收入明细成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }


    /**
     * 获取门店统计收入数据
     * @param storeId 门店id
     * @return Object
     */
    @PostMapping("/getStoreIncomeCount")
    public Result<Object> getStoreIncomeCount(String storeId) {

        return new ResultUtil<>().setData(iStoreIncomeService.getStoreIncomeCount(storeId), "获取门店统计收入数据成功！");
    }
}
