package cn.ruanyun.backInterface.modules.business.balance.serviceimpl;

import cn.hutool.core.util.ObjectUtil;
import cn.ruanyun.backInterface.common.enums.AddOrSubtractTypeEnum;
import cn.ruanyun.backInterface.common.enums.BalanceTypeEnum;
import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
import cn.ruanyun.backInterface.common.enums.BuyTypeEnum;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.business.balance.VO.AppBalanceVO;
import cn.ruanyun.backInterface.modules.business.balance.mapper.BalanceMapper;
import cn.ruanyun.backInterface.modules.business.balance.pojo.Balance;
import cn.ruanyun.backInterface.modules.business.balance.service.IBalanceService;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;
import cn.ruanyun.backInterface.modules.business.order.service.IOrderService;
import cn.ruanyun.backInterface.modules.business.orderAfterSale.service.IOrderAfterSaleService;
import cn.ruanyun.backInterface.modules.business.storeIncome.service.IStoreIncomeService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jcajce.provider.symmetric.AES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;


/**
 * 余额明细接口实现
 * @author zhu
 */
@Slf4j
@Service
@Transactional
public class IBalanceServiceImpl extends ServiceImpl<BalanceMapper, Balance> implements IBalanceService {


    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IStoreIncomeService storeIncomeService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderAfterSaleService orderAfterSaleService;

    @Override
    public void insertOrderUpdateBalance(Balance balance) {

        if (ToolUtil.isEmpty(balance.getCreateBy())) {

            balance.setCreateBy(securityUtil.getCurrUser().getId());
        } else {

            balance.setUpdateBy(securityUtil.getCurrUser().getId());
        }

        Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(balance)))
                .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                .toFuture().join();
    }

    @Override
    public void removeBalance(String ids) {

        CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
    }

    /**
     * app 获取用户明细
     * @return List<AppBalanceVO>
     */
    @Override
    public List<AppBalanceVO> getAppBalance(PageVo pageVo) {

        return Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<Balance>lambdaQuery()
                .eq(Balance::getCreateBy, securityUtil.getCurrUser().getId())
                .orderByDesc(Balance::getCreateTime))))
        .map(balances -> balances.parallelStream().flatMap(balance -> {

            AppBalanceVO appBalanceVO = new AppBalanceVO();
            ToolUtil.copyProperties(balance, appBalanceVO);
            return Stream.of(appBalanceVO);

        }).collect(Collectors.toList()))
        .orElse(null);
    }

    @Override
    public BigDecimal getProfitByUserId(String userId) {

        return Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<Balance>lambdaQuery()
                .eq(Balance::getAddOrSubtractTypeEnum, AddOrSubtractTypeEnum.ADD)
                .eq(Balance::getCreateBy, userId))))
        .map(balances -> balances.parallelStream().map(Balance::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add))
        .orElse(new BigDecimal(0));
    }

    @Override
    public BigDecimal getOrderFreezeMoney(String userId) {

        return Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<Balance>lambdaQuery()
        .eq(Balance::getCreateBy, userId)
        .eq(Balance::getBooleanReturnMoney, BooleanTypeEnum.NO)
        .eq(Balance::getAddOrSubtractTypeEnum, AddOrSubtractTypeEnum.ADD)
        .orderByDesc(Balance::getCreateTime))))
        .map(balances -> balances.parallelStream().filter(balance -> orderService.judgeOrderFreeze(balance.getOrderId()))
        .map(Balance::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add))
        .orElse(new BigDecimal(0));
    }

    @Override
    public void resolveReturnBalance(String orderId, BigDecimal actualRefundMoney) {

        // 1.从商家收入表里面 计算商家应该退款的金额,记录余额明细 2.从消费者的余额里面增加退款的金额 3.移除其余人分佣的明细

        //1. 先确认用户余额逻辑
        Optional.ofNullable(orderService.getById(orderId)).ifPresent(order -> {


            //1.1 处理分佣
            Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<Balance>lambdaQuery()
                    .eq(Balance::getAddOrSubtractTypeEnum, AddOrSubtractTypeEnum.ADD)
                    .eq(Balance::getBooleanReturnMoney, BooleanTypeEnum.NO)
                    .eq(Balance::getOrderId, orderId))))
                    .ifPresent(balances -> balances.parallelStream().forEach(balance -> {

                        balance.setBooleanReturnMoney(BooleanTypeEnum.YES);

                        //处理退款(排除店铺收入(因为不属于分佣))
                        if (! ObjectUtil.equal(order.getUserId(), balance.getCreateBy()) && ObjectUtil.equal(BalanceTypeEnum.IN_COME,
                                balance.getBalanceType())) {

                            Optional.ofNullable(userService.getById(balance.getCreateBy())).ifPresent(user -> {

                                user.setBalance(user.getBalance().subtract(balance.getPrice()));
                                userService.updateById(user);


                                Balance balanceShareUser = new Balance();
                                balanceShareUser.setTitle("订单" + order.getOrderNum() + "退款(佣金退回)")
                                        .setAddOrSubtractTypeEnum(AddOrSubtractTypeEnum.SUB)
                                        .setPrice(balance.getPrice())
                                        .setCreateBy(user.getId());

                                this.save(balanceShareUser);

                            });

                            this.updateById(balance);

                        }
                    }));


            //1.2 处理商家
            Optional.ofNullable(userService.getById(order.getUserId())).ifPresent(store -> {

                Balance balanceStore = new Balance();

                balanceStore.setTitle("订单" + order.getOrderNum() + "退款")
                        .setAddOrSubtractTypeEnum(AddOrSubtractTypeEnum.SUB);
                if (ToolUtil.isEmpty(actualRefundMoney)) {

                    //门店当前收入
                    BigDecimal storeIncome = storeIncomeService.getStoreIncomeByOrderId(order
                            .getUserId(), order.getId());

                    store.setBalance(store.getBalance().subtract(storeIncome));

                    balanceStore.setPrice(storeIncome);
                }else {

                    store.setBalance(store.getBalance().subtract(actualRefundMoney));
                    balanceStore.setPrice(actualRefundMoney);
                }

                log.info("处理商家退款成功！");
                userService.updateById(store);

                log.info("记录商家退款明细成功！");
                balanceStore.setCreateBy(order.getUserId());
                this.save(balanceStore);
            });



            //1.3 处理消费者
            Optional.ofNullable(userService.getById(order.getCreateBy())).ifPresent(user -> {

                Balance balanceUser = new Balance();

                balanceUser.setTitle("订单" + order.getOrderNum() + "退款")
                        .setAddOrSubtractTypeEnum(AddOrSubtractTypeEnum.ADD);

                if (ToolUtil.isEmpty(actualRefundMoney)) {

                    //申请退款金额
                    BigDecimal returnMoney = orderAfterSaleService.getOrderAfterSaleReturnMoney(orderId);
                    user.setBalance(user.getBalance().add(returnMoney));

                    balanceUser.setPrice(returnMoney);

                }else {

                    user.setBalance(user.getBalance().add(actualRefundMoney));
                    balanceUser.setPrice(actualRefundMoney);
                }

                userService.updateById(user);
                log.info("处理消费者退款成功！");

                balanceUser.setCreateBy(order.getCreateBy());
                this.save(balanceUser);
                log.info("记录消费者明细成功！");
            });
        });
    }
}