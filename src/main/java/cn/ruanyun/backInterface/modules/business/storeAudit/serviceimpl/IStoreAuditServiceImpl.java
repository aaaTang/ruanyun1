package cn.ruanyun.backInterface.modules.business.storeAudit.serviceimpl;

import cn.ruanyun.backInterface.modules.business.storeAudit.DTO.StoreAuditDTO;
import cn.ruanyun.backInterface.modules.business.storeAudit.VO.StoreAuditVO;
import cn.ruanyun.backInterface.modules.business.storeAudit.mapper.StoreAuditMapper;
import cn.ruanyun.backInterface.modules.business.storeAudit.pojo.StoreAudit;
import cn.ruanyun.backInterface.modules.business.storeAudit.service.IStoreAuditService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;


/**
 * 商家入驻审核接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IStoreAuditServiceImpl extends ServiceImpl<StoreAuditMapper, StoreAudit> implements IStoreAuditService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateStoreAudit(StoreAudit storeAudit) {

           if (ToolUtil.isEmpty(storeAudit.getCreateBy())) {

                       storeAudit.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       storeAudit.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(storeAudit)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeStoreAudit(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    @Override
    public void checkStoreAudit(StoreAuditDTO storeAuditDTO) {

    }

    @Override
    public List<StoreAuditVO> getStoreAuditList() {
        return null;
    }
}