package cn.ruanyun.backInterface.modules.business.discountCoupon.service;

import cn.ruanyun.backInterface.modules.business.discountCoupon.VO.DiscountCouponListVO;
import cn.ruanyun.backInterface.modules.business.discountMy.VO.DiscountVO;
import cn.ruanyun.backInterface.modules.business.harvestAddress.VO.HarvestAddressVO;
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
     *
     * @param discountCoupon
     */
    void insertOrderUpdateDiscountCoupon(DiscountCoupon discountCoupon);


    /**
     * 移除discountCoupon
     *
     * @param ids
     */
    void removeDiscountCoupon(String ids);


    /**
     * 获取优惠券详情
     * @param id
     * @return
     */
    DiscountCoupon getDiscountCouponDetail(String id);

    /**
     * 回去优惠券
     * @return
     */
    List<DiscountCoupon> getDiscountCouponList(DiscountCoupon discountCoupon);

    /**
     * 商家获取添加的优惠券
     * @return
     */
    List<DiscountCoupon> getDiscountCouponListByStoreAuditOid(String storeAuditOid);

    /**
     * 获取商品的优惠券
     * @return
     */
     List<DiscountCouponListVO> getDiscountCouponListByGoodsPackageId(String goodsPackageId);

    List<DiscountVO> getList(String join);

    List<DiscountCouponListVO>  getDiscountCouponListByCreateBy(String createBy);

    /**
     * 后端获取优惠券列表
     * @return
     */
    List PcGetDiscountCouponList(String id);
}