package cn.ruanyun.backInterface.modules.business.firstRateService.serviceimpl;

import cn.ruanyun.backInterface.modules.business.firstRateService.mapper.FirstRateServiceMapper;
import cn.ruanyun.backInterface.modules.business.firstRateService.pojo.FirstRateService;
import cn.ruanyun.backInterface.modules.business.firstRateService.service.IFirstRateServiceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;

import javax.swing.text.html.Option;


/**
 * 优质服务接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IFirstRateServiceServiceImpl extends ServiceImpl<FirstRateServiceMapper, FirstRateService> implements IFirstRateServiceService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateFirstRateService(FirstRateService firstRateService) {

           if (ToolUtil.isEmpty(firstRateService.getCreateBy())) {

                       firstRateService.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       firstRateService.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(firstRateService)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeFirstRateService(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    @Override
    public List<String> getFirstRateName(String ids) {

           if (ToolUtil.isEmpty(ids)) {

               return null;
           }
           return Optional.ofNullable(ToolUtil.setListToNul(this.listByIds(ToolUtil.splitterStr(ids))))
                   .map(firstRateServices -> firstRateServices.parallelStream().map(FirstRateService::getItemName)
                   .collect(Collectors.toList()))
                   .orElse(null);
    }
}