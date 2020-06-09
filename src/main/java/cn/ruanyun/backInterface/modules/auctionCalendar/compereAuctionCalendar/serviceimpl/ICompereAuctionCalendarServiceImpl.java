package cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.serviceimpl;

import cn.hutool.core.util.ObjectUtil;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.DayTimeTypeEnum;
import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.enums.UserTypeEnum;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.DTO.CompereAuctionCalendarDTO;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.VO.CompereAuctionCalendarVO;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.VO.PcGetCompereAuctionCalendarVO;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.mapper.CompereAuctionCalendarMapper;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.pojo.CompereAuctionCalendar;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.service.ICompereAuctionCalendarService;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereNoCalendars.mapper.CompereNoCalendarsMapper;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereNoCalendars.pojo.CompereNoCalendars;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.pojo.Site;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.vo.SiteDetailTimeVO;
import cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.pojo.SiteAuctionCalendar;
import cn.ruanyun.backInterface.modules.auctionCalendar.siteAuctionCalendar.vo.SiteAuctionCalendarVo;
import cn.ruanyun.backInterface.modules.auctionCalendar.sitePrice.pojo.SitePrice;
import cn.ruanyun.backInterface.modules.business.good.mapper.GoodMapper;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import cn.ruanyun.backInterface.modules.business.goodService.pojo.GoodService;
import cn.ruanyun.backInterface.modules.business.order.mapper.OrderMapper;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;
import cn.ruanyun.backInterface.modules.business.orderDetail.mapper.OrderDetailMapper;
import cn.ruanyun.backInterface.modules.business.orderDetail.pojo.OrderDetail;
import cn.ruanyun.backInterface.modules.business.orderDetail.service.IOrderDetailService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Resource;


/**
 * 主持人特殊档期价格管理接口
 * @author z
 */
@Slf4j
@Service
@Transactional
public class ICompereAuctionCalendarServiceImpl extends ServiceImpl<CompereAuctionCalendarMapper, CompereAuctionCalendar> implements ICompereAuctionCalendarService {


       @Autowired
       private SecurityUtil securityUtil;

       @Resource
       private GoodMapper goodMapper;

        @Resource
        private CompereNoCalendarsMapper  compereNoCalendarsMapper;

        @Autowired
        private IGoodService iGoodService;

        @Resource
        private OrderDetailMapper orderDetailMapper;

        @Resource
        private OrderMapper orderMapper;


       @Override
       public Result<Object> insertOrderUpdateCompereAuctionCalendar(CompereAuctionCalendar compereAuctionCalendar) {

           compereAuctionCalendar.setId(null);

           List<CompereAuctionCalendar> compereAuctionCalendars = this.list(Wrappers.<CompereAuctionCalendar>lambdaQuery()
                   .eq(CompereAuctionCalendar::getScheduleTime,compereAuctionCalendar.getScheduleTime())
                   .eq(CompereAuctionCalendar::getGoodsId,compereAuctionCalendar.getGoodsId())
                   .eq(CompereAuctionCalendar::getGoodTypeEnum,compereAuctionCalendar.getGoodTypeEnum())
                   .eq(CompereAuctionCalendar::getDelFlag,CommonConstant.STATUS_NORMAL));

           if(ToolUtil.isNotEmpty(compereAuctionCalendars)){

               for (CompereAuctionCalendar o : compereAuctionCalendars) {
                   ToolUtil.copyProperties(compereAuctionCalendar,o);
                   compereAuctionCalendar.setUpdateBy(securityUtil.getCurrUser().getId());
                   this.updateById(o);
               }
               return new ResultUtil<>().setData(200,"修改成功！");

           }else {
               compereAuctionCalendar.setCreateBy(securityUtil.getCurrUser().getId());
               compereAuctionCalendar.setDayTimeType(DayTimeTypeEnum.A_M);
               this.save(compereAuctionCalendar);

               compereAuctionCalendar.setId(String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId()));
               compereAuctionCalendar.setCreateBy(securityUtil.getCurrUser().getId());
               compereAuctionCalendar.setDayTimeType(DayTimeTypeEnum.P_M);
               this.save(compereAuctionCalendar);
               return new ResultUtil<>().setData(200,"新增成功！");
           }


       }

      @Override
      public void removeCompereAuctionCalendar(String goodsId,String scheduleTime) {

          List<CompereAuctionCalendar> compereAuctionCalendars = this.list(Wrappers.<CompereAuctionCalendar>lambdaQuery()
                  .eq(CompereAuctionCalendar::getScheduleTime,scheduleTime)
                  .eq(CompereAuctionCalendar::getGoodsId,goodsId)
                  .eq(CompereAuctionCalendar::getDelFlag,CommonConstant.STATUS_NORMAL));

          for (CompereAuctionCalendar o : compereAuctionCalendars) {
              o.setUpdateBy(securityUtil.getCurrUser().getId());
              o.setDelFlag(CommonConstant.DEL_FLAG);
              this.updateById(o);
          }
      }


    /**
     * APP查询某天是否有档期
     * @param goodsId 商品id
     * @param scheduleTime  档期时间
     * @return
     */
      @Override
      public List<CompereAuctionCalendarVO> AppGetCompereAuctionCalendar(String goodsId,String scheduleTime){


          List<CompereAuctionCalendarVO> compereAuctionCalendarVOS = new ArrayList<>();

          //1.先设置俩个时间段，上午和下午，以及默认价格
          CompereAuctionCalendarVO compereAuctionCalendarVO = new CompereAuctionCalendarVO();
          compereAuctionCalendarVO.setDayTimeType(DayTimeTypeEnum.A_M).setStatus(1).setScheduleTime(scheduleTime).setSitePrice(Optional.ofNullable(goodMapper.selectOne(Wrappers.<Good>lambdaQuery()
                  .eq(Good::getId,goodsId).eq(Good::getDelFlag,CommonConstant.STATUS_NORMAL)
          )).map(Good::getGoodNewPrice).orElse(null));
          compereAuctionCalendarVOS.add(compereAuctionCalendarVO);


          CompereAuctionCalendarVO compereAuctionCalendarVO1 = new CompereAuctionCalendarVO();
          compereAuctionCalendarVO1.setDayTimeType(DayTimeTypeEnum.P_M).setStatus(1).setScheduleTime(scheduleTime).setSitePrice(Optional.ofNullable(goodMapper.selectOne(Wrappers.<Good>lambdaQuery()
                  .eq(Good::getId,goodsId).eq(Good::getDelFlag,CommonConstant.STATUS_NORMAL)
          )).map(Good::getGoodNewPrice).orElse(null));
          compereAuctionCalendarVOS.add(compereAuctionCalendarVO1);


          //2.查看特殊节日的价格
          List<CompereAuctionCalendar> compereAuctionCalendars =  this.list(new QueryWrapper<CompereAuctionCalendar>().lambda()
                  .eq(CompereAuctionCalendar::getGoodsId,goodsId)
                  .eq(CompereAuctionCalendar::getScheduleTime,scheduleTime)
                  .eq(CompereAuctionCalendar::getDelFlag,CommonConstant.STATUS_NORMAL)
          );

          //3.查看是否有档期
          List<CompereNoCalendars>  compereNoCalendars = compereNoCalendarsMapper.selectList(new QueryWrapper<CompereNoCalendars>().lambda()
                  .eq(CompereNoCalendars::getNoScheduleTime,scheduleTime)
                  .eq(CompereNoCalendars::getGoodsId,goodsId)
                  .eq(CompereNoCalendars::getDelFlag,CommonConstant.STATUS_NORMAL)
          );


          for (CompereAuctionCalendarVO calendarVO : compereAuctionCalendarVOS) {

              //2.1 如果有特殊的价格就重新赋值价格
              for (CompereAuctionCalendar compereAuctionCalendar : compereAuctionCalendars) {

                  if(calendarVO.getDayTimeType().equals(compereAuctionCalendar.getDayTimeType())){

                      calendarVO.setSitePrice(compereAuctionCalendar.getGoodsPrice());
                  }

              }

              //3.1 如果没有档期时间就赋值状态0 == 无
              for (CompereNoCalendars noCalendars : compereNoCalendars) {

                  if(calendarVO.getDayTimeType().equals(noCalendars.getDayTimeType())){

                      calendarVO.setStatus(0);

                  }

              }

              //4.判断订单被购买
              Optional.ofNullable(orderDetailMapper.selectList(new QueryWrapper<OrderDetail>().lambda().eq(OrderDetail::getGoodId,goodsId)))
                      .map(orderDetails -> orderDetails.parallelStream().flatMap(orderDetail -> {
                          Optional.ofNullable(orderMapper.selectById(new QueryWrapper<Order>().lambda()
                                  .eq(Order::getId,orderDetail.getOrderId())
                                  .eq(Order::getDayTimeType,calendarVO.getDayTimeType())
                                  .eq(Order::getScheduleAppointment,calendarVO.getScheduleTime())
                                  .ge(Order::getOrderStatus, OrderStatusEnum.PRE_SEND)
                          )).ifPresent(order -> { calendarVO.setStatus(0); });

                          return null;
                      })).orElse(null);




          }

          return compereAuctionCalendarVOS;


      }


    /**
     * 后台获取特殊档期价格列表
     * @return
     */
      @Override
     public  List<PcGetCompereAuctionCalendarVO> PcGetCompereAuctionCalendar(CompereAuctionCalendarDTO compereAuctionCalendarDTO){

         String userRole  = iGoodService.getRoleUserList(securityUtil.getCurrUser().getId());



          return Optional.ofNullable(ToolUtil.setListToNul(this.list(new QueryWrapper<CompereAuctionCalendar>().lambda()

            .eq(ToolUtil.isNotEmpty(compereAuctionCalendarDTO.getId()),CompereAuctionCalendar::getId,compereAuctionCalendarDTO.getId())

            .eq(ToolUtil.isNotEmpty(compereAuctionCalendarDTO.getGoodsId()),CompereAuctionCalendar::getGoodsId,compereAuctionCalendarDTO.getGoodsId())

            .eq(userRole.equals(UserTypeEnum.STORE.getValue())||userRole.equals(UserTypeEnum.PER_STORE.getValue()),CompereAuctionCalendar::getCreateBy,securityUtil.getCurrUser().getId())

            .eq(ToolUtil.isNotEmpty(compereAuctionCalendarDTO.getScheduleTime()),CompereAuctionCalendar::getScheduleTime,compereAuctionCalendarDTO.getScheduleTime())

            .eq(ToolUtil.isNotEmpty(compereAuctionCalendarDTO.getGoodTypeEnum()),CompereAuctionCalendar::getGoodTypeEnum,compereAuctionCalendarDTO.getGoodTypeEnum())

            .eq(CompereAuctionCalendar::getDelFlag,CommonConstant.STATUS_NORMAL)

                  .orderByDesc(CompereAuctionCalendar::getCreateTime)

          ))).map(compereAuctionCalendars -> compereAuctionCalendars.parallelStream().flatMap(compereAuctionCalendar -> {

              PcGetCompereAuctionCalendarVO pcGetCompereAuctionCalendarVO = new PcGetCompereAuctionCalendarVO();

              ToolUtil.copyProperties(compereAuctionCalendar,pcGetCompereAuctionCalendarVO);

              return Stream.of(pcGetCompereAuctionCalendarVO);
          }).collect(Collectors.toList())).orElse(null);


      }








}