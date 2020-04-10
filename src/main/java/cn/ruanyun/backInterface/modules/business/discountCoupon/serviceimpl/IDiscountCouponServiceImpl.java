package cn.ruanyun.backInterface.modules.business.discountCoupon.serviceimpl;

import cn.ruanyun.backInterface.common.enums.DisCouponTypeEnum;
import cn.ruanyun.backInterface.common.enums.Disabled;
import cn.ruanyun.backInterface.common.utils.EmptyUtil;
import cn.ruanyun.backInterface.modules.business.discountCoupon.VO.DiscountCouponListVO;
import cn.ruanyun.backInterface.modules.business.discountCoupon.mapper.DiscountCouponMapper;
import cn.ruanyun.backInterface.modules.business.discountCoupon.pojo.DiscountCoupon;
import cn.ruanyun.backInterface.modules.business.discountCoupon.service.IDiscountCouponService;
import cn.ruanyun.backInterface.modules.business.discountMy.VO.DiscountVO;
import cn.ruanyun.backInterface.modules.business.discountMy.pojo.DiscountMy;
import cn.ruanyun.backInterface.modules.business.discountMy.service.IDiscountMyService;
import cn.ruanyun.backInterface.modules.business.harvestAddress.VO.HarvestAddressVO;
import cn.ruanyun.backInterface.modules.business.harvestAddress.entity.HarvestAddress;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;

import javax.swing.text.html.Option;


/**
 * 优惠券接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IDiscountCouponServiceImpl extends ServiceImpl<DiscountCouponMapper, DiscountCoupon> implements IDiscountCouponService {

       @Autowired
       private SecurityUtil securityUtil;
       @Autowired
       private IDiscountMyService discountMyService;

       @Override
       public void insertOrderUpdateDiscountCoupon(DiscountCoupon discountCoupon) {
           if (ToolUtil.isEmpty(discountCoupon.getCreateBy())) {
                       discountCoupon.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {
                       discountCoupon.setUpdateBy(securityUtil.getCurrUser().getId());
                   }
                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(discountCoupon)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeDiscountCoupon(String ids) {
          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    /**
     * 获取优惠券详情
     *
     * @param id
     * @return
     */
    @Override
    public DiscountCoupon getDiscountCouponDetail(String id) {
        return Optional.ofNullable(this.getById(id)).orElse(null);
    }

    /**
     * 回去优惠券
     *
     * @return
     */
    @Override
    public List<DiscountCoupon> getDiscountCouponList(DiscountCoupon discountCoupon) {
        CompletableFuture<Optional<List<DiscountCoupon>>> optionalCompletableFuture = CompletableFuture.supplyAsync(() ->
                Optional.ofNullable(this.list(Wrappers.<DiscountCoupon>lambdaQuery()
                        .eq(!StringUtils.isEmpty(discountCoupon.getStoreAuditOid()), DiscountCoupon::getStoreAuditOid, discountCoupon.getStoreAuditOid())
                        .eq(!StringUtils.isEmpty(discountCoupon.getGoodsPackageId()), DiscountCoupon::getGoodsPackageId, discountCoupon.getGoodsPackageId())
                        .eq(!EmptyUtil.isEmpty(discountCoupon.getDisCouponType()), DiscountCoupon::getGoodsPackageId, discountCoupon.getGoodsPackageId())
                        .orderByAsc(DiscountCoupon::getCreateTime))));
        return optionalCompletableFuture.join().orElse(null);
    }

    /**
     * 管理后台商家获取优惠券
     *
     * @return
     */
    @Override
    public List<DiscountCoupon> getDiscountCouponListByStoreAuditOid(String storeAuditOid) {
        DiscountCoupon discountCoupon = new DiscountCoupon();
        discountCoupon.setStoreAuditOid(storeAuditOid);
        return this.getDiscountCouponList(discountCoupon);
    }

    /**
     * app用户获取可以领取的优惠券
     *
     * @return
     */
    @Override
    public List<DiscountCouponListVO> getDiscountCouponListByGoodsPackageId(String goodsPackageId) {

        //获取该商品下所有的优惠券
        DiscountCoupon discountCoupon = new DiscountCoupon();
        discountCoupon.setGoodsPackageId(goodsPackageId);
        discountCoupon.setDisCouponType(DisCouponTypeEnum.ONE_PRODUCT);
        List<DiscountCoupon> discountCouponList = this.getDiscountCouponList(discountCoupon);
        List<DiscountCouponListVO> discountVOS = new ArrayList<>();
        discountCouponList.forEach(discountCoupon1 -> {
            DiscountCouponListVO discountCouponListVO = new DiscountCouponListVO();
            ToolUtil.copyProperties(discountCoupon1,discountCouponListVO);
            discountCouponListVO.setIsReceive(this.getDetailById(discountCoupon1));
            discountVOS.add(discountCouponListVO);
        });
        return discountVOS;
    }

    @Override
    public List<DiscountVO> getList(String join) {
        return Optional.ofNullable(ToolUtil.setListToNul(this.listByIds(ToolUtil.splitterStr(join)))).map(discountCouponList -> {
            List<DiscountVO> discountCoupons = discountCouponList.parallelStream().map(discountCoupon -> {
                DiscountVO discountCouponListVO = new DiscountVO();
                ToolUtil.copyProperties(discountCoupon,discountCouponListVO);
                return discountCouponListVO;
            }).collect(Collectors.toList());
            return discountCoupons;
        }).orElse(null);
    }

    //判断优惠券是否被领取
    public boolean getDetailById(DiscountCoupon discountCoupon){
        String userId = securityUtil.getCurrUser().getId();
        DiscountMy one = discountMyService.getOne(
                Wrappers.<DiscountMy>lambdaQuery()
                        .eq(DiscountMy::getCreateBy, userId)
                        .eq(DiscountMy::getDiscountCouponId, discountCoupon.getId())
        );
       return EmptyUtil.isEmpty(one);

    }
}