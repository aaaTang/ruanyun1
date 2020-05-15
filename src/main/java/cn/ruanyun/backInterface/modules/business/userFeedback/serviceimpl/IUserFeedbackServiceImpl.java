package cn.ruanyun.backInterface.modules.business.userFeedback.serviceimpl;

import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.business.userFeedback.VO.PcGetFeedbackListVO;
import cn.ruanyun.backInterface.modules.business.userFeedback.mapper.UserFeedbackMapper;
import cn.ruanyun.backInterface.modules.business.userFeedback.pojo.UserFeedback;
import cn.ruanyun.backInterface.modules.business.userFeedback.service.IUserFeedbackService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;

import javax.annotation.Resource;


/**
 * 用户意见反馈接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IUserFeedbackServiceImpl extends ServiceImpl<UserFeedbackMapper, UserFeedback> implements IUserFeedbackService {


       @Autowired
       private SecurityUtil securityUtil;
        @Resource
        private UserMapper userMapper;

       @Override
       public void insertOrderUpdateUserFeedback(UserFeedback userFeedback) {

           if (ToolUtil.isEmpty(userFeedback.getCreateBy())) {

                       userFeedback.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       userFeedback.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(userFeedback)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeUserFeedback(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }



    /**
     * 后端查询意见反馈列表
     * @return
     */
    public List getFeedbackList(String id){

        List<UserFeedback> list = this.list(new QueryWrapper<UserFeedback>().lambda()
                .eq(ToolUtil.isNotEmpty(id),UserFeedback::getId,id)
                .eq(UserFeedback::getDelFlag,0)
                .orderByDesc(UserFeedback::getCreateTime)
        );
        List<PcGetFeedbackListVO> feedbackList = new ArrayList<>();

        for (UserFeedback userFeedback : list) {

            PcGetFeedbackListVO feedbackVO = new PcGetFeedbackListVO();
            ToolUtil.copyProperties(userFeedback,feedbackVO);

            //查询用户名称
            feedbackVO.setUserName(Optional.ofNullable(userMapper.selectById(userFeedback.getCreateBy())).map(User::getNickName).orElse("未知"));
            feedbackList.add(feedbackVO);

        }

        return feedbackList;
    }
}