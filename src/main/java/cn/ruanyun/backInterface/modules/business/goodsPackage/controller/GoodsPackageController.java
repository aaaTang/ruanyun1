package cn.ruanyun.backInterface.modules.business.goodsPackage.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.goodsPackage.pojo.GoodsPackage;
import cn.ruanyun.backInterface.modules.business.goodsPackage.service.IGoodsPackageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

}
