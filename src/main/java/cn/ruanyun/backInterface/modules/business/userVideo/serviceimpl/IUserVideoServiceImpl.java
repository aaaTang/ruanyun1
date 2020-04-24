package cn.ruanyun.backInterface.modules.business.userVideo.serviceimpl;

import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.business.userDynamic.VO.GetUserDynamicListVO;
import cn.ruanyun.backInterface.modules.business.userVideo.DTO.UserVideoDTO;
import cn.ruanyun.backInterface.modules.business.userVideo.VO.UserVideoListVO;
import cn.ruanyun.backInterface.modules.business.userVideo.mapper.UserVideoMapper;
import cn.ruanyun.backInterface.modules.business.userVideo.pojo.UserVideo;
import cn.ruanyun.backInterface.modules.business.userVideo.service.IUserVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;

import javax.annotation.Resource;


/**
 * 用户视频接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IUserVideoServiceImpl extends ServiceImpl<UserVideoMapper, UserVideo> implements IUserVideoService {


       @Autowired
       private SecurityUtil securityUtil;

       @Resource
       private UserMapper userMapper;

       @Override
       public void insertOrderUpdateUserVideo(UserVideo userVideo) {

           if (ToolUtil.isEmpty(userVideo.getCreateBy())) {

                       userVideo.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       userVideo.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(userVideo)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeUserVideo(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    @Override
    public List getUserVideoList(UserVideoDTO userVideoDTO) {
           List<UserVideo> userVideoList = this.list(new QueryWrapper<UserVideo>().lambda()
                   .eq(UserVideo::getCreateBy,userVideoDTO.userId).eq(UserVideo::getDelFlag,0)
           );

        List<UserVideoListVO> listVO = userVideoList.parallelStream().map(userVideo -> {
            UserVideoListVO userVideoListVO = new UserVideoListVO();
            ToolUtil.copyProperties(userVideo,userVideoListVO);

            User user =  userMapper.selectById(userVideo.getCreateBy());
            userVideoListVO.setUserId(userVideo.getCreateBy()).setNickName(user.getNickName());

            //TODO:://点赞数量未处理
            return userVideoListVO;
        }).collect(Collectors.toList());

           return listVO;
    }


}