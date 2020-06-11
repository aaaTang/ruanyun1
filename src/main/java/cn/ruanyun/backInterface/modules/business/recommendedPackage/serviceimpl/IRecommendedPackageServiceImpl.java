package cn.ruanyun.backInterface.modules.business.recommendedPackage.serviceimpl;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.recommendedPackage.mapper.RecommendedPackageMapper;
import cn.ruanyun.backInterface.modules.business.recommendedPackage.pojo.RecommendedPackage;
import cn.ruanyun.backInterface.modules.business.recommendedPackage.service.IRecommendedPackageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;


/**
 * 推荐商品和套餐接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IRecommendedPackageServiceImpl extends ServiceImpl<RecommendedPackageMapper, RecommendedPackage> implements IRecommendedPackageService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public Result<Object> insertOrderUpdateRecommendedPackage(RecommendedPackage recommendedPackage) {

           List<RecommendedPackage> recommendedPackages = this.list(new QueryWrapper<RecommendedPackage>().lambda()
                   .eq(RecommendedPackage::getGoodId,recommendedPackage.getGoodId())
                   .eq(RecommendedPackage::getCreateBy,securityUtil.getCurrUser().getId())
           );

           if(recommendedPackages.size()<3){
               recommendedPackage.setCreateBy(securityUtil.getCurrUser().getId());

               Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.save(recommendedPackage)))
                       .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                       .toFuture().join();

               return new ResultUtil<>().setData(200,"新增成功！");
           }

           return new ResultUtil<>().setErrorMsg(201,"推荐商品已经大于3条！");
       }

      @Override
      public void removeRecommendedPackage(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }






}