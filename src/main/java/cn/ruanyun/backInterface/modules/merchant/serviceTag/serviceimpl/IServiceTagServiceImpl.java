package cn.ruanyun.backInterface.modules.merchant.serviceTag.serviceimpl;

import cn.ruanyun.backInterface.modules.merchant.serviceTag.DTO.ServiceTagDTO;
import cn.ruanyun.backInterface.modules.merchant.serviceTag.VO.ServiceTagVO;
import cn.ruanyun.backInterface.modules.merchant.serviceTag.mapper.ServiceTagMapper;
import cn.ruanyun.backInterface.modules.merchant.serviceTag.pojo.ServiceTag;
import cn.ruanyun.backInterface.modules.merchant.serviceTag.service.IServiceTagService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;


/**
 * 优质服务标签接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IServiceTagServiceImpl extends ServiceImpl<ServiceTagMapper, ServiceTag> implements IServiceTagService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateServiceTag(ServiceTag serviceTag) {

           if (ToolUtil.isEmpty(serviceTag.getCreateBy())) {

                       serviceTag.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       serviceTag.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(serviceTag)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeServiceTag(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }


      @Override
     public List<ServiceTagVO> getServiceTag(ServiceTagDTO serviceTagDTO){


           return Optional.ofNullable(ToolUtil.setListToNul(this.list(new QueryWrapper<ServiceTag>().lambda()
                   .eq(ToolUtil.isNotEmpty(serviceTagDTO.getClassId()),ServiceTag::getClassId,serviceTagDTO.getClassId()))))

                   .map(serviceTags -> serviceTags.parallelStream().flatMap(serviceTag -> {
                       ServiceTagVO serviceTagVO = new ServiceTagVO();

                       ToolUtil.copyProperties(serviceTag,serviceTagVO);

                       return Stream.of(serviceTagVO);
                   }).collect(Collectors.toList()))
                   .orElse(null);
      }

}