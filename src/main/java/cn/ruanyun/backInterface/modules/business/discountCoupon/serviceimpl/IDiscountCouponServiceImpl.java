package cn.ruanyun.backInterface.modules.business.discountCoupon.serviceimpl;

import cn.ruanyun.backInterface.modules.business.discountCoupon.mapper.DiscountCouponMapper;
import cn.ruanyun.backInterface.modules.business.discountCoupon.pojo.DiscountCoupon;
import cn.ruanyun.backInterface.modules.business.discountCoupon.service.IDiscountCouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.CompletableFuture;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;


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
}