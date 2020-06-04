package cn.ruanyun.backInterface.modules.business.bookingOrder.serviceimpl;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.UserTypeEnum;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.pojo.UserRole;
import cn.ruanyun.backInterface.modules.business.bookingOrder.DTO.BookingDTO;
import cn.ruanyun.backInterface.modules.business.bookingOrder.VO.BackBookingOrderListVO;
import cn.ruanyun.backInterface.modules.business.bookingOrder.VO.BookingOrderVO;
import cn.ruanyun.backInterface.modules.business.bookingOrder.VO.WhetherBookingOrderVO;
import cn.ruanyun.backInterface.modules.business.bookingOrder.mapper.BookingOrderMapper;
import cn.ruanyun.backInterface.modules.business.bookingOrder.pojo.BookingOrder;
import cn.ruanyun.backInterface.modules.business.bookingOrder.service.IBookingOrderService;
import cn.ruanyun.backInterface.modules.business.comment.mapper.CommentMapper;
import cn.ruanyun.backInterface.modules.business.comment.pojo.Comment;
import cn.ruanyun.backInterface.modules.business.comment.service.ICommentService;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import cn.ruanyun.backInterface.modules.business.goodCategory.entity.GoodCategory;
import cn.ruanyun.backInterface.modules.business.goodCategory.mapper.GoodCategoryMapper;
import cn.ruanyun.backInterface.modules.business.grade.mapper.GradeMapper;
import cn.ruanyun.backInterface.modules.business.grade.pojo.Grade;
import cn.ruanyun.backInterface.modules.business.grade.service.IGradeService;
import cn.ruanyun.backInterface.modules.business.storeAudit.DTO.StoreAuditDTO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        private IGoodService iGoodService;

       @Override
       public Result<Object> insertOrderUpdatebookingOrder(BookingOrder bookingOrder) {

           BookingOrder booking = this.getOne(new QueryWrapper<BookingOrder>().lambda()
                   .eq(BookingOrder::getStoreId,bookingOrder.getStoreId())
                   .eq(BookingOrder::getCreateBy,securityUtil.getCurrUser().getId())
                   .eq(BookingOrder::getConsent,CommonConstant.STATUS_NORMAL)
           );

           if(ToolUtil.isNotEmpty(booking)){

               booking.setUpdateBy(securityUtil.getCurrUser().getId());
               booking.setBookingTime(bookingOrder.getBookingTime());
               this.updateById(booking);
               return new ResultUtil<>().setData(200, "修改成功！");

           }else {

               bookingOrder.setCreateBy(securityUtil.getCurrUser().getId());
               this.save(bookingOrder);
               return new ResultUtil<>().setData(200, "新增成功！");

           }


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
     * 后端商家处理预约
     * @return
     */
    @Override
    public Result<Object> checkBookingOrder(BookingDTO bookingDTO) {

        return CompletableFuture.supplyAsync(() -> Optional.ofNullable(super.getById(bookingDTO.getId())))
                .thenApplyAsync(storeAudit -> storeAudit.map(storeAudit1 -> {

                    ToolUtil.copyProperties(bookingDTO, storeAudit1);
                    super.updateById(storeAudit1);

                    return new ResultUtil<>().setSuccessMsg("审核成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无该数据！")))
                .join();

    }



        /**
         * 获取预约订单列表
         */
        @Override
        public List<BookingOrderVO> bookingOrderList(String classId){

            //TODO::先查询预约的所有数据
            List<BookingOrder> bookingOrder  = this.list(new QueryWrapper<BookingOrder>().lambda()
                    .eq(BookingOrder::getCreateBy,securityUtil.getCurrUser().getId()));

            List<BookingOrderVO> bookingOrderVO =new ArrayList<>();

            //TODO::数据不为空
               if(ToolUtil.isNotEmpty(bookingOrder)){
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
                     booking.setTitle(Optional.ofNullable(goodCategoryMapper.selectById(user.getClassId())).map(GoodCategory::getTitle).orElse("暂无！"));
                       //评分
                     booking.setScore(Double.parseDouble(gradeService.getShopScore(order.getStoreId())))
                             //评论数量
                             .setCommentNum(Optional.ofNullable(gradeMapper.selectList(new QueryWrapper<Grade>().lambda().eq(Grade::getUserId,order.getStoreId())))
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


    /**
     * 后端获取商家预约订单列表
     * @return
     */
    @Override
    public List<BackBookingOrderListVO> BackBookingOrderList(BookingDTO bookingDTO) {

     String  userRole  =  iGoodService.getRoleUserList(securityUtil.getCurrUser().getId());

        return Optional.ofNullable(ToolUtil.setListToNul(this.list(new QueryWrapper<BookingOrder>().lambda()

                .eq(ToolUtil.isNotEmpty(bookingDTO.getId()),BookingOrder::getId,bookingDTO.getId())

                .eq(ToolUtil.isNotEmpty(bookingDTO.getConsent()),BookingOrder::getId,bookingDTO.getConsent())

                .eq(userRole.equals(UserTypeEnum.PER_STORE.getValue())||userRole.equals(UserTypeEnum.STORE.getValue()),BookingOrder::getStoreId,securityUtil.getCurrUser().getId())

                .orderByDesc(BookingOrder::getCreateTime)

        ))).map(bookingOrders -> bookingOrders.parallelStream().flatMap(bookingOrder -> {

            BackBookingOrderListVO backBookingOrderListVO = new BackBookingOrderListVO();
            ToolUtil.copyProperties(bookingOrder,backBookingOrderListVO);


            backBookingOrderListVO.setShopName(Optional.ofNullable(userMapper.selectById(bookingOrder.getStoreId())).map(User::getShopName).orElse("商家信息错误！"));
            backBookingOrderListVO.setNickName(Optional.ofNullable(userMapper.selectById(bookingOrder.getCreateBy())).map(User::getNickName).orElse("用户信息错误！"));


            return Stream.of(backBookingOrderListVO);
        }).collect(Collectors.toList())).filter(Objects::nonNull).orElse(null);

    }


}