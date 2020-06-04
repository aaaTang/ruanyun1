package cn.ruanyun.backInterface.modules.auctionCalendar.compereNoCalendars.serviceimpl;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.UserTypeEnum;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.DTO.CompereAuctionCalendarDTO;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.VO.PcGetCompereAuctionCalendarVO;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.pojo.CompereAuctionCalendar;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereNoCalendars.DTO.CompereNoCalendarsDTO;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereNoCalendars.VO.CompereNoCalendarsVO;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereNoCalendars.mapper.CompereNoCalendarsMapper;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereNoCalendars.pojo.CompereNoCalendars;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereNoCalendars.service.ICompereNoCalendarsService;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
 * 设置主持人没有档期的时间接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class ICompereNoCalendarsServiceImpl extends ServiceImpl<CompereNoCalendarsMapper, CompereNoCalendars> implements ICompereNoCalendarsService {


       @Autowired
       private SecurityUtil securityUtil;

       @Autowired
       private IGoodService iGoodService;

       @Override
       public void insertOrderUpdateCompereNoCalendars(CompereNoCalendars compereNoCalendars) {

           if (ToolUtil.isEmpty(compereNoCalendars.getCreateBy())) {

                       compereNoCalendars.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       compereNoCalendars.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(compereNoCalendars)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeCompereNoCalendars(CompereNoCalendars compereNoCalendars) {

          CompletableFuture.runAsync(() -> updateById(compereNoCalendars));
      }

    /**
     * 后端获取主持人没有档期的列表
     * @return
     */
    @Override
    public List<CompereNoCalendarsVO> PcGetCompereNoCalendars(CompereNoCalendarsDTO compereNoCalendarsDTO){


        String userRole  = iGoodService.getRoleUserList(securityUtil.getCurrUser().getId());

           return Optional.ofNullable(ToolUtil.setListToNul(this.list(new QueryWrapper<CompereNoCalendars>().lambda()


                   .eq(ToolUtil.isNotEmpty(compereNoCalendarsDTO.getId()), CompereNoCalendars::getId,compereNoCalendarsDTO.getId())

                   .eq(ToolUtil.isNotEmpty(compereNoCalendarsDTO.getGoodsId()),CompereNoCalendars::getGoodsId,compereNoCalendarsDTO.getGoodsId())

                   .eq(userRole.equals(UserTypeEnum.STORE.getValue())||userRole.equals(UserTypeEnum.PER_STORE.getValue()),CompereNoCalendars::getCreateBy,securityUtil.getCurrUser().getId())

                   .eq(ToolUtil.isNotEmpty(compereNoCalendarsDTO.getNoScheduleTime()),CompereNoCalendars::getNoScheduleTime,compereNoCalendarsDTO.getNoScheduleTime())

                   .eq(ToolUtil.isNotEmpty(compereNoCalendarsDTO.getGoodTypeEnum()),CompereNoCalendars::getGoodTypeEnum,compereNoCalendarsDTO.getGoodTypeEnum())

                   .eq(CompereNoCalendars::getDelFlag, CommonConstant.STATUS_NORMAL)

                   .orderByDesc(CompereNoCalendars::getCreateTime)


           ))).map( compereNoCalendars -> compereNoCalendars.parallelStream().flatMap(compereNoCalendars1 -> {

               CompereNoCalendarsVO compereNoCalendarsVO = new CompereNoCalendarsVO();

               ToolUtil.copyProperties(compereNoCalendars1,compereNoCalendarsVO);

               return Stream.of(compereNoCalendarsVO);

           }).collect(Collectors.toList())).orElse(null);



    }






}