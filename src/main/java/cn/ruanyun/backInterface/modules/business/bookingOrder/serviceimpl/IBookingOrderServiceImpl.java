package cn.ruanyun.backInterface.modules.business.bookingOrder.serviceimpl;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.business.bookingOrder.VO.BookingOrderVO;
import cn.ruanyun.backInterface.modules.business.bookingOrder.VO.WhetherBookingOrderVO;
import cn.ruanyun.backInterface.modules.business.bookingOrder.mapper.BookingOrderMapper;
import cn.ruanyun.backInterface.modules.business.bookingOrder.pojo.BookingOrder;
import cn.ruanyun.backInterface.modules.business.bookingOrder.service.IBookingOrderService;
import cn.ruanyun.backInterface.modules.business.comment.mapper.CommentMapper;
import cn.ruanyun.backInterface.modules.business.comment.pojo.Comment;
import cn.ruanyun.backInterface.modules.business.comment.service.ICommentService;
import cn.ruanyun.backInterface.modules.business.goodCategory.entity.GoodCategory;
import cn.ruanyun.backInterface.modules.business.goodCategory.mapper.GoodCategoryMapper;
import cn.ruanyun.backInterface.modules.business.grade.mapper.GradeMapper;
import cn.ruanyun.backInterface.modules.business.grade.pojo.Grade;
import cn.ruanyun.backInterface.modules.business.grade.service.IGradeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
       private UserMapper userMapper;
       @Resource
       private GoodCategoryMapper goodCategoryMapper;
        @Autowired
        private IGradeService gradeService;
        @Resource
        private GradeMapper gradeMapper;
        @Autowired
        private ICommentService commentService;


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
                       BookingOrderVO booking = new BookingOrderVO();
                       String consent = "";
                       if(order.getConsent().equals(0)){
                           consent+="等待商家同意预约！";
                       }else if(order.getConsent().equals(1)){
                           consent+="商家同意预约！";
                       }else if(order.getConsent().equals(-1)){
                           consent+="商家拒绝预约！";
                       }
                       booking.setTimeDetail("预约时间:"+order.getBookingTime()+consent);
                     //获取店铺基本数据
                     User user =  userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getId,order.getStoreId()));
                     ToolUtil.copyProperties(user,booking);
                     //获取分类名称
                     booking.setTitle(Optional.ofNullable(goodCategoryMapper.selectById(user.getClassId())).map(GoodCategory::getTitle).orElse(null));
                       booking.setScore(Double.parseDouble(gradeService.getShopScore(order.getStoreId())))//评分
                               .setCommentNum(Optional.ofNullable(gradeMapper.selectList(new QueryWrapper<Grade>().lambda().eq(Grade::getUserId,order.getStoreId())))//评论数量
                                       .map(List::size)
                                       .orElse(0));
                       if(ToolUtil.isNotEmpty(classId)){
                           if(ToolUtil.isNotEmpty(booking.getClassId()) && booking.getClassId().equals(classId)){
                               bookingOrderVO.add(booking);
                           }
                       }else{
                           bookingOrderVO.add(booking);
                       }
                   }
               }
           return bookingOrderVO;

        }




    /**
     * 查詢我是否预约这个店铺
     * @param storeId
     * @param userid
     * @return
     */
    @Override
    public WhetherBookingOrderVO getWhetherBookingOrder(String storeId, String userid) {

        BookingOrder bookingOrder = this.getOne(Wrappers.<BookingOrder>lambdaQuery()
        .eq(BookingOrder::getStoreId,storeId).eq(BookingOrder::getCreateBy,userid)
                .eq(BookingOrder::getConsent,CommonConstant.STATUS_NORMAL)
        );
        WhetherBookingOrderVO whetherBookingOrderVO  = new WhetherBookingOrderVO();

        if(ToolUtil.isNotEmpty(bookingOrder)){
            ToolUtil.copyProperties(bookingOrder,whetherBookingOrderVO);
        }else {
            whetherBookingOrderVO.setConsent(-1);
        }
        return whetherBookingOrderVO;
    }


}