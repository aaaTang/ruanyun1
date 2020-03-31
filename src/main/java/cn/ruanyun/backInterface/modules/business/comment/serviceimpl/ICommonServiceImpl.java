package cn.ruanyun.backInterface.modules.business.comment.serviceimpl;

import cn.ruanyun.backInterface.modules.business.comment.mapper.CommonMapper;
import cn.ruanyun.backInterface.modules.business.comment.pojo.Common;
import cn.ruanyun.backInterface.modules.business.comment.service.ICommonService;
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
 * 评价接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class ICommonServiceImpl extends ServiceImpl<CommonMapper, Common> implements ICommonService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateCommon(Common common) {

           if (ToolUtil.isEmpty(common.getCreateBy())) {

                       common.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       common.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(common)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeCommon(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }
}