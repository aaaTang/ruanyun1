package cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.serviceimpl;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.mapper.SiteAuctionCalendarMapper;
import cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.pojo.SiteAuctionCalendar;
import cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.service.ISiteAuctionCalendarService;
import cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.vo.SiteAuctionCalendarVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Override
    public Result<List<SiteAuctionCalendarVo>> getSiteNoAuctionCalendar(String siteId) {

        return Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<SiteAuctionCalendar>lambdaQuery()
                .eq(SiteAuctionCalendar::getSiteId, siteId)
                .orderByDesc(SiteAuctionCalendar::getCreateTime))))
                .map(siteAuctionCalendars -> new ResultUtil<List<SiteAuctionCalendarVo>>()
                .setData(siteAuctionCalendars.parallelStream().flatMap(siteAuctionCalendar -> {

                    SiteAuctionCalendarVo siteAuctionCalendarVo = new SiteAuctionCalendarVo();
                    ToolUtil.copyProperties(siteAuctionCalendar, siteAuctionCalendarVo);

                    return Stream.of(siteAuctionCalendarVo);

                }).collect(Collectors.toList()), "获取没有档期列表成功！"))
                .orElse(new ResultUtil<List<SiteAuctionCalendarVo>>().setErrorMsg(201, "暂无数据！"));
    }
}