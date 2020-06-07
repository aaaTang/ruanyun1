package cn.ruanyun.backInterface.modules.business.kotlinDemo.serviceimpl;

import cn.ruanyun.backInterface.modules.business.kotlinDemo.mapper.KotlinDemoMapper;
import cn.ruanyun.backInterface.modules.business.kotlinDemo.pojo.KotlinDemo;
import cn.ruanyun.backInterface.modules.business.kotlinDemo.service.IKotlinDemoService;
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
 * kotlin栗子接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IKotlinDemoServiceImpl extends ServiceImpl<KotlinDemoMapper, KotlinDemo> implements IKotlinDemoService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateKotlinDemo(KotlinDemo kotlinDemo) {

           if (ToolUtil.isEmpty(kotlinDemo.getCreateBy())) {

                       kotlinDemo.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       kotlinDemo.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(kotlinDemo)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeKotlinDemo(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }
}