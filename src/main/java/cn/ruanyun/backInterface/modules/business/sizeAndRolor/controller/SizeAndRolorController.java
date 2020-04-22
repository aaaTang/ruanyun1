package cn.ruanyun.backInterface.modules.business.sizeAndRolor.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.pojo.SizeAndRolor;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.service.ISizeAndRolorService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;


/**
 * @author zhu
 * 规格和大小管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/sizeAndRolor")
@Transactional
public class SizeAndRolorController {

    @Autowired
    private ISizeAndRolorService iSizeAndRolorService;


   /**
     * 更新或者插入数据
     * @param sizeAndRolor
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateSizeAndRolor")
    public Result<Object> insertOrderUpdateSizeAndRolor(SizeAndRolor sizeAndRolor){

        try {

            iSizeAndRolorService.insertOrderUpdateSizeAndRolor(sizeAndRolor);
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
    @PostMapping(value = "/removeSizeAndRolor")
    public Result<Object> removeSizeAndRolor(String ids){

        try {

            iSizeAndRolorService.removeSizeAndRolor(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 获取商品规格和大小
     * @return
     */
    @PostMapping(value = "/SizeAndRolorList")
    public Result<Object> SizeAndRolorList(String goodsId){

            return Optional.ofNullable(iSizeAndRolorService.SizeAndRolorList(goodsId))
                    .map(sizeAndRolor-> {
                        Map<String, Object> result = Maps.newHashMap();
                        result.put("data",  sizeAndRolor);
                        return new ResultUtil<>().setData(result, "获取商品规格和大小数据成功！");

                    }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }


    /**
     * 获取规格属性的图片价格库存
     * @return
     */
    @PostMapping(value = "/getInventory")
    public Result<Object> getInventory(String ids,String goodsId){

        return Optional.ofNullable(iSizeAndRolorService.getInventory(ids,goodsId))
                .map(sizeAndRolor-> {
                    Map<String, Object> result = Maps.newHashMap();
                    result.put("data",  sizeAndRolor);
                    return new ResultUtil<>().setData(result, "获取规格属性的图片价格库存数据成功！");

                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }


    /**
     * WEB按商品获取库存
     * @return
     */
    @PostMapping(value = "/getWebInventory")
    public Result<Object> getWebInventory(String ids,String goodsId){

        return Optional.ofNullable(iSizeAndRolorService.getWebInventory(ids,goodsId))
                .map(sizeAndRolor-> {
                    Map<String, Object> result = Maps.newHashMap();
                    result.put("data",  sizeAndRolor);
                    return new ResultUtil<>().setData(result, "WEB按商品获取库存成功！");

                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }

}
