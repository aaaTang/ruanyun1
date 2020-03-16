package cn.ruanyun.backInterface.modules.business.selectedStore.serviceimpl;

import cn.ruanyun.backInterface.modules.business.selectedStore.mapper.SelectedStoreMapper;
import cn.ruanyun.backInterface.modules.business.selectedStore.pojo.SelectedStore;
import cn.ruanyun.backInterface.modules.business.selectedStore.service.ISelectedStoreService;
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
 * 严选商家接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class ISelectedStoreServiceImpl extends ServiceImpl<SelectedStoreMapper, SelectedStore> implements ISelectedStoreService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateSelectedStore(SelectedStore selectedStore) {

           if (ToolUtil.isEmpty(selectedStore.getCreateBy())) {

                       selectedStore.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       selectedStore.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(selectedStore)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeSelectedStore(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }
}