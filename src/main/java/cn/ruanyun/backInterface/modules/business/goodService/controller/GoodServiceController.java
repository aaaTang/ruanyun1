package cn.ruanyun.backInterface.modules.business.goodService.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.goodService.pojo.GoodService;
import cn.ruanyun.backInterface.modules.business.goodService.service.IGoodServiceService;
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
 * 商品服务管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/goodService")
@Transactional
public class GoodServiceController {

    @Autowired
    private IGoodServiceService iGoodServiceService;


   /**
     * 更新或者插入数据
     * @param goodService
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateGoodService")
    public Result<Object> insertOrderUpdateGoodService(GoodService goodService){

        try {

            iGoodServiceService.insertOrderUpdateGoodService(goodService);
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
    @PostMapping(value = "/removeGoodService")
    public Result<Object> removeGoodService(String ids){

        try {

            iGoodServiceService.removeGoodService(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

    /**
     * 按商品获取服务类型数据
     * @param goodsId
     * @return
     */
    @PostMapping(value = "/getGoodsServiceList")
    public Result<Object> getGoodsServiceList(String goodsId){

        return Optional.ofNullable(iGoodServiceService.getGoodsServiceList(goodsId))
                .map(goodServer -> {

                    Map<String,Object> result = Maps.newHashMap();
                    result.put("data",goodServer);
                    return new ResultUtil<>().setData(result,"按商品获取服务类型数据成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }



    /**
     * 获取商家自己的公用服务数据
     * @return
     */
    @PostMapping(value = "/getShopServiceList")
    public Result<Object> getShopServiceList(){

        return Optional.ofNullable(iGoodServiceService.getShopServiceList())
                .map(goodServer -> {

                    Map<String,Object> result = Maps.newHashMap();
                    result.put("data",goodServer);
                    return new ResultUtil<>().setData(result,"获取商家自己的公用服务数据成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }


}
