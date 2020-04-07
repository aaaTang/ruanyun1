package cn.ruanyun.backInterface.modules.business.discountCoupon.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.discountCoupon.pojo.DiscountCoupon;
import cn.ruanyun.backInterface.modules.business.discountCoupon.service.IDiscountCouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author fei
 * 优惠券管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/discountCoupon")
@Transactional
public class DiscountCouponController {

    @Autowired
    private IDiscountCouponService iDiscountCouponService;


   /**
     * 更新或者插入数据
     * @param discountCoupon
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateDiscountCoupon")
    public Result<Object> insertOrderUpdateDiscountCoupon(DiscountCoupon discountCoupon){
        try {
            iDiscountCouponService.insertOrderUpdateDiscountCoupon(discountCoupon);
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
    @PostMapping(value = "/removeDiscountCoupon")
    public Result<Object> removeDiscountCoupon(String ids){
        try {
            iDiscountCouponService.removeDiscountCoupon(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {
            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

    /**
     * 后台管理优惠券
     * @param id
     * @return
     */



    /**
     * 优惠券详情
     * @param id
     * @return
     */
    @PostMapping("/getDiscountCouponDetail")
    public Result<Object> getDiscountCouponDetail(String id) {
        return new ResultUtil<>().setData(iDiscountCouponService.getById(id),"获取详情成功！");
    }


    /**
     * 商品获取优惠券
     * @param goodsPackageId
     * @return
     */
    @PostMapping("/getDiscountCouponListByGoodsPackageId")
    public Result<Object> getDiscountCouponListByGoodsPackageId(String goodsPackageId) {
        return Optional.ofNullable(iDiscountCouponService.getDiscountCouponListByGoodsPackageId(goodsPackageId))
                .map(discountCouponList -> new ResultUtil<>().setData(discountCouponList,"获取列表成功！"))
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }


}
