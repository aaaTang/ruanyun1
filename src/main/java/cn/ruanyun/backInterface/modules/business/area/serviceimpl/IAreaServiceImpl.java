package cn.ruanyun.backInterface.modules.business.area.serviceimpl;

import cn.ruanyun.backInterface.modules.business.area.mapper.AreaMapper;
import cn.ruanyun.backInterface.modules.business.area.pojo.Area;
import cn.ruanyun.backInterface.modules.business.area.service.IAreaService;
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
 * 城市管理接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IAreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements IAreaService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateArea(Area area) {

           if (ToolUtil.isEmpty(area.getCreateBy())) {
                       area.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {
                       area.setUpdateBy(securityUtil.getCurrUser().getId());
                   }
                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(area)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeArea(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }



}