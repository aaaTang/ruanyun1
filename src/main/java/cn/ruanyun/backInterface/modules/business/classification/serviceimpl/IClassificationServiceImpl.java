package cn.ruanyun.backInterface.modules.business.classification.serviceimpl;

import cn.ruanyun.backInterface.modules.business.classification.mapper.ClassificationMapper;
import cn.ruanyun.backInterface.modules.business.classification.pojo.Classification;
import cn.ruanyun.backInterface.modules.business.classification.service.IClassificationService;
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
 * 分类管理接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IClassificationServiceImpl extends ServiceImpl<ClassificationMapper, Classification> implements IClassificationService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateClassification(Classification classification) {

           if (ToolUtil.isEmpty(classification.getCreateBy())) {

                       classification.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       classification.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(classification)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeClassification(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }
}