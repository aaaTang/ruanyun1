package cn.ruanyun.backInterface.modules.business.userFeedback.serviceimpl;

import cn.ruanyun.backInterface.modules.business.userFeedback.mapper.UserFeedbackMapper;
import cn.ruanyun.backInterface.modules.business.userFeedback.pojo.UserFeedback;
import cn.ruanyun.backInterface.modules.business.userFeedback.service.IUserFeedbackService;
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
 * 用户意见反馈接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IUserFeedbackServiceImpl extends ServiceImpl<UserFeedbackMapper, UserFeedback> implements IUserFeedbackService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateUserFeedback(UserFeedback userFeedback) {

           if (ToolUtil.isEmpty(userFeedback.getCreateBy())) {

                       userFeedback.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       userFeedback.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(userFeedback)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeUserFeedback(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }
}