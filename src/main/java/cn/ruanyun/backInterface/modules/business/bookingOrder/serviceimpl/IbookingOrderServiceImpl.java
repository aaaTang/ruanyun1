package cn.ruanyun.backInterface.modules.business.bookingOrder.serviceimpl;

import cn.ruanyun.backInterface.modules.business.bookingOrder.VO.BookingOrderVO;
import cn.ruanyun.backInterface.modules.business.bookingOrder.mapper.bookingOrderMapper;
import cn.ruanyun.backInterface.modules.business.bookingOrder.pojo.bookingOrder;
import cn.ruanyun.backInterface.modules.business.bookingOrder.service.IbookingOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class IbookingOrderServiceImpl extends ServiceImpl<bookingOrderMapper, bookingOrder> implements IbookingOrderService {


       @Autowired
       private SecurityUtil securityUtil;

       @Resource
       private bookingOrderMapper ibookingOrderMapper;

       @Override
       public void insertOrderUpdatebookingOrder(bookingOrder bookingOrder) {

           if (ToolUtil.isEmpty(bookingOrder.getCreateBy())) {

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

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    /**
     * 获取预约订单列表
     */
        public List<BookingOrderVO> bookingOrderList(String classId){

            List<BookingOrderVO> List = ibookingOrderMapper.bookingOrderList(securityUtil.getCurrUser().getId(),classId);
            /*评论数量，评分未写*/
           return List;
        }


}