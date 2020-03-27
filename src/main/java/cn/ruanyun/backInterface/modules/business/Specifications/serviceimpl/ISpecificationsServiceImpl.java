package cn.ruanyun.backInterface.modules.business.Specifications.serviceimpl;

import cn.ruanyun.backInterface.modules.business.Specifications.mapper.SpecificationsMapper;
import cn.ruanyun.backInterface.modules.business.Specifications.pojo.Specifications;
import cn.ruanyun.backInterface.modules.business.Specifications.service.ISpecificationsService;
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
 * 商品规格接口实现
 * @author zhu
 */
@Slf4j
@Service
@Transactional
public class ISpecificationsServiceImpl extends ServiceImpl<SpecificationsMapper, Specifications> implements ISpecificationsService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateSpecifications(Specifications specifications) {

           if (ToolUtil.isEmpty(specifications.getCreateBy())) {

                       specifications.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       specifications.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(specifications)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeSpecifications(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }
}