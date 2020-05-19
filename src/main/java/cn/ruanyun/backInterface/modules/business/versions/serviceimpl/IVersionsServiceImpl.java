package cn.ruanyun.backInterface.modules.business.versions.serviceimpl;

import cn.ruanyun.backInterface.modules.business.versions.mapper.VersionsMapper;
import cn.ruanyun.backInterface.modules.business.versions.pojo.Versions;
import cn.ruanyun.backInterface.modules.business.versions.service.IVersionsService;
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
 * 设备版本接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IVersionsServiceImpl extends ServiceImpl<VersionsMapper, Versions> implements IVersionsService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateVersions(Versions versions) {

           if (ToolUtil.isEmpty(versions.getCreateBy())) {

                       versions.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       versions.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(versions)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeVersions(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }




}