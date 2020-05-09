package cn.ruanyun.backInterface.modules.business.aboutUs.serviceimpl;

import cn.ruanyun.backInterface.modules.business.aboutUs.mapper.AboutUsMapper;
import cn.ruanyun.backInterface.modules.business.aboutUs.pojo.AboutUs;
import cn.ruanyun.backInterface.modules.business.aboutUs.service.IAboutUsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;


/**
 * 关于我们接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IAboutUsServiceImpl extends ServiceImpl<AboutUsMapper, AboutUs> implements IAboutUsService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateAboutUs(AboutUs aboutUs) {

           if (ToolUtil.isEmpty(aboutUs.getCreateBy())) {

                       aboutUs.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       aboutUs.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(aboutUs)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeAboutUs(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }


    @Override
    public AboutUs getAboutUs() {
       return Optional.ofNullable(this.getById("267900849261383680")).orElse(null);
    }

}