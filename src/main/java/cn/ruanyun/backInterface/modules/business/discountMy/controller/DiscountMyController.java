package cn.ruanyun.backInterface.modules.business.discountMy.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.discountMy.pojo.DiscountMy;
import cn.ruanyun.backInterface.modules.business.discountMy.service.IDiscountMyService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author wj
 */
@Slf4j
@RestController
@Api(description = "优惠券管理接口")
@RequestMapping("/ruanyun/discountMy")
@Transactional
public class DiscountMyController {

    @Autowired
    private IDiscountMyService discountMyService;

    /**
     * 我的优惠券
     * @param userId
     * @return
     */
    @PostMapping("/getDiscountMy")
    public Result<Object> getDiscountMy(String userId, Integer status){
        return Optional.ofNullable(discountMyService.getMyCoupon(status)).map(myCoupons ->new ResultUtil<>().setData(myCoupons,"获取我的优惠券成功！"))
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }

    /**
     * 领取优惠券
     * @param couponId
     * @return
     */
    @PostMapping("/receiveDiscountMy")
    public Result<Object> receiveDiscountMy(String couponId){
        return discountMyService.receiveCoupon(couponId);
    }


    /**
     * 获取详情
     * @param id
     * @return
     */
    @PostMapping("/getAddressDetail")
    public Result<Object> getAddressDetail(String id) {
        return new ResultUtil<>().setData(discountMyService.getById(id),"获取详情成功！");
    }

    /**
     * 获取在当前商品下使用的优惠券
     * @param productId
     * @return
     */
    @PostMapping("/getCanUseCoupon")
    public Result<Object> getCanUseCoupon(String productId){
        return Optional.ofNullable(discountMyService.getCanUseCoupon(productId)).map(myCoupon -> new ResultUtil<>().setData(myCoupon,"获取可以使用的优惠券成功！"))
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }

    /**
     * 获取下单的时候，可以选择的优惠券
     * @param orderMoney
     * @param goodId
     * @return
     */
    @PostMapping("/getCanUseCouponByOrder")
    public Result<Object> getCanUseCouponByOrder(String goodId ,BigDecimal orderMoney){
        return Optional.ofNullable(discountMyService.getCanUseCouponByOrder(goodId,orderMoney)).map(myCoupon -> new ResultUtil<>().setData(myCoupon,"获取可以使用的优惠券成功！"))
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }
}
