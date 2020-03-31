package cn.ruanyun.backInterface.modules.business.myCollect.serviceimpl;

import cn.ruanyun.backInterface.modules.business.myCollect.mapper.myCollectMapper;
import cn.ruanyun.backInterface.modules.business.myCollect.pojo.myCollect;
import cn.ruanyun.backInterface.modules.business.myCollect.service.ImyCollectService;
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
 * 我的收藏接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class ImyCollectServiceImpl extends ServiceImpl<myCollectMapper, myCollect> implements ImyCollectService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdatemyCollect(myCollect myCollect) {

           if (ToolUtil.isEmpty(myCollect.getCreateBy())) {

                       myCollect.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       myCollect.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(myCollect)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removemyCollect(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }
}