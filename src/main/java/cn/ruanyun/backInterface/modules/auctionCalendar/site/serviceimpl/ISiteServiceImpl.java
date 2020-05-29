package cn.ruanyun.backInterface.modules.auctionCalendar.site.serviceimpl;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.mapper.SiteMapper;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.pojo.Site;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.service.ISiteService;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.vo.SiteDetailVo;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.vo.SiteListVo;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.service.IItemAttrValService;
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
 * 场地接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class ISiteServiceImpl extends ServiceImpl<SiteMapper, Site> implements ISiteService {


    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private IItemAttrValService iItemAttrValService;

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

    @Override
    public Result<List<SiteListVo>> getSiteList(String storeId) {

        return Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<Site>lambdaQuery()
        .eq(Site::getCreateBy, storeId)
        .orderByDesc(Site::getCreateTime))))
        .map(sites -> new ResultUtil<List<SiteListVo>>().setData(sites.parallelStream().flatMap(site -> {

            SiteListVo siteListVo = new SiteListVo();
            ToolUtil.copyProperties(site, siteListVo);
            siteListVo.setSiteItemValue(Optional.ofNullable(ToolUtil.setListToNul(iItemAttrValService
            .getItemAttrVals(site.getSiteItemValue()))).map(itemValues -> itemValues.get(0))
            .orElse("-"));

            return Stream.of(siteListVo);
        }).collect(Collectors.toList()), "获取门店场地信息成功！"))
        .orElse(new ResultUtil<List<SiteListVo>>().setErrorMsg(201, "暂无数据！"));
    }

    /**
     * 查询场地详情
     *
     * @param id id
     * @return SiteDetailVo
     */
    @Override
    public Result<SiteDetailVo> getSiteDetail(String id) {

        return Optional.ofNullable(this.getById(id)).map(site -> {

            SiteDetailVo siteDetailVo = new SiteDetailVo();
            siteDetailVo.setSiteItemValues(iItemAttrValService.getItemAttrVals(site.getSiteItemValue()))
                    .setSitePics(ToolUtil.splitterStr(site.getSitePics()));

            return new ResultUtil<SiteDetailVo>().setData(siteDetailVo, "获取场地信息成功！");
        }).orElse(new ResultUtil<SiteDetailVo>().setErrorMsg(201, "暂无场地信息"));
    }

}