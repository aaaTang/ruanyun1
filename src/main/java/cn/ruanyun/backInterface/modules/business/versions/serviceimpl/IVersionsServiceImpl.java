package cn.ruanyun.backInterface.modules.business.versions.serviceimpl;

import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.versions.mapper.VersionsMapper;
import cn.ruanyun.backInterface.modules.business.versions.pojo.Versions;
import cn.ruanyun.backInterface.modules.business.versions.service.IVersionsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


/**
 * 设备版本接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IVersionsServiceImpl extends ServiceImpl<VersionsMapper, Versions> implements IVersionsService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateVersions(Versions versions) {

           if (ToolUtil.isEmpty(versions.getCreateBy())) {

                       versions.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       versions.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(versions)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeVersions(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }


    /**
     * 查询当前系统版本
     * @param currentVersion  当前版本号
     * @return
     */
      @Override
      public Result<Object> getVersions(String currentVersion){
          Versions v = this.getById("100000000000001");

          if(ToolUtil.isNotEmpty(v)){

              int max = v.getNewVersion().compareTo(currentVersion);

              int min = v.getMinVersion().compareTo(currentVersion);

              if(max>0&&min>0){
                  v.setIsUpdate(true);
                  v.setForceUpdate(true);
                  return new ResultUtil<>().setData(v,"当前版本需要更新后才能使用！");
              }else if(max>0&&min<0){
                  v.setIsUpdate(true);
                  return new ResultUtil<>().setData(v,"当前版本需要更新！");
              }

          }
              return new ResultUtil<>().setSuccessMsg("系统数据有误，请联系管理员！");


      }


}