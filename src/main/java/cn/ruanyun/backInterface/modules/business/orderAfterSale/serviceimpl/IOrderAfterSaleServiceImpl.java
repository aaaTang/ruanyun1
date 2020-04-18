package cn.ruanyun.backInterface.modules.business.orderAfterSale.serviceimpl;

import cn.ruanyun.backInterface.common.enums.AfterSaleStatusEnum;
import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;
import cn.ruanyun.backInterface.modules.business.order.service.IOrderService;
import cn.ruanyun.backInterface.modules.business.orderAfterSale.VO.OrderAfterSaleVO;
import cn.ruanyun.backInterface.modules.business.orderAfterSale.mapper.OrderAfterSaleMapper;
import cn.ruanyun.backInterface.modules.business.orderAfterSale.pojo.OrderAfterSale;
import cn.ruanyun.backInterface.modules.business.orderAfterSale.service.IOrderAfterSaleService;
import cn.ruanyun.backInterface.modules.business.orderDetail.service.IOrderDetailService;
import cn.ruanyun.backInterface.modules.business.orderReturnReason.service.IOrderReturnReasonService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


/**
 * 售后申请接口实现
 * @author wj
 */
@Slf4j
@Service
@Transactional
public class IOrderAfterSaleServiceImpl extends ServiceImpl<OrderAfterSaleMapper, OrderAfterSale> implements IOrderAfterSaleService {


       @Autowired
       private SecurityUtil securityUtil;
       @Autowired
       private IOrderService orderService;
       @Autowired
       private IOrderReturnReasonService orderReturnReasonService;
       @Autowired
       private IOrderDetailService orderDetailService;

       @Override
       public Result<Object> insertUpdate(OrderAfterSale orderAfterSale) {
           String userId = securityUtil.getCurrUser().getId();
           if (StringUtils.isNotBlank(orderAfterSale.getReturnReasonId())){
               orderAfterSale.setReturnReason(orderReturnReasonService.getById(orderAfterSale.getReturnReasonId()).getReason());
           }
           if (StringUtils.isNotBlank(orderAfterSale.getOrderId())){
               Order byId = orderService.getById(orderAfterSale.getOrderId());
               orderAfterSale.setTotalPrice(new BigDecimal(byId.getTotalPrice()));
               orderAfterSale.setOrderStatus(byId.getOrderStatus());
           }
           if (ToolUtil.isEmpty(orderAfterSale.getCreateBy())) {
               OrderAfterSale one = this.getOne(Wrappers.<OrderAfterSale>lambdaQuery()
                       .eq(OrderAfterSale::getOrderId, orderAfterSale.getOrderId()));
               if (EmptyUtil.isNotEmpty(one)){
                    return new ResultUtil<>().setErrorMsg(202,"该订单已申请售后！");
               }
               orderAfterSale.setCreateBy(userId);
               Order order = new Order();
               order.setId(orderAfterSale.getOrderId());
               order.setOrderStatus(OrderStatusEnum.SALE_AFTER);
               orderService.changeStatus(order);
               orderAfterSale.setTotalPrice(new BigDecimal(orderService.getById(orderAfterSale.getOrderId()).getTotalPrice()));
           } else {
               orderAfterSale.setStatus(AfterSaleStatusEnum.APPLY);
               orderAfterSale.setUpdateBy(userId);
           }
           Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(orderAfterSale)))
                   .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                   .toFuture().join();
           return new ResultUtil<>().setSuccessMsg("插入或者更新成功!");
       }

      @Override
      public void removeOrderAfterSale(String ids) {
          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    /***
     * 改变售后状态
     * @param orderAfterSale
     * @return
     */
    @Override
    public Object changeStatus(OrderAfterSale orderAfterSale) {
        OrderAfterSale byId = this.getById(orderAfterSale.getId());
        switch(orderAfterSale.getStatus()){
            //申请
            case APPLY:
                break;
            //审核通过
            case APPLY_PASS:
                break;
            //发货
            case GOOD_DELIVER:
                byId.setExpressCode(orderAfterSale.getExpressCode());
                break;
            //确认退款
            case FINISH:
                Order order = new Order();
                order.setId(byId.getOrderId());
                order.setOrderStatus(OrderStatusEnum.RETURN_FINISH);
                orderService.changeStatus(order);
                break;
            //货物不完整
            case GOOD_NO_PASS:
                break;
            //审核不通过
            case APPLY_NO_PASS:
                break;
            //撤销
            case REVOCATION:
                Order order1 = new Order();
                order1.setOrderStatus(byId.getOrderStatus());
                orderService.changeStatus(order1);
                break;
            default:
                break;
        }
        byId.setStatus(orderAfterSale.getStatus());

        return null;
    }

    /**
     * app通过订单id获取售后信息
     *
     * @param orderId
     * @return
     */
    @Override
    public Object getByOrderId(String orderId) {
        return Optional.ofNullable(this.getOne(Wrappers.<OrderAfterSale>lambdaQuery().eq(OrderAfterSale::getOrderId,orderId))).map(orderAfterSale -> {
            OrderAfterSaleVO orderAfterSaleVO = new OrderAfterSaleVO();
            ToolUtil.copyProperties(orderAfterSale,orderAfterSaleVO);
            orderAfterSaleVO.setStatusCode(orderAfterSale.getStatus().getCode());
            orderAfterSaleVO.setTypeCode(orderAfterSale.getType().getCode());
            orderAfterSaleVO.setOrderDetails(orderDetailService.getOrderListByOrderId(orderId));
            return orderAfterSaleVO;
        }).orElse(null);
    }


}