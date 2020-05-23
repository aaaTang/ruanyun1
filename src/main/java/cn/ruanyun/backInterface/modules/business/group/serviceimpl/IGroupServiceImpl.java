package cn.ruanyun.backInterface.modules.business.group.serviceimpl;

import cn.ruanyun.backInterface.modules.business.group.mapper.GroupMapper;
import cn.ruanyun.backInterface.modules.business.group.pojo.Group;
import cn.ruanyun.backInterface.modules.business.group.service.IGroupService;
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
 * 群组列表接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IGroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements IGroupService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateGroup(Group group) {

           if (ToolUtil.isEmpty(group.getCreateBy())) {

                       group.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       group.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(group)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeGroup(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }
}