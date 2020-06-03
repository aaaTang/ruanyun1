package cn.ruanyun.backInterface.modules.business.shopWorks.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.shopWorks.DTO.ShopWorksDTO;
import cn.ruanyun.backInterface.modules.business.shopWorks.pojo.ShopWorks;
import cn.ruanyun.backInterface.modules.business.shopWorks.service.IShopWorksService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author z
 * 商家作品管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/shopWorks")
@Transactional
public class ShopWorksController {

    @Autowired
    private IShopWorksService iShopWorksService;


   /**
     * 更新或者插入数据
     * @param shopWorks
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateShopWorks")
    public Result<Object> insertOrderUpdateShopWorks(ShopWorks shopWorks){

        try {

            iShopWorksService.insertOrderUpdateShopWorks(shopWorks);
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
    @PostMapping(value = "/removeShopWorks")
    public Result<Object> removeShopWorks(String ids){

        try {

            iShopWorksService.removeShopWorks(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }



    /**
     * App获取商家作品列表
     * @param pageVo
     * @return
     */
    @PostMapping("/AppGetShopWorksList")
    public Result<Object> AppGetShopWorksList(PageVo pageVo, ShopWorksDTO shopWorksDTO) {

        return Optional.ofNullable(iShopWorksService.AppGetShopWorksList(shopWorksDTO))
                .map(appGetShopWorksList -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",appGetShopWorksList.size());
                    result.put("data", PageUtil.listToPage(pageVo,appGetShopWorksList));
                    return new ResultUtil<>().setData(result,"App获取商家作品列表成功！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));

    }


    /**
     * 后端获取商家作品列表
     * @param pageVo
     * @return
     */
    @PostMapping("/getShopWorksList")
    public Result<Object> getShopWorksList(PageVo pageVo, ShopWorksDTO shopWorksDTO) {

        return Optional.ofNullable(iShopWorksService.getShopWorksList(shopWorksDTO))
                .map(shopWorksListVOS -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",shopWorksListVOS.size());
                    result.put("data", PageUtil.listToPage(pageVo,shopWorksListVOS));
                    return new ResultUtil<>().setData(result,"后端获取商家作品列表成功！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));

    }


}
