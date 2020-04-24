package cn.ruanyun.backInterface.modules.business.userDynamic.serviceimpl;

import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.business.userDynamic.DTO.UserDynamicDTO;
import cn.ruanyun.backInterface.modules.business.userDynamic.VO.GetUserDynamicListVO;
import cn.ruanyun.backInterface.modules.business.userDynamic.mapper.UserDynamicMapper;
import cn.ruanyun.backInterface.modules.business.userDynamic.pojo.UserDynamic;
import cn.ruanyun.backInterface.modules.business.userDynamic.service.IUserDynamicService;
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
 * 用户动态接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IUserDynamicServiceImpl extends ServiceImpl<UserDynamicMapper, UserDynamic> implements IUserDynamicService {


       @Autowired
       private SecurityUtil securityUtil;
       @Resource
       private UserMapper userMapper;

       @Override
       public void insertOrderUpdateUserDynamic(UserDynamic userDynamic) {

           if (ToolUtil.isEmpty(userDynamic.getCreateBy())) {

                       userDynamic.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       userDynamic.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(userDynamic)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeUserDynamic(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    /**
     * App查询用户动态
     * @return
     */
    @Override
    public List getUserDynamic(UserDynamicDTO userDynamicDTO) {

        List<UserDynamic> userDynamic = this.list(new QueryWrapper<UserDynamic>().lambda()
                .eq(ToolUtil.isNotEmpty(userDynamicDTO.getUserId()),UserDynamic::getCreateBy,userDynamicDTO.getUserId()).eq(UserDynamic::getDelFlag,0));

        List<GetUserDynamicListVO> listVO = userDynamic.parallelStream().map(userDynamic1 -> {
            GetUserDynamicListVO getUserDynamicListVO = new GetUserDynamicListVO();
            ToolUtil.copyProperties(userDynamic1,getUserDynamicListVO);

            User user =   userMapper.selectById(userDynamic1.getCreateBy());
            getUserDynamicListVO.setUserId(userDynamic1.getCreateBy()).setNickName(user.getNickName());

            //TODO:://点赞，评论数量未处理

            return getUserDynamicListVO;
        }).collect(Collectors.toList());

        return listVO;
    }


}