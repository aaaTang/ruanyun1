package cn.ruanyun.backInterface.modules.business.orderDetail.serviceimpl;

import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.modules.business.discountMy.pojo.DiscountMy;
import cn.ruanyun.backInterface.modules.business.discountMy.service.IDiscountMyService;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.service.IItemAttrValService;
import cn.ruanyun.backInterface.modules.business.orderDetail.VO.OrderDetailListVO;
import cn.ruanyun.backInterface.modules.business.orderDetail.mapper.OrderDetailMapper;
import cn.ruanyun.backInterface.modules.business.orderDetail.pojo.OrderDetail;
import cn.ruanyun.backInterface.modules.business.orderDetail.service.IOrderDetailService;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


/**
 * 子订单接口实现
 * @author wj
 */
@Slf4j
@Service
@Transactional
public class IOrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {


       @Autowired
       private SecurityUtil securityUtil;
       @Autowired
       private IDiscountMyService discountMyService;
       @Autowired
       private IItemAttrValService itemAttrValService;

       @Override
       public void insertOrderUpdateOrderDetail(OrderDetail orderDetail) {
           if (ToolUtil.isEmpty(orderDetail.getCreateBy())) {
               orderDetail.setCreateBy(securityUtil.getCurrUser().getId());
           } else {
               orderDetail.setUpdateBy(securityUtil.getCurrUser().getId());
           }
           //优惠券变成已使用
           if (!StringUtils.isEmpty(orderDetail.getDiscountMyId())){
               DiscountMy byId = discountMyService.getById(orderDetail.getDiscountMyId());
               byId.setStatus(0);
               discountMyService.updateById(byId);
           }
           Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(orderDetail)))
                   .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                   .toFuture().join();
       }

      @Override
      public void removeOrderDetail(String ids) {
          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    /**
     * 获取销量
     *
     * @param id
     * @return
     */
    @Override
    public Integer getGoodSalesVolume(String id) {
        return this.count(Wrappers.<OrderDetail>lambdaQuery()
                .eq(OrderDetail::getGoodId,id));
    }

    /**
     * 获取订单列表
     *
     * @param orderId
     * @return
     */
    @Override
    public List<OrderDetailListVO> getOrderListByOrderId(String orderId) {
        return Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<OrderDetail>lambdaQuery()
                .eq(OrderDetail::getOrderId, orderId)))).map(orderDetails -> {
                    List<OrderDetailListVO> orderDetailVOS = orderDetails.parallelStream().map(orderDetail -> {
                        OrderDetailListVO orderDetailVO = new OrderDetailListVO();
                        ToolUtil.copyProperties(orderDetail,orderDetailVO);
                        if(ToolUtil.isNotEmpty(orderDetail.getAttrSymbolPath())){
                            orderDetailVO.setItemAttrKeys(itemAttrValService.getItemAttrVals(orderDetail.getAttrSymbolPath()));
                        }
                        orderDetailVO.setGoodDalancePayment(orderDetail.getGoodDalancePayment().multiply(BigDecimal.valueOf(orderDetail.getBuyCount())));
                        return orderDetailVO;
                    }).collect(Collectors.toList());
                    return orderDetailVOS;
        }).orElse(null);
    }
}