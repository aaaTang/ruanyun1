package cn.ruanyun.backInterface.modules.business.balance.serviceimpl;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.AddOrSubtractTypeEnum;
import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.RoleService;
import cn.ruanyun.backInterface.modules.base.service.UserService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.business.balance.VO.AppBalanceVO;
import cn.ruanyun.backInterface.modules.business.balance.VO.BalanceVO;
import cn.ruanyun.backInterface.modules.business.balance.mapper.BalanceMapper;
import cn.ruanyun.backInterface.modules.business.balance.pojo.Balance;
import cn.ruanyun.backInterface.modules.business.balance.service.IBalanceService;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;
import cn.ruanyun.backInterface.modules.business.order.service.IOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
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
    private IUserRoleService userRoleService;


    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserService userService;

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
        .eq(Balance::getAddOrSubtractTypeEnum, AddOrSubtractTypeEnum.ADD)
        .orderByDesc(Balance::getCreateTime))))
        .map(balances -> balances.parallelStream().filter(balance -> orderService.judgeOrderFreeze(balance.getOrderId()))
        .map(Balance::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add))
        .orElse(new BigDecimal(0));
    }

    @Override
    public void resolveReturnTotalMoneyByBalance(String orderId) {

        //1. 根据余额明细表全额退回
        Optional.ofNullable(orderService.getById(orderId)).flatMap(order ->
                Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<Balance>lambdaQuery()
                        .eq(Balance::getOrderId, order.getId())
                        .eq(Balance::getAddOrSubtractTypeEnum, AddOrSubtractTypeEnum.ADD)))))
                .ifPresent(balances ->

                        //1. 退款操作
                        balances.parallelStream().forEach(balance -> {

                            //1.1 更改账户余额信息
                            Optional.ofNullable(userService.getById(balance.getId()))
                                    .ifPresent(user -> {

                                        user.setBalance(user.getBalance().add(balance.getPrice()));
                                        userService.updateById(user);
                                    });

                            //2.2 更改明细信息
                            balance.setAddOrSubtractTypeEnum(AddOrSubtractTypeEnum.SUB);
                            balance.setTitle("订单" + orderService.getById(orderId).getOrderNum() + "退款" );
                            this.updateById(balance);

                        }));

                        //2. 进款操作
                         Optional.ofNullable(this.getOne(Wrappers.<Balance>lambdaQuery()
                         .eq(Balance::getOrderId, orderId)
                         .eq(Balance::getCreateBy, orderService.getById(orderId).getCreateBy())
                         .eq(Balance::getAddOrSubtractTypeEnum, AddOrSubtractTypeEnum.SUB)))
                         .ifPresent(balance -> {

                             //1. 更改明细
                             balance.setAddOrSubtractTypeEnum(AddOrSubtractTypeEnum.ADD)
                                     .setTitle("商家退款");

                             //2. 增加余额
                             Optional.ofNullable(userService.getById(balance.getCreateBy()))
                                     .ifPresent(user -> {

                                         user.setBalance(user.getBalance().add(balance.getPrice()));
                                         userService.updateById(user);
                                     });
                         });
    }

    @Override
    public void resolveReturnPartMoneyByBalance(String orderId, BigDecimal actualRefundMoney) {




    }


}