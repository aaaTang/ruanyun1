package cn.ruanyun.backInterface.modules.business.orderReturnReason.serviceimpl;

import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.modules.business.orderReturnReason.mapper.OrderReturnReasonMapper;
import cn.ruanyun.backInterface.modules.business.orderReturnReason.pojo.OrderReturnReason;
import cn.ruanyun.backInterface.modules.business.orderReturnReason.service.IOrderReturnReasonService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


/**
 * 退货原因接口实现
 * @author wj
 */
@Slf4j
@Service
@Transactional
public class IOrderReturnReasonServiceImpl extends ServiceImpl<OrderReturnReasonMapper, OrderReturnReason> implements IOrderReturnReasonService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateOrderReturnReason(OrderReturnReason orderReturnReason) {
           if (ToolUtil.isEmpty(orderReturnReason.getCreateBy())) {
               orderReturnReason.setCreateBy(securityUtil.getCurrUser().getId());
           } else {
               orderReturnReason.setUpdateBy(securityUtil.getCurrUser().getId());
           }
           Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(orderReturnReason)))
                   .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                   .toFuture().join();
       }

      @Override
      public void removeOrderReturnReason(String ids) {
          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    /**
     * 获取退款
     * @param orderReturnReason
     * @return
     */
    @Override
    public List getOrderReturnReasonList(OrderReturnReason orderReturnReason) {
        return Optional.ofNullable(ToolUtil.setListToNul(this.list())).orElse(null);
    }
}