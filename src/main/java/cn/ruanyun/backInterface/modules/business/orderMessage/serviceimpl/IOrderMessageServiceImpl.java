package cn.ruanyun.backInterface.modules.business.orderMessage.serviceimpl;

import cn.ruanyun.backInterface.modules.business.orderMessage.mapper.OrderMessageMapper;
import cn.ruanyun.backInterface.modules.business.orderMessage.pojo.OrderMessage;
import cn.ruanyun.backInterface.modules.business.orderMessage.service.IOrderMessageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;


/**
 * 订单消息接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IOrderMessageServiceImpl extends ServiceImpl<OrderMessageMapper, OrderMessage> implements IOrderMessageService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateOrderMessage(OrderMessage orderMessage) {

           if (ToolUtil.isEmpty(orderMessage.getCreateBy())) {

                       orderMessage.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       orderMessage.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(orderMessage)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeOrderMessage(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }


    @Override
    public List getOrderMessage() {
        return Optional.ofNullable(this.list(new QueryWrapper<OrderMessage>().lambda()
                .eq(OrderMessage::getCreateBy,securityUtil.getCurrUser().getId())
                .orderByDesc(OrderMessage::getCreateTime)
        )).orElse(null);
    }


}