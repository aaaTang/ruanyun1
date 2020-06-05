package cn.ruanyun.backInterface.modules.business.userRelationship.serviceimpl;

import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.business.order.mapper.OrderMapper;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;
import cn.ruanyun.backInterface.modules.business.orderDetail.mapper.OrderDetailMapper;
import cn.ruanyun.backInterface.modules.business.orderDetail.pojo.OrderDetail;
import cn.ruanyun.backInterface.modules.business.userRelationship.VO.AppRelationUserVO;
import cn.ruanyun.backInterface.modules.business.userRelationship.VO.UserOrderListVO;
import cn.ruanyun.backInterface.modules.business.userRelationship.mapper.UserRelationshipMapper;
import cn.ruanyun.backInterface.modules.business.userRelationship.pojo.UserRelationship;
import cn.ruanyun.backInterface.modules.business.userRelationship.service.IUserRelationshipService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;

import javax.annotation.Resource;


/**
 * 用户关联管理接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IUserRelationshipServiceImpl extends ServiceImpl<UserRelationshipMapper, UserRelationship> implements IUserRelationshipService {


       @Autowired
       private SecurityUtil securityUtil;

       @Resource
       private UserMapper userMapper;

       @Resource
       private OrderMapper orderMapper;

       @Resource
       private OrderDetailMapper orderDetailMapper;


       @Override
       public void insertOrderUpdateUserRelationship(UserRelationship userRelationship) {

           this.save(userRelationship);
       }

      @Override
      public void removeUserRelationship(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    @Override
    public String getRelationUser(String userId) {

           return Optional.ofNullable(this.getOne(Wrappers.<UserRelationship>lambdaQuery()
           .eq(UserRelationship::getCreateBy, userId)))
           .map(UserRelationship::getParentUserid)
           .orElse(null);
    }

    @Override
    public List<UserRelationship> getUserRelationshipListByUserId(String userId) {

           return Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<UserRelationship>lambdaQuery()
                   .eq(UserRelationship::getParentUserid, userId)
                   .orderByDesc(UserRelationship::getCreateTime))))
                   .orElse(null);
    }


    /**
     * 获取我的邀请人列表数据
     * @return
     */
    @Override
    public List<AppRelationUserVO> getUserRelationshipListByUser() {

        return Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<UserRelationship>lambdaQuery()
                .eq(UserRelationship::getCreateBy, securityUtil.getCurrUser().getId()))))
                .map(userRelationships -> userRelationships.parallelStream().flatMap(userRelationship -> {
                    AppRelationUserVO appRelationUserVO = new AppRelationUserVO();

                    Optional.ofNullable(userMapper.selectById(userRelationship.getParentUserid())).ifPresent(user -> {

                        ToolUtil.copyProperties(user,appRelationUserVO);

                    } );

                    return Stream.of(appRelationUserVO);

                }).filter(Objects::nonNull).collect(Collectors.toList()))
                .orElse(null);


    }


    /**
     * 获取邀请人的订单数据列表
     * @return
     */
    @Override
    public List<UserOrderListVO> getUserOrderList(String userId) {

        return Optional.ofNullable(ToolUtil.setListToNul(orderMapper.selectList(Wrappers.<Order>lambdaQuery()
                .eq(Order::getCreateBy,userId)
                .eq(Order::getOrderStatus, OrderStatusEnum.IS_COMPLETE)

        ))).map(orders -> orders.parallelStream().flatMap(order -> {

            UserOrderListVO userOrderListVO = new UserOrderListVO();

            OrderDetail orderDetail =  orderDetailMapper.selectOne(new QueryWrapper<OrderDetail>().lambda().eq(OrderDetail::getOrderId,order.getId()));

            ToolUtil.copyProperties(orderDetail,userOrderListVO);

            ToolUtil.copyProperties(order,userOrderListVO);


            return Stream.of(userOrderListVO);
        }).collect(Collectors.toList())).orElse(null);


    }



}