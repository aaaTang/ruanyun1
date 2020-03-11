package cn.ruanyun.backInterface.modules.business.advertising.serviceimpl;

import cn.ruanyun.backInterface.modules.business.advertising.mapper.AdvertisingMapper;
import cn.ruanyun.backInterface.modules.business.advertising.pojo.Advertising;
import cn.ruanyun.backInterface.modules.business.advertising.service.IAdvertisingService;
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
 * 广告管理接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IAdvertisingServiceImpl extends ServiceImpl<AdvertisingMapper, Advertising> implements IAdvertisingService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateAdvertising(Advertising advertising) {

           if (ToolUtil.isEmpty(advertising.getCreateBy())) {

                       advertising.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       advertising.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(advertising)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeAdvertising(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }
}