package cn.ruanyun.backInterface.modules.business.orderAfterSale.serviceimpl;

import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.balance.service.IBalanceService;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;
import cn.ruanyun.backInterface.modules.business.order.service.IOrderService;
import cn.ruanyun.backInterface.modules.business.orderAfterSale.VO.OrderAfterSaleVo;
import cn.ruanyun.backInterface.modules.business.orderAfterSale.dto.OrderAfterCommitDto;
import cn.ruanyun.backInterface.modules.business.orderAfterSale.dto.OrderAfterSaleDto;
import cn.ruanyun.backInterface.modules.business.orderAfterSale.mapper.OrderAfterSaleMapper;
import cn.ruanyun.backInterface.modules.business.orderAfterSale.pojo.OrderAfterSale;
import cn.ruanyun.backInterface.modules.business.orderAfterSale.service.IOrderAfterSaleService;
import cn.ruanyun.backInterface.modules.business.orderDetail.service.IOrderDetailService;
import cn.ruanyun.backInterface.modules.business.orderReturnReason.service.IOrderReturnReasonService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private IBalanceService balanceService;
    @Override
    public void commitOrderAfterSale(OrderAfterCommitDto orderAfterCommitDto) {

        OrderAfterSale orderAfterSale = new OrderAfterSale();

        //设置退款订单信息
        Optional.ofNullable(orderService.getById(orderAfterCommitDto.getOrderId()))
                .ifPresent(order -> {

                    orderAfterSale.setOrderStatus(order.getOrderStatus())
                            .setRefundMoney(order.getTotalPrice())
                            .setOrderId(order.getId())
                            .setReturnReasonId(orderAfterCommitDto.getReturnReasonId())
                            .setReturnReason(orderReturnReasonService.getReturnReason(orderAfterCommitDto.getReturnReasonId()))
                            .setExpand(orderAfterCommitDto.getExpand());

                    orderAfterSale.setCreateBy(securityUtil.getCurrUser().getId());

                    if (this.save(orderAfterSale)) {

                        //更改订单状态
                        order.setOrderStatus(OrderStatusEnum.SALE_AFTER);
                        orderService.updateById(order);
                    }
                });
    }

    @Override
    public void removeOrderAfterSale(String ids) {
        CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
    }


    @Override
    public void resolveOrderAfterSale(OrderAfterSaleDto orderAfterSaleDto) {

        Optional.ofNullable(this.getById(orderAfterSaleDto.getId()))
                .ifPresent(orderAfterSale -> {

                    //1. 更改售后订单状态
                    orderAfterSale.setActualRefundMoney(orderAfterSaleDto.getActualRefundMoney());
                    this.updateById(orderAfterSale);

                    //2. 更改订单状态
                    Optional.ofNullable(orderService.getById(orderAfterSale.getOrderId()))
                            .ifPresent(order -> {

                                //异步执行修改订单操作
                                CompletableFuture.runAsync(() -> {

                                    order.setOrderStatus(OrderStatusEnum.RETURN_FINISH);
                                    orderService.updateById(order);
                                }).join();


                                //3. 消费以及分佣返回 记录明细
                                balanceService.resolveReturnMoneyByBalance(order.getId(), order.getCreateBy(), orderAfterSaleDto.getActualRefundMoney());

                            });
                });
    }


    /**
     * app通过订单id获取售后信息
     *
     * @param orderId 订单id
     * @return OrderAfterSaleVO
     */
    @Override
    public OrderAfterSaleVo getByOrderId(String orderId) {

        return Optional.ofNullable(this.getOne(Wrappers.<OrderAfterSale>lambdaQuery()
                .eq(OrderAfterSale::getOrderId,orderId)
        )).map(orderAfterSale -> {

            OrderAfterSaleVo orderAfterSaleVO = new OrderAfterSaleVo();
            ToolUtil.copyProperties(orderAfterSale,orderAfterSaleVO);
            orderAfterSaleVO.setOrderDetails(orderDetailService.getOrderDetailByOrderId(orderId));
            return orderAfterSaleVO;

        }).orElse(null);
    }

    @Override
    public Result<Object> revocationAfterOrder(String orderId) {

        return Optional.ofNullable(this.getOne(Wrappers.<OrderAfterSale>lambdaQuery()
        .eq(OrderAfterSale::getOrderId, orderId)))
        .map(orderAfterSale -> {

            // 处理订单信息
            Optional.ofNullable(orderService.getById(orderId)).ifPresent(order -> {

                order.setOrderStatus(orderAfterSale.getOrderStatus());
                orderService.updateById(order);
            });

            // 移除售后订单
            this.removeById(orderAfterSale.getId());

            return new ResultUtil<>().setSuccessMsg("撤销成功！");

        }).orElse(new ResultUtil<>().setErrorMsg(205, "订单不存在！"));
    }


}