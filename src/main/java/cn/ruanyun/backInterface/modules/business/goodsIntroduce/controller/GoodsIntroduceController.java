package cn.ruanyun.backInterface.modules.business.goodsIntroduce.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.goodsIntroduce.pojo.GoodsIntroduce;
import cn.ruanyun.backInterface.modules.business.goodsIntroduce.service.IGoodsIntroduceService;
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
 * 商品介绍管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/goodsIntroduce")
@Transactional
public class GoodsIntroduceController {

    @Autowired
    private IGoodsIntroduceService iGoodsIntroduceService;


   /**
     * 更新或者插入数据
     * @param goodsIntroduce
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateGoodsIntroduce")
    public Result<Object> insertOrderUpdateGoodsIntroduce(GoodsIntroduce goodsIntroduce){

        try {

            iGoodsIntroduceService.insertOrderUpdateGoodsIntroduce(goodsIntroduce);
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
    @PostMapping(value = "/removeGoodsIntroduce")
    public Result<Object> removeGoodsIntroduce(String ids){

        try {

            iGoodsIntroduceService.removeGoodsIntroduce(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }



    /**
     * 按商品id查询商品介绍
     * @param ids
     * @param goodsPackageId  套餐id
     * @param introduceAndDuy   1商品线详情   2 购买须知
     * @return
     */
    @PostMapping(value = "/goodsIntroduceList")
    public Result<Object> goodsIntroduceList(String ids,String goodsPackageId,Integer introduceAndDuy){
        return Optional.ofNullable(iGoodsIntroduceService.goodsIntroduceList(ids,goodsPackageId,introduceAndDuy))
                .map(goodsIntroduceList-> {
                    Map<String, Object> result = Maps.newHashMap();
                    result.put("size",  goodsIntroduceList.size());
                    result.put("data", goodsIntroduceList);
                    return new ResultUtil<>().setData(result, "获取套餐数据成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }

}
