package cn.ruanyun.backInterface.modules.auctionCalendar.site.serviceimpl;

import cn.ruanyun.backInterface.modules.auctionCalendar.site.mapper.SiteMapper;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.pojo.Site;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.service.ISiteService;
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
 * 场地接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class ISiteServiceImpl extends ServiceImpl<SiteMapper, Site> implements ISiteService {


    @Autowired
    private SecurityUtil securityUtil;

    @Override
    public void insertOrderUpdateSite(Site site) {

        if (ToolUtil.isEmpty(site.getCreateBy())) {

            site.setCreateBy(securityUtil.getCurrUser().getId());
        }else {

            site.setUpdateBy(securityUtil.getCurrUser().getId());
        }


        Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(site)))
                .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                .toFuture().join();
    }

    @Override
    public void removeSite(String ids) {

        CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
    }

}