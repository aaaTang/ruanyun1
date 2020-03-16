package cn.ruanyun.backInterface.modules.business.discountCoupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.discountCoupon.pojo.DiscountCoupon;

import java.util.List;

/**
 * 优惠券接口
 * @author fei
 */
public interface IDiscountCouponService extends IService<DiscountCoupon> {


      /**
        * 插入或者更新discountCoupon
        * @param discountCoupon
       */
     void insertOrderUpdateDiscountCoupon(DiscountCoupon discountCoupon);



      /**
       * 移除discountCoupon
       * @param ids
       */
     void removeDiscountCoupon(String ids);
}