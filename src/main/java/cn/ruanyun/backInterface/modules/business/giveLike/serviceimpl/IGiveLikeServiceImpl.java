package cn.ruanyun.backInterface.modules.business.giveLike.serviceimpl;

import cn.ruanyun.backInterface.modules.business.giveLike.mapper.GiveLikeMapper;
import cn.ruanyun.backInterface.modules.business.giveLike.pojo.GiveLike;
import cn.ruanyun.backInterface.modules.business.giveLike.service.IGiveLikeService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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

import javax.annotation.Resource;


/**
 * 用户点赞接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IGiveLikeServiceImpl extends ServiceImpl<GiveLikeMapper, GiveLike> implements IGiveLikeService {


       @Autowired
       private SecurityUtil securityUtil;

       @Resource
       private GiveLikeMapper giveLikeMapper;

       @Override
       public void insertOrderUpdateGiveLike(GiveLike giveLike) {

           if (ToolUtil.isEmpty(giveLike.getCreateBy())) {

                       giveLike.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       giveLike.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(giveLike)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeGiveLike(String dynamicVideoId) {

          giveLikeMapper.removeGiveLike(dynamicVideoId,securityUtil.getCurrUser().getId());
      }
}