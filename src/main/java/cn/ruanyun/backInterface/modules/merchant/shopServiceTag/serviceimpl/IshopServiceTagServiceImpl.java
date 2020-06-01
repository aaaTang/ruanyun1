package cn.ruanyun.backInterface.modules.merchant.shopServiceTag.serviceimpl;

import cn.ruanyun.backInterface.modules.merchant.shopServiceTag.mapper.shopServiceTagMapper;
import cn.ruanyun.backInterface.modules.merchant.shopServiceTag.pojo.shopServiceTag;
import cn.ruanyun.backInterface.modules.merchant.shopServiceTag.service.IshopServiceTagService;
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
 * 商家优质服务标签接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IshopServiceTagServiceImpl extends ServiceImpl<shopServiceTagMapper, shopServiceTag> implements IshopServiceTagService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateshopServiceTag(shopServiceTag shopServiceTag) {

           if (ToolUtil.isEmpty(shopServiceTag.getCreateBy())) {

                       shopServiceTag.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       shopServiceTag.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(shopServiceTag)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeshopServiceTag(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }
}