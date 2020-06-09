package cn.ruanyun.backInterface.modules.auctionCalendar.site.serviceimpl;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.DayTimeTypeEnum;
import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.mapper.SiteMapper;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.pojo.Site;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.service.ISiteService;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.vo.BackSiteListVo;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.vo.SiteDetailTimeVO;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.vo.SiteDetailVo;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.vo.SiteListVo;
import cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.mapper.SiteAuctionCalendarMapper;
import cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.pojo.SiteAuctionCalendar;
import cn.ruanyun.backInterface.modules.auctionCalendar.sitePrice.mapper.SitePriceMapper;
import cn.ruanyun.backInterface.modules.auctionCalendar.sitePrice.pojo.SitePrice;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.service.IItemAttrValService;
import cn.ruanyun.backInterface.modules.business.order.mapper.OrderMapper;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;
import cn.ruanyun.backInterface.modules.business.orderDetail.pojo.OrderDetail;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Resource;


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

    @Resource
    private SitePriceMapper sitePriceMapper;

    @Resource
    private SiteAuctionCalendarMapper siteAuctionCalendarMapper;

    @Resource
    private OrderMapper orderMapper;

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
            siteListVo.setSiteItemValue(iItemAttrValService.getItemAttrValVo(site.getSiteItemValue()))
                    .setSitePic(Optional.ofNullable(ToolUtil.setListToNul(ToolUtil.splitterStr(site.getSitePics())))
                    .map(pics -> pics.get(0)).orElse("-"));

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
            ToolUtil.copyProperties(site,siteDetailVo);
            siteDetailVo.setSiteItemValues(iItemAttrValService.getItemAttrVals(site.getSiteItemValue()))
                    .setSitePics(ToolUtil.splitterStr(site.getSitePics()));

            return new ResultUtil<SiteDetailVo>().setData(siteDetailVo, "获取场地信息成功！");
        }).orElse(new ResultUtil<SiteDetailVo>().setErrorMsg(201, "暂无场地信息"));
    }

    @Override
    public Result<DataVo<BackSiteListVo>> getBackSiteListVo(PageVo pageVo) {

        Page<Site> sitePage = this.page(PageUtil.initMpPage(pageVo), Wrappers.<Site>lambdaQuery()
                .eq(Site::getCreateBy, securityUtil.getCurrUser().getId())
        .orderByDesc(Site::getCreateTime));

        if (ToolUtil.isEmpty(sitePage.getRecords())) {

            return new ResultUtil<DataVo<BackSiteListVo>>().setErrorMsg(201, "暂无数据！");
        }

        DataVo<BackSiteListVo> result = new DataVo<>();

        result.setDataResult(sitePage.getRecords().parallelStream().flatMap(site -> {

            BackSiteListVo backSiteListVo = new BackSiteListVo();

            ToolUtil.copyProperties(site, backSiteListVo);
            backSiteListVo.setSiteItemValue(iItemAttrValService.getItemAttrValVo(site.getSiteItemValue()));
            backSiteListVo.setSitePics(Optional.ofNullable(site.getSitePics())
            .map(ToolUtil::splitterStr)
            .orElse(null));

            return Stream.of(backSiteListVo);
        }).collect(Collectors.toList())).setTotalSize(sitePage.getTotal())
                .setCurrentPageNum(sitePage.getCurrent())
                .setTotalPage(sitePage.getPages());

        return new ResultUtil<DataVo<BackSiteListVo>>().setData(result, "获取场所数据成功！");
    }

    /**
     * App按分类获取场地列表
     * @param categoryId 分类id
     * @return
     */
    @Override
    public Result<List<SiteListVo>> AppGetCategorySiteList(String categoryId) {

        return Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<Site>lambdaQuery()
                .eq(ToolUtil.isNotEmpty(categoryId),Site::getCategoryId, categoryId)
                .eq(Site::getDelFlag, CommonConstant.STATUS_NORMAL)
                .orderByDesc(Site::getCreateTime))))
                .map(sites -> new ResultUtil<List<SiteListVo>>().setData(sites.parallelStream().flatMap(site -> {

                    SiteListVo siteListVo = new SiteListVo();
                    ToolUtil.copyProperties(site, siteListVo);
                    siteListVo.setSiteItemValue(iItemAttrValService.getItemAttrValVo(site.getSiteItemValue()))
                            .setSitePic(Optional.ofNullable(ToolUtil.setListToNul(ToolUtil.splitterStr(site.getSitePics())))
                                    .map(pics -> pics.get(0)).orElse("-"));

                    return Stream.of(siteListVo);
                }).collect(Collectors.toList()), "App按分类获取场地列表成功！"))
                .orElse(new ResultUtil<List<SiteListVo>>().setErrorMsg(201, "暂无数据！"));
    }

    /**
     * 查询场地某个时间段是否有档期时间
     * @param siteId  场地id
     * @param scheduleTime  时间
     * @return
     */
    @Override
    public List<SiteDetailTimeVO> getSiteDetailTime(String siteId, String scheduleTime) {

        List<SiteDetailTimeVO> siteDetailTimeVOS = new ArrayList<>();

        //1.先设置俩个时间段，上午和下午，以及默认价格
        SiteDetailTimeVO siteDetailTimeVO = new SiteDetailTimeVO();
        siteDetailTimeVO.setDayTimeType(DayTimeTypeEnum.A_M).setStatus(1).setSitePrice(Optional.ofNullable(this.getOne(Wrappers.<Site>lambdaQuery()
                .eq(Site::getId,siteId).eq(Site::getDelFlag,CommonConstant.STATUS_NORMAL)
        )).map(Site::getSitePrice).orElse(null));
        siteDetailTimeVOS.add(siteDetailTimeVO);


        SiteDetailTimeVO siteDetailTimeVO1 = new SiteDetailTimeVO();
        siteDetailTimeVO1.setDayTimeType(DayTimeTypeEnum.P_M).setStatus(1).setSitePrice(Optional.ofNullable(this.getOne(Wrappers.<Site>lambdaQuery()
                .eq(Site::getId,siteId).eq(Site::getDelFlag,CommonConstant.STATUS_NORMAL)
        )).map(Site::getSitePrice).orElse(null));
        siteDetailTimeVOS.add(siteDetailTimeVO1);


        //2.查看特殊节日的价格
       List<SitePrice> sitePrices =  sitePriceMapper.selectList(new QueryWrapper<SitePrice>().lambda()
                    .eq(SitePrice::getScheduleTime,scheduleTime)
                    .eq(SitePrice::getSiteId,siteId)
                    .eq(SitePrice::getDelFlag,CommonConstant.STATUS_NORMAL)
       );

       //3.查看是否有档期
        List<SiteAuctionCalendar>  siteAuctionCalendars = siteAuctionCalendarMapper.selectList(new QueryWrapper<SiteAuctionCalendar>().lambda()
                .eq(SiteAuctionCalendar::getNoScheduleTime,scheduleTime)
                .eq(SiteAuctionCalendar::getSiteId,siteId)
                .eq(SiteAuctionCalendar::getDelFlag,CommonConstant.STATUS_NORMAL)
        );


        for (SiteDetailTimeVO detailTimeVO : siteDetailTimeVOS) {

            //2.1 如果有特殊的价格就重新赋值价格
            for (SitePrice sitePrice : sitePrices) {

                if(detailTimeVO.getDayTimeType().equals(sitePrice.getDayTimeType())){

                    detailTimeVO.setSitePrice(sitePrice.getSitePrice());
                }

            }

            //3.1 如果没有档期时间就赋值状态0 == 无
          for (SiteAuctionCalendar siteAuctionCalendar : siteAuctionCalendars) {

                if(detailTimeVO.getDayTimeType().equals(siteAuctionCalendar.getDayTimeType())){

                    detailTimeVO.setStatus(0);
                }

            }

            //4.判断订单被购买
            Optional.ofNullable(orderMapper.selectById(new QueryWrapper<Order>().lambda()
                    .eq(Order::getSiteId,siteId)
                    .eq(Order::getDayTimeType,detailTimeVO.getDayTimeType())
                    .eq(Order::getScheduleAppointment,scheduleTime)
                    .ge(Order::getOrderStatus, OrderStatusEnum.PRE_SEND)
            )).ifPresent(order -> { detailTimeVO.setStatus(0); });


        }


        return siteDetailTimeVOS;
    }


}