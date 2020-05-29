package cn.ruanyun.backInterface.modules.business.discountShop.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.discountShop.pojo.DiscountShop;
import cn.ruanyun.backInterface.modules.business.discountShop.service.IDiscountShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author z
 * 优惠券参加的商家管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/discountShop")
@Transactional
public class DiscountShopController {

    @Autowired
    private IDiscountShopService iDiscountShopService;


   /**
     * 更新或者插入数据
     * @param discountShop
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateDiscountShop")
    public Result<Object> insertOrderUpdateDiscountShop(DiscountShop discountShop){

        try {

            iDiscountShopService.insertOrderUpdateDiscountShop(discountShop);
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
    @PostMapping(value = "/removeDiscountShop")
    public Result<Object> removeDiscountShop(String ids){

        try {

            iDiscountShopService.removeDiscountShop(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }





}
