package cn.ruanyun.backInterface.modules.business.discountMy.service;

import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.discountMy.VO.DiscountVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.discountMy.pojo.DiscountMy;

import java.math.BigDecimal;
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

    /***
     *
     * @param discountCouponId
     * @return
     */
    DiscountVO getDetailById(String discountCouponId);

    /***
     *
     * @param id
     * @param goodId
     * @param multiply
     * @return
     */
    List<DiscountVO> getDealCanUseCoupon(String id, String goodId, BigDecimal multiply);


    /**
     * 改变我的优惠券为已使用的状态
     * @param disCouponId 优惠券id
     */
    void changeMyDisCouponStatus(String disCouponId, String userId);

}