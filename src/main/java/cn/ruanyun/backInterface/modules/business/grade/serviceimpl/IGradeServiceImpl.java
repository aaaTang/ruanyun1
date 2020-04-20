package cn.ruanyun.backInterface.modules.business.grade.serviceimpl;

import cn.ruanyun.backInterface.modules.business.grade.mapper.GradeMapper;
import cn.ruanyun.backInterface.modules.business.grade.pojo.Grade;
import cn.ruanyun.backInterface.modules.business.grade.service.IGradeService;
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
 * 评分接口实现
 * @author wj
 */
@Slf4j
@Service
@Transactional
public class IGradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements IGradeService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateGrade(Grade grade) {

           if (ToolUtil.isEmpty(grade.getCreateBy())) {

                       grade.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       grade.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(grade)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeGrade(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }
}