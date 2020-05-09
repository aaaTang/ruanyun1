package cn.ruanyun.backInterface.modules.business.userRelationship.serviceimpl;

import cn.ruanyun.backInterface.modules.business.userRelationship.mapper.UserRelationshipMapper;
import cn.ruanyun.backInterface.modules.business.userRelationship.pojo.UserRelationship;
import cn.ruanyun.backInterface.modules.business.userRelationship.service.IUserRelationshipService;
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
 * 用户关联管理接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IUserRelationshipServiceImpl extends ServiceImpl<UserRelationshipMapper, UserRelationship> implements IUserRelationshipService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateUserRelationship(UserRelationship userRelationship) {

           this.save(userRelationship);
       }

      @Override
      public void removeUserRelationship(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }
}