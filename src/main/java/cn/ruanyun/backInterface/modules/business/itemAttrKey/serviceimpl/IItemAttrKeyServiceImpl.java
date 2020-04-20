package cn.ruanyun.backInterface.modules.business.itemAttrKey.serviceimpl;

import cn.ruanyun.backInterface.modules.business.itemAttrKey.mapper.ItemAttrKeyMapper;
import cn.ruanyun.backInterface.modules.business.itemAttrKey.pojo.ItemAttrKey;
import cn.ruanyun.backInterface.modules.business.itemAttrKey.service.IItemAttrKeyService;
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
 * 规格管理接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IItemAttrKeyServiceImpl extends ServiceImpl<ItemAttrKeyMapper, ItemAttrKey> implements IItemAttrKeyService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateItemAttrKey(ItemAttrKey itemAttrKey) {

           if (ToolUtil.isEmpty(itemAttrKey.getCreateBy())) {

                       itemAttrKey.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       itemAttrKey.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(itemAttrKey)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeItemAttrKey(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }
}