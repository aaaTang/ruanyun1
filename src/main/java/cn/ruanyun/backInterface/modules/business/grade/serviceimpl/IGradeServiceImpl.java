package cn.ruanyun.backInterface.modules.business.grade.serviceimpl;

import cn.ruanyun.backInterface.modules.business.grade.mapper.GradeMapper;
import cn.ruanyun.backInterface.modules.business.grade.pojo.Grade;
import cn.ruanyun.backInterface.modules.business.grade.service.IGradeService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;


/**
 * 评分接口实现
 * @author wj
 */
@Slf4j
@Service
@Transactional
public class IGradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements IGradeService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateGrade(Grade grade) {
           if (ToolUtil.isEmpty(grade.getCreateBy())) {
               grade.setCreateBy(securityUtil.getCurrUser().getId());
           } else {
               grade.setUpdateBy(securityUtil.getCurrUser().getId());
           }
           Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(grade)))
                   .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                   .toFuture().join();
       }

      @Override
      public void removeGrade(String ids) {
          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    /**
     * 计算商铺评分
     *
     * @param ids
     * @return
     */
    @Override
    public String getShopScore(String ids) {
        List<Grade> list = this.list(Wrappers.<Grade>lambdaQuery().eq(Grade::getUserId, ids));

        if(list.size()>=10){
            return Optional.ofNullable(ToolUtil.setListToNul(list)).map(grades -> {
                double score = 0;
                score = list.stream().mapToDouble(Grade::getStartLevel).sum()/grades.size();
                return score+"";
            }).orElse("0");
        }else {
            return "5";
        }

    }
}