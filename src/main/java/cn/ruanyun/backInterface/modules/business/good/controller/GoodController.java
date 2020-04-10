package cn.ruanyun.backInterface.modules.business.good.controller;

import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.good.DTO.GoodDTO;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

/**
 * @author fei
 * 商品管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/good")
public class GoodController {

    @Autowired
    private IGoodService iGoodService;


   /**
     * 更新或者插入数据 普通商品
     * @param good
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateGood")
    public Result<Object> insertOrderUpdateGood(Good good){
        try {
            good.setTypeEnum(GoodTypeEnum.GOOD);
            iGoodService.insertOrderUpdateGood(good);
            return new ResultUtil<>().setSuccessMsg("插入或者更新成功!");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

    /**
     * 更新或者插入数据 套餐商品
     * @param good
     * @return
     */
    @PostMapping(value = "/insertOrderUpdateGoodsPackage")
    public Result<Object> insertOrderUpdateGoodsPackage(Good good){
        try {
            good.setTypeEnum(GoodTypeEnum.GOODSPACKAGE);
            iGoodService.insertOrderUpdateGood(good);
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
    @PostMapping(value = "/removeGood")
    public Result<Object> removeGood(String ids){
        try {
            iGoodService.removeGood(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {
            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 获取 普通商品列表
     * @param goodDTO
     * @param pageVo
     * @return
     */
    @PostMapping("/getAppGoodList")
    public Result<Object> getAppGoodList(GoodDTO goodDTO, PageVo pageVo) {
        goodDTO.setGoodTypeEnum(GoodTypeEnum.GOOD);
        return Optional.ofNullable(iGoodService.getAppGoodList(goodDTO))
                .map(goodListVOS -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",goodListVOS.size());
                    result.put("data", PageUtil.listToPage(pageVo,goodListVOS));
                    return new ResultUtil<>().setData(result,"获取商品列表成功！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }


    /**
     * 获取商品详情
     * @param id
     * @return
     */
    @PostMapping("/getGoodDetailVO")
    public Result<Object> getGoodDetailVO(String id) {
        return Optional.ofNullable(iGoodService.getById(id))
                .map(good -> new ResultUtil<>().setData(iGoodService.getAppGoodDetail(id),"获取商品详情成功！"))
                .orElse(new ResultUtil<>().setErrorMsg(201,"不存在该商品！"));
    }


    /**
     * 获取商品参数信息
     * @param id
     * @return
     */
    @PostMapping("/getAppGoodInfo")
    public Result<Object> getAppGoodInfo(String id) {
        return Optional.ofNullable(iGoodService.getAppGoodInfo(id))
                .map(appGoodInfoVO -> new ResultUtil<>().setData(appGoodInfoVO, "获取商品信息数据成功！"))
                .orElse(new ResultUtil<>().setErrorMsg(201, "不存在该数据！"));
    }





    /**
     * app获取商家详情商品列表
     * @param goodDTO
     * @param pageVo
     * @return
     */
    /*@PostMapping("/getAppGoodList")
    public Result<Object> getAppGoodList(GoodDTO goodDTO, PageVo pageVo) {

        return Optional.ofNullable(iGoodService.getAppGoodList(goodDTO))
                .map(goodListVOS -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",goodListVOS.size());
                    result.put("data", PageUtil.listToPage(pageVo,goodListVOS));
                    return new ResultUtil<>().setData(result,"获取数据成功！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }*/


    /**
     * PC获取商家的商品列表
     * @param pageVo
     * @return
     */
     @PostMapping("/PCgoodsList")
        public Result<Object> PCgoodsList(PageVo pageVo) {
            return Optional.ofNullable(iGoodService.PCgoodsList())
                    .map(goodsList -> {
                        Map<String,Object> result = Maps.newHashMap();
                        result.put("size",goodsList.size());
                        result.put("data", PageUtil.listToPage(pageVo,goodsList));
                        return new ResultUtil<>().setData(result,"获取商家的商品成功！");
                    })
                    .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
        }


}
