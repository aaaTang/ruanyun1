package cn.ruanyun.backInterface.modules.business.platformServicer.serviceimpl;

import cn.ruanyun.backInterface.modules.business.platformServicer.mapper.PlatformServicerMapper;
import cn.ruanyun.backInterface.modules.business.platformServicer.pojo.PlatformServicer;
import cn.ruanyun.backInterface.modules.business.platformServicer.service.IPlatformServicerService;
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
 * 平台客服接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IPlatformServicerServiceImpl extends ServiceImpl<PlatformServicerMapper, PlatformServicer> implements IPlatformServicerService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdatePlatformServicer(PlatformServicer platformServicer) {

           if (ToolUtil.isEmpty(platformServicer.getCreateBy())) {

                       platformServicer.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       platformServicer.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(platformServicer)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removePlatformServicer(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }
}