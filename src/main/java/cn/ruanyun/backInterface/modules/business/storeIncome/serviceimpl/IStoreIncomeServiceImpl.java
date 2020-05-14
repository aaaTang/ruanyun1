package cn.ruanyun.backInterface.modules.business.storeIncome.serviceimpl;

import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.business.order.service.IOrderService;
import cn.ruanyun.backInterface.modules.business.storeIncome.mapper.StoreIncomeMapper;
import cn.ruanyun.backInterface.modules.business.storeIncome.pojo.StoreIncome;
import cn.ruanyun.backInterface.modules.business.storeIncome.service.IStoreIncomeService;
import cn.ruanyun.backInterface.modules.business.storeIncome.vo.StoreIncomeCountVo;
import cn.ruanyun.backInterface.modules.business.storeIncome.vo.StoreIncomeVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;


/**
 * 店铺收入接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IStoreIncomeServiceImpl extends ServiceImpl<StoreIncomeMapper, StoreIncome> implements IStoreIncomeService {


       @Autowired
       private SecurityUtil securityUtil;

       @Autowired
       private IUserService userService;

       @Autowired
       private IOrderService orderService;

       @Override
       public void insertOrderUpdateStoreIncome(StoreIncome storeIncome) {

           if (ToolUtil.isEmpty(storeIncome.getCreateBy())) {

                       storeIncome.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       storeIncome.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(storeIncome)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeStoreIncome(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    @Override
    public List<StoreIncomeVo> getStoreIncomeList() {

           return Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<StoreIncome>lambdaQuery()
           .eq(StoreIncome::getCreateBy, securityUtil.getCurrUser().getId())
           .orderByDesc(StoreIncome::getCreateTime))))
           .map(storeIncomes -> storeIncomes.parallelStream().flatMap(storeIncome -> {

               StoreIncomeVo storeIncomeVo = new StoreIncomeVo();

               //订单信息
               Optional.ofNullable(orderService.getById(storeIncome.getOrderId())).ifPresent(order -> {

                   ToolUtil.copyProperties(order, storeIncomeVo);

                   storeIncomeVo.setBuyTime(order.getCreateTime());

                   Optional.ofNullable(userService.getById(order.getCreateBy())).ifPresent(user -> {

                       storeIncomeVo.setBuyerName(user.getNickName())
                               .setBuyerMobile(user.getMobile());
                   });
               });

               //收入信息
               ToolUtil.copyProperties(storeIncome, storeIncomeVo);
               storeIncomeVo.setIncomeTime(storeIncome.getCreateTime());

               return Stream.of(storeIncomeVo);
           }).collect(Collectors.toList()))
           .orElse(null);

    }

    @Override
    public StoreIncomeCountVo getStoreIncomeCount(String storeId) {

           String currentStoreId = ToolUtil.isEmpty(storeId) ? securityUtil.getCurrUser().getId() : storeId;

           return Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<StoreIncome>lambdaQuery()
                   .eq(StoreIncome::getCreateBy, currentStoreId))))
                   .map(storeIncomes -> {

                       StoreIncomeCountVo storeIncomeCountVo = new StoreIncomeCountVo();

                       // TODO: 2020/5/14 0014  日收入

                       // TODO: 2020/5/14 0014  月收入

                       // TODO: 2020/5/14 0014  年收入

                       //总收入
                       storeIncomeCountVo.setTotalIncome(storeIncomes.parallelStream().map(StoreIncome::getIncomeMoney)
                       .reduce(BigDecimal.ZERO, BigDecimal::add));

                       return storeIncomeCountVo;

                   }).orElse(new StoreIncomeCountVo());
    }
}