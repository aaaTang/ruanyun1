package cn.ruanyun.backInterface.modules.business.orderDetail.serviceimpl;

import cn.ruanyun.backInterface.modules.business.orderDetail.mapper.OrderDetailMapper;
import cn.ruanyun.backInterface.modules.business.orderDetail.pojo.OrderDetail;
import cn.ruanyun.backInterface.modules.business.orderDetail.service.IOrderDetailService;
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
 * 子订单接口实现
 * @author wj
 */
@Slf4j
@Service
@Transactional
public class IOrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateOrderDetail(OrderDetail orderDetail) {
           if (ToolUtil.isEmpty(orderDetail.getCreateBy())) {
                       orderDetail.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {
                       orderDetail.setUpdateBy(securityUtil.getCurrUser().getId());
                   }
                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(orderDetail)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeOrderDetail(String ids) {
          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }
}