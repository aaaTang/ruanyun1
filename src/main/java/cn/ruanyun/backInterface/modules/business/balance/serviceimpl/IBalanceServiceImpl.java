package cn.ruanyun.backInterface.modules.business.balance.serviceimpl;

import cn.ruanyun.backInterface.common.enums.AddOrSubtractTypeEnum;
import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.business.balance.VO.AppBalanceVO;
import cn.ruanyun.backInterface.modules.business.balance.VO.BalanceVO;
import cn.ruanyun.backInterface.modules.business.balance.mapper.BalanceMapper;
import cn.ruanyun.backInterface.modules.business.balance.pojo.Balance;
import cn.ruanyun.backInterface.modules.business.balance.service.IBalanceService;
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


}