package cn.ruanyun.backInterface.modules.business.discountMy.service;

import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.discountMy.VO.DiscountVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.discountMy.pojo.DiscountMy;

import java.util.List;

/**
 * 我领取的优惠券接口
 * @author wj
 */
public interface IDiscountMyService extends IService<DiscountMy> {

    /**
     * 获取我的优惠券
     * @param status
     * @return
     */
    List<DiscountVO> getMyCoupon(Integer status);


    /**
     * 领取优惠券
     * @param couponId
     */
    Result<Object> receiveCoupon(String couponId);


    /**
     * 获取当前商品可以使用的优惠券
     * @param productId
     * @return
     */
    List<DiscountVO> getCanUseCoupon(String productId);

}