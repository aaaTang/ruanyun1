package cn.ruanyun.backInterface.modules.business.bestChoiceShop.controller;

import cn.ruanyun.backInterface.common.utils.EmptyUtil;
import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.bestChoiceShop.pojo.BestShop;
import cn.ruanyun.backInterface.modules.business.bestChoiceShop.service.IBestShopService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author zhu
 * 严选商家管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/bestShop")
@Transactional
public class BestShopController {

    @Autowired
    private IBestShopService iBestShopService;


   /**
     * 更新或者插入数据
     * @param bestShop
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateBestShop")
    public Result<Object> insertOrderUpdateBestShop(BestShop bestShop){

        try {

            iBestShopService.insertOrderUpdateBestShop(bestShop);
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
    @PostMapping(value = "/removeBestShop")
    public Result<Object> removeBestShop(String ids){

        try {

            iBestShopService.removeBestShop(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

    /**
     * APP严选商家
     * @param strict 是否严选1是  0否
     * @return
     */
    @PostMapping(value = "/getBestChoiceShopList")
    public Result<Object> getBestChoiceShopList(Integer pageNumber, Integer pageSize,String strict){
                     PageVo pageVo = new PageVo();
                    if(EmptyUtil.isEmpty(pageNumber)&&EmptyUtil.isEmpty(pageSize)){
                        pageVo.setPageNumber(1);
                        pageVo.setPageSize(100);
                    }else {
                        pageVo.setPageNumber(pageNumber);
                        pageVo.setPageSize(pageSize);
                    }
            return Optional.ofNullable(iBestShopService.getBestChoiceShopList(strict))
                    .map(bestShopList-> {
                        Map<String, Object> result = Maps.newHashMap();
                        result.put("size",  bestShopList.size());
                        result.put("data",  PageUtil.listToPage(pageVo, bestShopList));

                        return new ResultUtil<>().setData(result, "获取严选商家数据成功！");

                    }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }




    /**
     * 后端严选商家
     * @param strict 是否严选1是  0否
     * @return
     */
    @PostMapping(value = "/BackBestChoiceShopList")
    public Result<Object> BackBestChoiceShopList(PageVo pageVo,String strict){

        return Optional.ofNullable(iBestShopService.BackBestChoiceShopList(strict))
                .map(bestShopList-> {
                    Map<String, Object> result = Maps.newHashMap();
                    result.put("size",  bestShopList.size());
                    result.put("data",  PageUtil.listToPage(pageVo, bestShopList));

                    return new ResultUtil<>().setData(result, "获取后端严选商家数据成功！");

                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }




}
