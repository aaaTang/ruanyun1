package cn.ruanyun.backInterface.modules.business.goodsPackage.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.goodsPackage.DTO.ShopParticularsDTO;
import cn.ruanyun.backInterface.modules.business.goodsPackage.pojo.GoodsPackage;
import cn.ruanyun.backInterface.modules.business.goodsPackage.service.IGoodsPackageService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author fei
 * 商品套餐管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/goodsPackage")
@Transactional
public class GoodsPackageController {

    @Autowired
    private IGoodsPackageService iGoodsPackageService;


   /**
     * 更新或者插入数据
     * @param goodsPackage
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateGoodsPackage")
    public Result<Object> insertOrderUpdateGoodsPackage(GoodsPackage goodsPackage){

        try {

            iGoodsPackageService.insertOrderUpdateGoodsPackage(goodsPackage);
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
    @PostMapping(value = "/removeGoodsPackage")
    public Result<Object> removeGoodsPackage(String ids){

        try {

            iGoodsPackageService.removeGoodsPackage(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 查询商家商品详情
     * @param ids
     * @return
     */
    @PostMapping(value = "/GetGoodsPackage")
    public Result<Object> GetGoodsPackage(String ids){

        return Optional.ofNullable(iGoodsPackageService.GetGoodsPackage(ids))
                .map(iAppGetGoodsPackage-> {
                    Map<String, Object> result = Maps.newHashMap();
                    result.put("data",  iAppGetGoodsPackage.getResult());
                    return new ResultUtil<>().setData(result, "获取App查询商品详情数据成功！");

                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }


    /**
     * 后端查询商品全部数据
     * @return
     */
    @PostMapping(value = "/BackGoodsPackageList")
    public Result<Object> BackGoodsPackageList(PageVo pageVo){

        return Optional.ofNullable(iGoodsPackageService.BackGoodsPackageList())
                .map(iBackGoodsPackageList-> {
                    Map<String, Object> result = Maps.newHashMap();
                    result.put("size",  iBackGoodsPackageList.size());
                    result.put("data",  PageUtil.listToPage(pageVo,iBackGoodsPackageList));

                    return new ResultUtil<>().setData(result, "获取后端查询商品全部数据成功！");

                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }


    /**
     * App分类商家商品筛选
     */
    @PostMapping(value = "/GetGoodsPackageList")
    public Result<Object> GetGoodsPackageList(PageVo pageVo, String classId, String areaId,Integer newPrice){

        return Optional.ofNullable(iGoodsPackageService.GetGoodsPackageList(classId,areaId,newPrice))
                .map(iGetGoodsPackageList-> {
                    Map<String, Object> result = Maps.newHashMap();
                    result.put("size",  iGetGoodsPackageList.size());
                    result.put("data",  PageUtil.listToPage(pageVo, iGetGoodsPackageList));

                    return new ResultUtil<>().setData(result, "获取App查询商品详情数据成功！");

                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }


    /**
     * 查询商家精选套餐
     */
    @PostMapping(value = "/AppGoodsPackageList")
    public Result<Object> AppGoodsPackageList(PageVo pageVo, String ids){

        return Optional.ofNullable(iGoodsPackageService.AppGoodsPackageList(ids))
                .map(iAppGoodsPackageList-> {
                    Map<String, Object> result = Maps.newHashMap();
                    result.put("size",  iAppGoodsPackageList.size());
                    result.put("data",  PageUtil.listToPage(pageVo, iAppGoodsPackageList));

                    return new ResultUtil<>().setData(result, "获取查询商家精选套餐数据成功！");

                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }



    /*****************************************************分割线****商家店铺******************************************************/

    /**
     * 获取App店铺详情
     */
    @PostMapping(value = "/getShopParticulars")
    public Result<Object> getShopParticulars(String ids){

        return Optional.ofNullable(iGoodsPackageService.getShopParticulars(ids))
                .map(iShopParticulars-> {
                    Map<String, Object> result = Maps.newHashMap();
                    result.put("data",  iShopParticulars);

                    return new ResultUtil<>().setData(result, "获取App店铺详情数据成功！");

                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));

    }


    /**
     * 修改店铺详情
     * @return
     */
    @PostMapping(value = "/UpdateShopParticulars")
    public Result<Object> UpdateShopParticulars(ShopParticularsDTO shopParticularsDTO){

        try {

                iGoodsPackageService.UpdateShopParticulars(shopParticularsDTO);
            return new ResultUtil<>().setSuccessMsg("修改店铺详情成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

    /**
     * 后端获取店铺列表
     */
    @PostMapping("/getShopDateList")
    public Result<Object> getShopDateList(String username, String shopName, Integer storeType , PageVo pageVo) {

        return Optional.ofNullable(iGoodsPackageService.getShopDateList(username,  shopName,  storeType ))
                .map(backUserVOS -> {

                    Map<String, Object> result = Maps.newHashMap();
                    result.put("size", backUserVOS.size());
                    result.put("data", PageUtil.listToPage(pageVo, backUserVOS));
                    return new ResultUtil<>().setData(result, "获取后端获取店铺列表成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }

}
