package cn.ruanyun.backInterface.modules.business.userAnswer.serviceimpl;

import cn.ruanyun.backInterface.modules.business.userAnswer.DTO.UserAnswerDTO;
import cn.ruanyun.backInterface.modules.business.userAnswer.VO.AppUserAnswerVO;
import cn.ruanyun.backInterface.modules.business.userAnswer.mapper.UserAnswerMapper;
import cn.ruanyun.backInterface.modules.business.userAnswer.pojo.UserAnswer;
import cn.ruanyun.backInterface.modules.business.userAnswer.service.IUserAnswerService;
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


/**
 * 用户问答接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IUserAnswerServiceImpl extends ServiceImpl<UserAnswerMapper, UserAnswer> implements IUserAnswerService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateUserAnswer(UserAnswer userAnswer) {

           if (ToolUtil.isEmpty(userAnswer.getCreateBy())) {

                       userAnswer.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       userAnswer.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(userAnswer)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeUserAnswer(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    /**
     * App查询用户问答列表
     * @return
     */
      @Override
      public List getUserAnswer(UserAnswerDTO userAnswerDTO) {

          List<UserAnswer> userAnswers = this.list(new QueryWrapper<UserAnswer>().lambda()
            .eq(ToolUtil.isNotEmpty(userAnswerDTO.getUserId()),UserAnswer::getCreateBy,userAnswerDTO.getUserId()).eq(UserAnswer::getDelFlag,0)
          );

          List<AppUserAnswerVO> userAnswerList = new ArrayList<>();
          for (UserAnswer userAnswer : userAnswers) {
              AppUserAnswerVO answerVO = new AppUserAnswerVO();
              ToolUtil.copyProperties(userAnswer,answerVO);
              //TODO::点赞数量和精选用户评论为处理

              userAnswerList.add(answerVO);
          }

          return Optional.ofNullable(userAnswerList).orElse(null);
      }


}