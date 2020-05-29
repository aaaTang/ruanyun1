package cn.ruanyun.backInterface.modules.auctionCalendar.auctionCalendarOrder.serviceimpl;

import cn.ruanyun.backInterface.modules.auctionCalendar.auctionCalendarOrder.mapper.AuctionCalendarOrderMapper;
import cn.ruanyun.backInterface.modules.auctionCalendar.auctionCalendarOrder.pojo.AuctionCalendarOrder;
import cn.ruanyun.backInterface.modules.auctionCalendar.auctionCalendarOrder.service.IAuctionCalendarOrderService;
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
 * 档期订单接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IAuctionCalendarOrderServiceImpl extends ServiceImpl<AuctionCalendarOrderMapper, AuctionCalendarOrder> implements IAuctionCalendarOrderService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateAuctionCalendarOrder(AuctionCalendarOrder auctionCalendarOrder) {

           if (ToolUtil.isEmpty(auctionCalendarOrder.getCreateBy())) {

                       auctionCalendarOrder.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       auctionCalendarOrder.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(auctionCalendarOrder)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeAuctionCalendarOrder(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }
}