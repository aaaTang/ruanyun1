package cn.ruanyun.backInterface.modules.business.dynamicVideo.serviceimpl;

import cn.ruanyun.backInterface.common.enums.DynamicTypeEnum;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.business.dynamicVideo.VO.DynamicVideoListVO;
import cn.ruanyun.backInterface.modules.business.dynamicVideo.VO.QuestionsAndAnswersVO;
import cn.ruanyun.backInterface.modules.business.dynamicVideo.mapper.DynamicVideoMapper;
import cn.ruanyun.backInterface.modules.business.dynamicVideo.pojo.DynamicVideo;
import cn.ruanyun.backInterface.modules.business.dynamicVideo.service.IDynamicVideoService;
import cn.ruanyun.backInterface.modules.business.giveLike.mapper.GiveLikeMapper;
import cn.ruanyun.backInterface.modules.business.giveLike.pojo.GiveLike;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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

import javax.annotation.Resource;


/**
 * 动态视频接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IDynamicVideoServiceImpl extends ServiceImpl<DynamicVideoMapper, DynamicVideo> implements IDynamicVideoService {


       @Autowired
       private SecurityUtil securityUtil;

       @Resource
       private UserMapper userMapper;

       @Resource
       private GiveLikeMapper giveLikeMapper;

       @Override
       public void insertOrderUpdateDynamicVideo(DynamicVideo dynamicVideo) {

           if (ToolUtil.isEmpty(dynamicVideo.getCreateBy())) {

                       dynamicVideo.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       dynamicVideo.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(dynamicVideo)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeDynamicVideo(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }


      @Override
      public List<DynamicVideoListVO> getDynamicVideo(String userId, String label,DynamicTypeEnum dynamicType){

           //TODO::获取视频
          List<DynamicVideo> list = this.list(new QueryWrapper<DynamicVideo>().lambda()
                  .eq(DynamicVideo::getDynamicTypeEnum,dynamicType)
                  .eq(ToolUtil.isNotEmpty(userId),DynamicVideo::getCreateBy,userId)
                  .like(ToolUtil.isNotEmpty(label),DynamicVideo::getLabel,label)
                  .eq(DynamicVideo::getDelFlag,0)
          );

          List<DynamicVideoListVO> dynamicVideoListVO = list.parallelStream().map(dynamicVideo -> {
              DynamicVideoListVO dv = new DynamicVideoListVO();
              User user = Optional.ofNullable(userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getId,dynamicVideo.getCreateBy()))).orElse(null);
                dv.setUserid(user.getId())//用户id
              .setUsername(user.getUsername())//用户名称
              .setAvatar(user.getAvatar())//用户图片
                        .setLikeNum(Optional.ofNullable(giveLikeMapper.selectList(new QueryWrapper<GiveLike>().lambda()//点赞数量
                                .eq(GiveLike::getDynamicVideoId,dynamicVideo.getId())).size()).orElse(0));

              ToolUtil.copyProperties(dynamicVideo,dv);
              return dv;
          }).collect(Collectors.toList());

           return dynamicVideoListVO;
      }

    @Override
    public List<QuestionsAndAnswersVO> getQuestionsAndAnswers(String userId) {

        //TODO:: 获取所有问答数据
        List<DynamicVideo> list = this.list(new QueryWrapper<DynamicVideo>().lambda()
                .eq(DynamicVideo::getDynamicTypeEnum,DynamicTypeEnum.ASK)
                .eq(ToolUtil.isNotEmpty(userId),DynamicVideo::getCreateBy,userId)
                .eq(DynamicVideo::getDelFlag,0)
        );

        List<QuestionsAndAnswersVO> questionsAndAnswers = list.parallelStream().map(dynamicVideo -> {
            QuestionsAndAnswersVO questionsAndAnswersVO = new QuestionsAndAnswersVO();
            questionsAndAnswersVO
            .setLikeNum(Optional.ofNullable(giveLikeMapper.selectList(new QueryWrapper<GiveLike>().lambda()//点赞数量
                    .eq(GiveLike::getDynamicVideoId,dynamicVideo.getId())).size()).orElse(0));

            ToolUtil.copyProperties(dynamicVideo,questionsAndAnswersVO);
            return questionsAndAnswersVO;
        }).collect(Collectors.toList());

        return questionsAndAnswers;
    }


}