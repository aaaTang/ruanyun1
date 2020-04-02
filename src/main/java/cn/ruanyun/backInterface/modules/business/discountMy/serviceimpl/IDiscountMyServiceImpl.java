package cn.ruanyun.backInterface.modules.business.discountMy.serviceimpl;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.discountCoupon.pojo.DiscountCoupon;
import cn.ruanyun.backInterface.modules.business.discountCoupon.service.IDiscountCouponService;
import cn.ruanyun.backInterface.modules.business.discountMy.VO.DiscountVO;
import cn.ruanyun.backInterface.modules.business.discountMy.mapper.DiscountMyMapper;
import cn.ruanyun.backInterface.modules.business.discountMy.pojo.DiscountMy;
import cn.ruanyun.backInterface.modules.business.discountMy.service.IDiscountMyService;
import cn.ruanyun.backInterface.modules.business.harvestAddress.VO.HarvestAddressVO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 优惠券接口实现
 * @author wj
 */
@Slf4j
@Service
@Transactional
public class IDiscountMyServiceImpl extends ServiceImpl<DiscountMyMapper, DiscountMy> implements IDiscountMyService {
    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private IDiscountCouponService discountService;

    private static final Joiner JOINER = Joiner.on(",").skipNulls();

    @Override
    public List<DiscountVO> getMyCoupon(Integer status) {
        return Optional.ofNullable(ToolUtil.setListToNul(
                this.list(Wrappers.<DiscountMy>lambdaQuery()
                        .eq(DiscountMy::getCreateBy,securityUtil.getCurrUser().getId())
                        .orderByDesc(DiscountMy::getCreateTime)))
        ).map(list ->{
            List<DiscountVO> discountVOS = list.parallelStream().flatMap(discountMy -> {
                DiscountVO byId = this.getDetailById(discountMy.getId());
                return Stream.of(byId);
            }).collect(Collectors.toList());
            return discountVOS;
        }).orElse(null);
    }


    public DiscountVO getDetailById(String id){
        return Optional.ofNullable(this.getById(id))
                .map(discountMy -> {
                    DiscountVO discountVO = new DiscountVO();
                    DiscountCoupon byId = discountService.getById(discountMy.getDiscountCouponId());
                    ToolUtil.copyProperties(byId,discountVO);
                    discountVO.setDisCouponType(byId.getDisCouponType().getValue());
                    discountVO.setStatus(discountMy.getStatus());
                    return discountVO;
                }).orElse(null);
    }


    /**
     * 领取优惠券
     * @param couponId
     */
    @Override
    public Result<Object> receiveCoupon(String couponId) {
      return  Optional.ofNullable(this.getOne(Wrappers.<DiscountMy>lambdaQuery().eq(DiscountMy::getDiscountCouponId,couponId)
                .eq(DiscountMy::getCreateBy,securityUtil.getCurrUser().getId())))
                .map(discountMy -> new ResultUtil<>().setErrorMsg(201,"已经领取该优惠券！"))
                .orElseGet(() -> {
                    DiscountMy discountMy = new DiscountMy();
                    discountMy.setDiscountCouponId(couponId);
                    discountMy.setCreateBy(securityUtil.getCurrUser().getId());
                    this.save(discountMy);
                    return new ResultUtil<>().setSuccessMsg("领取优惠券成功！");
                });
    }

    @Override
    public List<DiscountVO> getCanUseCoupon( String productId) {
        /*return Optional.ofNullable(getCanUseCouponId(securityUtil.getCurrUser().getId(),productId)).map(strings -> discountService.getList(JOINER.join(strings)))
                .orElse(null);*/
       return  null;
    }

    public List<String> getCanUseCouponId(String userId, String productId){
       //1.先找出自己的优惠券
        List<DiscountMy> myCoupons = this.list(Wrappers.<DiscountMy>lambdaQuery()
                .eq(DiscountMy::getCreateBy,userId));
        //2.过滤出能用的优惠券
        ;
     /*   return Optional.ofNullable(ToolUtil.setListToNul(myCoupons)).map(myCouponsList -> myCouponsList.parallelStream()
                .takeWhile(myCoupon -> discountService.getOne(Wrappers.<DiscountCoupon>lambdaQuery()
                        .eq(DiscountCoupon::getId,myCoupons.get(1).getDiscountCouponId())).equals(Disabled.Yes.getCode()))
                .takeWhile(myCoupon -> judgeCouponInProduct(productId,myCoupon.())).flatMap(myCoupon -> Stream.of(myCoupon.getCode()))
                .collect(Collectors.toList())).orElse(null);*/
     return  null;
    }

    /**
     * 判断此优惠券是否可用
     * @param productId
     * @param couponId
     * @return
     */
    public Boolean judgeCouponInProduct(String productId, String couponId){
      return Optional.ofNullable(discountService.getOne(Wrappers.<DiscountCoupon>lambdaQuery()
                .eq(DiscountCoupon::getId,productId)))
                .map(productCoupon -> true).orElse(false);
    }

}