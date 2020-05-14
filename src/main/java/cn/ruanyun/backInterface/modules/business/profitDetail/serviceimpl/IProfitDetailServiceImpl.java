package cn.ruanyun.backInterface.modules.business.profitDetail.serviceimpl;

import cn.ruanyun.backInterface.common.enums.ProfitTypeEnum;
import cn.ruanyun.backInterface.modules.base.service.UserService;
import cn.ruanyun.backInterface.modules.business.order.service.IOrderService;
import cn.ruanyun.backInterface.modules.business.profitDetail.mapper.ProfitDetailMapper;
import cn.ruanyun.backInterface.modules.business.profitDetail.pojo.ProfitDetail;
import cn.ruanyun.backInterface.modules.business.profitDetail.service.IProfitDetailService;
import cn.ruanyun.backInterface.modules.business.profitDetail.vo.ProfitDetailVo;
import cn.ruanyun.backInterface.modules.business.profitPercent.service.IProfitPercentService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
 * 分红明细接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IProfitDetailServiceImpl extends ServiceImpl<ProfitDetailMapper, ProfitDetail> implements IProfitDetailService {


       @Autowired
       private SecurityUtil securityUtil;

       @Autowired
       private UserService userService;

       @Autowired
       private IOrderService orderService;

       @Autowired
       private IProfitPercentService profitPercentService;

       @Override
       public void insertOrderUpdateProfitDetail(ProfitDetail profitDetail) {

           if (ToolUtil.isEmpty(profitDetail.getCreateBy())) {

                       profitDetail.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       profitDetail.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(profitDetail)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeProfitDetail(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    @Override
    public List<ProfitDetailVo> getProfitDetailList() {

           return Optional.ofNullable(this.list(Wrappers.<ProfitDetail>lambdaQuery()
           .orderByDesc(ProfitDetail::getCreateTime)))
           .map(profitDetails -> profitDetails.parallelStream().flatMap(profitDetail -> {

               ProfitDetailVo profitDetailVo = new ProfitDetailVo();

               //分红人信息
               Optional.ofNullable(userService.get(profitDetail.getCreateBy()))
                       .ifPresent(user -> ToolUtil.copyProperties(user, profitDetailVo));

               //订单信息
               Optional.ofNullable(orderService.getById(profitDetail.getOrderId()))
                       .ifPresent(order -> {

                           ToolUtil.copyProperties(order, profitDetailVo);

                           //分账金额信息
                           profitDetailVo.setProfitTotalMoney(order.getTotalPrice().multiply(profitPercentService
                           .getProfitPercentLimitOne(ProfitTypeEnum.FIRST).getPlatformProfit()));
                       });

               ToolUtil.copyProperties(profitDetail, profitDetailVo);

               return Stream.of(profitDetailVo);
           }).collect(Collectors.toList()))
           .orElse(null);
    }
}