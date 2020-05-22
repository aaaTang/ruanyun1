package cn.ruanyun.backInterface.modules.business.storeServicer.serviceimpl;

import cn.ruanyun.backInterface.modules.business.storeServicer.mapper.StoreServicerMapper;
import cn.ruanyun.backInterface.modules.business.storeServicer.pojo.StoreServicer;
import cn.ruanyun.backInterface.modules.business.storeServicer.service.IStoreServicerService;
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
 * 店铺客服接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IStoreServicerServiceImpl extends ServiceImpl<StoreServicerMapper, StoreServicer> implements IStoreServicerService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateStoreServicer(StoreServicer storeServicer) {

           if (ToolUtil.isEmpty(storeServicer.getCreateBy())) {

                       storeServicer.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       storeServicer.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(storeServicer)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeStoreServicer(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }
}