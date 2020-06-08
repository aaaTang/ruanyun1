package cn.ruanyun.backInterface.modules.business.firstRateService.serviceimpl;

import cn.ruanyun.backInterface.modules.business.firstRateService.DTO.FirstRateServiceDTO;
import cn.ruanyun.backInterface.modules.business.firstRateService.VO.FirstRateServiceVO;
import cn.ruanyun.backInterface.modules.business.firstRateService.mapper.FirstRateServiceMapper;
import cn.ruanyun.backInterface.modules.business.firstRateService.pojo.FirstRateService;
import cn.ruanyun.backInterface.modules.business.firstRateService.service.IFirstRateServiceService;
import cn.ruanyun.backInterface.modules.business.goodCategory.entity.GoodCategory;
import cn.ruanyun.backInterface.modules.business.goodCategory.mapper.GoodCategoryMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;

import javax.swing.text.html.Option;


/**
 * 优质服务接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IFirstRateServiceServiceImpl extends ServiceImpl<FirstRateServiceMapper, FirstRateService> implements IFirstRateServiceService {


       @Autowired
       private SecurityUtil securityUtil;

       @Autowired
       private GoodCategoryMapper goodCategoryMapper;

       @Override
       public void insertOrderUpdateFirstRateService(FirstRateService firstRateService) {

           if (ToolUtil.isEmpty(firstRateService.getCreateBy())) {

                       firstRateService.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       firstRateService.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(firstRateService)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeFirstRateService(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    @Override
    public List<String> getFirstRateName(String ids) {

           if (ToolUtil.isEmpty(ids)) {

               return null;
           }
           return Optional.ofNullable(ToolUtil.setListToNul(this.listByIds(ToolUtil.splitterStr(ids))))
                   .map(firstRateServices -> firstRateServices.parallelStream().map(FirstRateService::getItemName)
                   .collect(Collectors.toList()))
                   .orElse(null);
    }


    /**
     * 获取优质服务列表
     * @param firstRateServiceDTO 实体类
     * @return
     */
    @Override
    public List<FirstRateServiceVO> getFirstRateService(FirstRateServiceDTO firstRateServiceDTO){

           return Optional.ofNullable(this.list(new QueryWrapper<FirstRateService>().lambda()

                   .eq(ToolUtil.isNotEmpty(firstRateServiceDTO.getId()),FirstRateService::getId,firstRateServiceDTO.getId())

                   .eq(ToolUtil.isNotEmpty(firstRateServiceDTO.getGoodCategoryId()),FirstRateService::getGoodCategoryId,firstRateServiceDTO.getGoodCategoryId())

                   .like(ToolUtil.isNotEmpty(firstRateServiceDTO.getItemName()),FirstRateService::getItemName,firstRateServiceDTO.getItemName())

                   .orderByDesc(FirstRateService::getCreateTime)

           )).filter(Objects::nonNull).map(firstRateServiceList -> firstRateServiceList.parallelStream().flatMap(firstRateService -> {

               FirstRateServiceVO firstRateServiceVO = new FirstRateServiceVO();
               ToolUtil.copyProperties(firstRateService,firstRateServiceVO);

               firstRateServiceVO.setGoodCategoryName(Optional.ofNullable(goodCategoryMapper.selectById(firstRateService.getGoodCategoryId())).map(GoodCategory::getTitle).orElse(null));

               return Stream.of(firstRateServiceVO);
           }).collect(Collectors.toList())).orElse(null);
    }






}