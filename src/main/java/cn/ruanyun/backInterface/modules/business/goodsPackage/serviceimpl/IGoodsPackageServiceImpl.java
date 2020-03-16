package cn.ruanyun.backInterface.modules.business.goodsPackage.serviceimpl;

import cn.ruanyun.backInterface.modules.business.goodsPackage.mapper.GoodsPackageMapper;
import cn.ruanyun.backInterface.modules.business.goodsPackage.pojo.GoodsPackage;
import cn.ruanyun.backInterface.modules.business.goodsPackage.service.IGoodsPackageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.CompletableFuture;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;


/**
 * 商品套餐接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IGoodsPackageServiceImpl extends ServiceImpl<GoodsPackageMapper, GoodsPackage> implements IGoodsPackageService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateGoodsPackage(GoodsPackage goodsPackage) {

           if (ToolUtil.isEmpty(goodsPackage.getCreateBy())) {

                       goodsPackage.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       goodsPackage.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(goodsPackage)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeGoodsPackage(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }
}