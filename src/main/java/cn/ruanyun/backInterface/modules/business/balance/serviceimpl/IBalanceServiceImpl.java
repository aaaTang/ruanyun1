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
        .orderByDesc(Balance::getCreateTime))))
        .map(balances -> balances.parallelStream().filter(balance -> orderService.judgeOrderFreeze(balance.getOrderId()))
        .map(Balance::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add))
        .orElse(new BigDecimal(0));
    }

    @Override
    public void resolveReturnMoneyByBalance(String orderId, String userId, BigDecimal actualRefundMoney) {

        //1. 找到此条订单的全部明细记录
        Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<Balance>lambdaQuery()
        .eq(Balance::getAddOrSubtractTypeEnum, AddOrSubtractTypeEnum.ADD)
        .eq(Balance::getOrderId, orderId)
        .ne(Balance::getCreateBy, userId))))
        .ifPresent(balances -> balances.parallelStream().filter(balance ->
                ToolUtil.isNotEmpty(roleService.getRoleNameByUserId(balance.getCreateBy())))
                .forEach(balance -> {

                    //2. 遍历找出每个人的入账记录,然后退还金额
                    if (roleService.getRoleNameByUserId(balance.getCreateBy()).contains(CommonConstant
                    .PER_STORE) || roleService.getRoleNameByUserId(balance.getCreateBy()).contains(CommonConstant
                    .STORE)) {

                        Optional.ofNullable(userService.getById(balance.getCreateBy()))
                                .ifPresent(user -> {

                                    //修改个人余额
                                    ThreadPoolUtil.getPool().execute(() -> {

                                        user.setBalance(user.getBalance().subtract(actualRefundMoney));
                                        userService.updateById(user);
                                    });

                                    //记录明细
                                    ThreadPoolUtil.getPool().execute(() -> {

                                        Balance balanceNew = new Balance();
                                        balanceNew.setOrderId(orderId)
                                                .setAddOrSubtractTypeEnum(AddOrSubtractTypeEnum.SUB)
                                                .setPrice(actualRefundMoney)
                                                .setTitle("退款金额")
                                                .setCreateBy(user.getId());
                                        this.save(balance);
                                    });

                                });

                    }else {

                        Optional.ofNullable(userService.getById(balance.getCreateBy()))
                                .ifPresent(user -> {

                                    //修改个人余额
                                    ThreadPoolUtil.getPool().execute(() -> {

                                        user.setBalance(user.getBalance().subtract(balance.getPrice()));
                                        userService.updateById(user);
                                    });

                                    //记录明细
                                    ThreadPoolUtil.getPool().execute(() -> {

                                        Balance balanceNew = new Balance();
                                        balanceNew.setOrderId(orderId)
                                                .setAddOrSubtractTypeEnum(AddOrSubtractTypeEnum.SUB)
                                                .setPrice(balance.getPrice())
                                                .setTitle("退款金额")
                                                .setCreateBy(user.getId());
                                        this.save(balance);
                                    });

                                });
                    }
        }));



    }


}