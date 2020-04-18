package cn.ruanyun.backInterface.modules.business.bookingOrder.serviceimpl;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.modules.business.bookingOrder.VO.BookingOrderVO;
import cn.ruanyun.backInterface.modules.business.bookingOrder.VO.WhetherBookingOrderVO;
import cn.ruanyun.backInterface.modules.business.bookingOrder.mapper.BookingOrderMapper;
import cn.ruanyun.backInterface.modules.business.bookingOrder.pojo.BookingOrder;
import cn.ruanyun.backInterface.modules.business.bookingOrder.service.IBookingOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;

import javax.annotation.Resource;


/**
 * 预约订单接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IBookingOrderServiceImpl extends ServiceImpl<BookingOrderMapper, BookingOrder> implements IBookingOrderService {


       @Autowired
       private SecurityUtil securityUtil;

       @Resource
       private BookingOrderMapper bookingOrderMapper;

       @Override
       public void insertOrderUpdatebookingOrder(BookingOrder bookingOrder) {

              if(ToolUtil.isEmpty(bookingOrder.getCreateBy())){
                  bookingOrder.setCreateBy(securityUtil.getCurrUser().getId());
              }else {
                  bookingOrder.setUpdateBy(securityUtil.getCurrUser().getId());
              }
              Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(bookingOrder)))
                      .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                      .toFuture().join();
       }

      @Override
      public void removebookingOrder(String ids) {
           BookingOrder bookingOrder = this.getOne(Wrappers.<BookingOrder>lambdaQuery()
                   .eq(BookingOrder::getStoreId,ids)
                   .eq(BookingOrder::getCreateBy,securityUtil.getCurrUser().getId())
                   .eq(BookingOrder::getDelFlag,0)
           );
          if (ToolUtil.isNotEmpty(bookingOrder)){
              bookingOrder.setDelFlag(1);
              Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(bookingOrder)))
                      .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                      .toFuture().join();
          }

      }

    /**
     * 获取预约订单列表
     */
        public List<BookingOrderVO> bookingOrderList(String classId){

            //TODO::先查询预约的所有数据
            List<BookingOrder> bookingOrder  = this.list(new QueryWrapper<BookingOrder>().lambda()
                    .eq(BookingOrder::getCreateBy,securityUtil.getCurrUser().getId()));
            List<BookingOrderVO> bookingOrderVO =new ArrayList<>();

               if(ToolUtil.isNotEmpty(bookingOrder)){//TODO::数据不为空
                   for (BookingOrder order : bookingOrder) {
                       if(ToolUtil.isNotEmpty(classId)){//TODO::传过来的classId不为空
                           //TODO::筛选出等于classId的数据
                           BookingOrderVO dvo = (bookingOrderMapper.bookingOrderList(order.getStoreId(),securityUtil.getCurrUser().getId()).getClassId() == classId ? bookingOrderMapper.bookingOrderList(order.getStoreId(),securityUtil.getCurrUser().getId()) : null);
                           if(ToolUtil.isNotEmpty(dvo)){bookingOrderVO.add(dvo);}
                       }else {
                           //TODO::classId是空的时候拿出全部数据
                           bookingOrderVO.add(bookingOrderMapper.bookingOrderList(order.getStoreId(),securityUtil.getCurrUser().getId()));
                       }

                   }
               }
           return bookingOrderVO;

        }

    @Override
    public WhetherBookingOrderVO getWhetherBookingOrder(String storeId, String userid) {

        BookingOrder bookingOrder = this.getOne(Wrappers.<BookingOrder>lambdaQuery()
        .eq(BookingOrder::getStoreId,storeId).eq(BookingOrder::getCreateBy,userid)
                .eq(BookingOrder::getConsent,CommonConstant.STATUS_NORMAL)
        );
        WhetherBookingOrderVO whetherBookingOrderVO  = new WhetherBookingOrderVO();

        if(ToolUtil.isNotEmpty(bookingOrder)){
            ToolUtil.copyProperties(bookingOrder,whetherBookingOrderVO);
        }
        return whetherBookingOrderVO;
    }


}