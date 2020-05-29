package cn.ruanyun.backInterface.modules.business.discountShop.serviceimpl;

import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.modules.business.discountShop.mapper.DiscountShopMapper;
import cn.ruanyun.backInterface.modules.business.discountShop.pojo.DiscountShop;
import cn.ruanyun.backInterface.modules.business.discountShop.service.IDiscountShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CompletableFuture;


/**
 * 优惠券参加的商家接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IDiscountShopServiceImpl extends ServiceImpl<DiscountShopMapper, DiscountShop> implements IDiscountShopService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateDiscountShop(DiscountShop discountShop) {

           if (ToolUtil.isEmpty(discountShop.getCreateBy())) {

                       discountShop.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       discountShop.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(discountShop)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeDiscountShop(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }
}