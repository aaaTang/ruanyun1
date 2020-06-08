package cn.ruanyun.backInterface.modules.business.goodsPackage.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.goodsPackage.DTO.ShopParticularsDTO;
import cn.ruanyun.backInterface.modules.business.goodsPackage.pojo.GoodsPackage;
import cn.ruanyun.backInterface.modules.business.goodsPackage.service.IGoodsPackageService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "H5接口")
public class GoodsPackageController {

    @Autowired
    private IGoodsPackageService iGoodsPackageService;



    @ApiOperation("app查询套餐商品详情")
    @PostMapping(value = "/GetGoodsPackage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "套餐id", dataType = "string", paramType = "query")

    })
    public Result<Object> getGoodsPackage(String ids){

        return Optional.ofNullable(iGoodsPackageService.GetGoodsPackage(ids))
                .map(iAppGetGoodsPackage-> {
                    Map<String, Object> result = Maps.newHashMap();
                    result.put("data",  iAppGetGoodsPackage.getResult());
                    return new ResultUtil<>().setData(result, "获取App查询商品详情数据成功！");

                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }


    @ApiOperation("获取App店铺详情")
    @PostMapping(value = "/getShopParticulars")
    @ApiImplicitParams({

            @ApiImplicitParam(name = "ids", value = "商家id", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "longitude", value = "精度", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "latitude", value = "维度", dataType = "string", paramType = "query")
    })
    public Result<Object> getShopParticulars(String ids, String longitude, String latitude){

        return Optional.ofNullable(iGoodsPackageService.getShopParticulars(ids,longitude,latitude))
                .map(iShopParticulars-> {

                    Map<String, Object> result = Maps.newHashMap();
                    result.put("data",  iShopParticulars);

                    return new ResultUtil<>().setData(result, "获取App店铺详情数据成功！");

                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));

    }



    /**
     * App分类商家商品筛选
     */
    @PostMapping(value = "/GetGoodsPackageList")
    public Result<Object> GetGoodsPackageList(PageVo pageVo, String classId, String areaId,Integer newPrice){
        return Optional.ofNullable(iGoodsPackageService.GetGoodsPackageList(classId,areaId,newPrice,null))
                .map(iGetGoodsPackageList-> {
                    Map<String, Object> result = Maps.newHashMap();
                    result.put("size",  iGetGoodsPackageList.size());
                    result.put("data",  PageUtil.listToPage(pageVo, iGetGoodsPackageList));
                    return new ResultUtil<>().setData(result, "获取App查询商品详情数据成功！");

                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }


    @PostMapping(value = "/AppGoodsPackageList")
    @ApiOperation(value = "查询商家精选套餐")
    public Result<Object> appGoodsPackageList(PageVo pageVo, String ids){
        return Optional.ofNullable(iGoodsPackageService.AppGoodsPackageList(ids,null))
                .map(iAppGoodsPackageList-> {
                    Map<String, Object> result = Maps.newHashMap();
                    result.put("size",  iAppGoodsPackageList.size());
                    result.put("data",  PageUtil.listToPage(pageVo, iAppGoodsPackageList));
                    return new ResultUtil<>().setData(result, "获取查询商家精选套餐数据成功！");

                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }


    /**
     * 查询商家推荐套餐
     * ids  店铺id
     */
    @PostMapping(value = "/AppGoodsRecommendPackageList")
    public Result<Object> AppGoodsRecommendPackageList(PageVo pageVo, String ids,String goodName){
        return Optional.ofNullable(iGoodsPackageService.AppGoodsRecommendPackageList(ids,goodName))
                .map(appGoodsRecommendPackageList-> {
                    Map<String, Object> result = Maps.newHashMap();
                    result.put("size",  appGoodsRecommendPackageList.size());
                    result.put("data",  PageUtil.listToPage(pageVo, appGoodsRecommendPackageList));
                    return new ResultUtil<>().setData(result, "查询商家推荐套餐成功！");

                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }



    /*****************************************************分割线****商家店铺******************************************************/




    /**
     * 获取App店铺详情参数
     */
    @PostMapping(value = "/getShopParticularsParameter")
    public Result<Object> getShopParticularsParameter(String ids){

        return Optional.ofNullable(iGoodsPackageService.getShopParticularsParameter(ids))
                .map(iShopParticulars-> {
                    Map<String, Object> result = Maps.newHashMap();
                    result.put("data",  iShopParticulars);
                    return new ResultUtil<>().setData(result, "获取App店铺详情参数成功！");
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
