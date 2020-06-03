package cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.serviceimpl;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.VO.CompereAuctionCalendarVO;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.mapper.CompereAuctionCalendarMapper;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.pojo.CompereAuctionCalendar;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.service.ICompereAuctionCalendarService;
import cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.pojo.SiteAuctionCalendar;
import cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.vo.SiteAuctionCalendarVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
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
 * 主持人没有档期的时间接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class ICompereAuctionCalendarServiceImpl extends ServiceImpl<CompereAuctionCalendarMapper, CompereAuctionCalendar> implements ICompereAuctionCalendarService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public Result<Object> insertOrderUpdateCompereAuctionCalendar(CompereAuctionCalendar compereAuctionCalendar) {


           CompereAuctionCalendar compereAuctionCalendar1 = this.getOne(Wrappers.<CompereAuctionCalendar>lambdaQuery().eq(CompereAuctionCalendar::getNoScheduleTime,compereAuctionCalendar.getNoScheduleTime())
                   .eq(CompereAuctionCalendar::getDayTimeType,compereAuctionCalendar.getDayTimeType())
                   .eq(CompereAuctionCalendar::getCreateBy,securityUtil.getCurrUser().getId())
                   .eq(CompereAuctionCalendar::getDelFlag,CommonConstant.STATUS_NORMAL)
           );

           if(ToolUtil.isNotEmpty(compereAuctionCalendar1)){
               return new ResultUtil<>().setErrorMsg(201,"您已添加此档期时间！");
           }else {
               compereAuctionCalendar.setCreateBy(securityUtil.getCurrUser().getId());
               this.save(compereAuctionCalendar);
               return new ResultUtil<>().setData(200,"新增成功！");
           }

       }

      @Override
      public void removeCompereAuctionCalendar(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }


      @Override
      public Result<List<CompereAuctionCalendarVO>> AppGetCompereNoAuctionCalendar(String id){

          SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

           return Optional.ofNullable(ToolUtil.setListToNul(
                   this.list(new QueryWrapper<CompereAuctionCalendar>().lambda()

                           .eq(CompereAuctionCalendar::getCreateBy,id)

                           .eq(CompereAuctionCalendar::getDelFlag, CommonConstant.STATUS_NORMAL)

                           .ge(CompereAuctionCalendar::getNoScheduleTime,simpleDateFormat.format(new Date()))

                           .orderByAsc(CompereAuctionCalendar::getNoScheduleTime)

                   ))).map(compereAuctionCalendars ->

                   new ResultUtil<List<CompereAuctionCalendarVO>>()
                           .setData(compereAuctionCalendars.parallelStream().flatMap(compereAuctionCalendar -> {

                       CompereAuctionCalendarVO compereAuctionCalendarVO = new CompereAuctionCalendarVO();

                       ToolUtil.copyProperties(compereAuctionCalendar,compereAuctionCalendarVO);

                       return Stream.of(compereAuctionCalendarVO);

                   }).collect(Collectors.toList()),"获取没有档期时间列表成功"))
                   .orElse(new ResultUtil<List<CompereAuctionCalendarVO>>().setErrorMsg(201, "暂无数据！"));
      }




}