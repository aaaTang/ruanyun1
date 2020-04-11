package cn.ruanyun.backInterface.modules.business.itemAttrVal.serviceimpl;

import cn.ruanyun.backInterface.modules.business.itemAttrVal.mapper.ItemAttrValMapper;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.pojo.ItemAttrVal;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.service.IItemAttrValService;
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
 * 规格属性管理接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IItemAttrValServiceImpl extends ServiceImpl<ItemAttrValMapper, ItemAttrVal> implements IItemAttrValService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateItemAttrVal(ItemAttrVal itemAttrVal) {

           if (ToolUtil.isEmpty(itemAttrVal.getCreateBy())) {

                       itemAttrVal.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       itemAttrVal.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(itemAttrVal)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeItemAttrVal(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }
}