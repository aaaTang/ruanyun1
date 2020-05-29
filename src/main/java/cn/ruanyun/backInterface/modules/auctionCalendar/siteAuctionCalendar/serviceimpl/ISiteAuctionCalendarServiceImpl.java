package cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.serviceimpl;

import cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.mapper.SiteAuctionCalendarMapper;
import cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.pojo.SiteAuctionCalendar;
import cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.service.ISiteAuctionCalendarService;
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
 * 场地档期接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class ISiteAuctionCalendarServiceImpl extends ServiceImpl<SiteAuctionCalendarMapper, SiteAuctionCalendar> implements ISiteAuctionCalendarService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateSiteAuctionCalendar(SiteAuctionCalendar siteAuctionCalendar) {

           if (ToolUtil.isEmpty(siteAuctionCalendar.getCreateBy())) {

                       siteAuctionCalendar.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       siteAuctionCalendar.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(siteAuctionCalendar)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeSiteAuctionCalendar(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }
}