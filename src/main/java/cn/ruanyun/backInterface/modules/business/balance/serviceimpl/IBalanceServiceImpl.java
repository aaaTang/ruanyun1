package cn.ruanyun.backInterface.modules.business.balance.serviceimpl;

import cn.ruanyun.backInterface.modules.business.balance.mapper.BalanceMapper;
import cn.ruanyun.backInterface.modules.business.balance.pojo.Balance;
import cn.ruanyun.backInterface.modules.business.balance.service.IBalanceService;
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
 * 余额明细接口实现
 * @author zhu
 */
@Slf4j
@Service
@Transactional
public class IBalanceServiceImpl extends ServiceImpl<BalanceMapper, Balance> implements IBalanceService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateBalance(Balance balance) {

           if (ToolUtil.isEmpty(balance.getCreateBy())) {

                       balance.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       balance.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(balance)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeBalance(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }
}