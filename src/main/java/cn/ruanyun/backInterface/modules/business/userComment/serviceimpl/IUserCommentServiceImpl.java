package cn.ruanyun.backInterface.modules.business.userComment.serviceimpl;

import cn.ruanyun.backInterface.modules.business.userComment.mapper.UserCommentMapper;
import cn.ruanyun.backInterface.modules.business.userComment.pojo.UserComment;
import cn.ruanyun.backInterface.modules.business.userComment.service.IUserCommentService;
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
 * 用户评论接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IUserCommentServiceImpl extends ServiceImpl<UserCommentMapper, UserComment> implements IUserCommentService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateUserComment(UserComment userComment) {

           if (ToolUtil.isEmpty(userComment.getCreateBy())) {

                       userComment.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       userComment.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(userComment)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeUserComment(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }
}