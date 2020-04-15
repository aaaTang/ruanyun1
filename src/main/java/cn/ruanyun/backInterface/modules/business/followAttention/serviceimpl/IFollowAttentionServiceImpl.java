package cn.ruanyun.backInterface.modules.business.followAttention.serviceimpl;

import cn.ruanyun.backInterface.modules.business.followAttention.VO.GoodFollowAttentionVO;
import cn.ruanyun.backInterface.modules.business.followAttention.VO.MefansListVO;
import cn.ruanyun.backInterface.modules.business.followAttention.VO.UserFollowAttentionVO;
import cn.ruanyun.backInterface.modules.business.followAttention.mapper.FollowAttentionMapper;
import cn.ruanyun.backInterface.modules.business.followAttention.pojo.FollowAttention;
import cn.ruanyun.backInterface.modules.business.followAttention.service.IFollowAttentionService;
import cn.ruanyun.backInterface.modules.business.myFootprint.pojo.MyFootprint;
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
import java.util.stream.Collector;
import java.util.stream.Collectors;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;

import javax.annotation.Resource;


/**
 * 用户关注接口实现
 * @author zhu
 */
@Slf4j
@Service
@Transactional
public class IFollowAttentionServiceImpl extends ServiceImpl<FollowAttentionMapper, FollowAttention> implements IFollowAttentionService {


       @Autowired
       private SecurityUtil securityUtil;
       @Resource
       private FollowAttentionMapper followAttentionMapper;

       @Override
       public void insertOrderUpdateFollowAttention(FollowAttention followAttention) {

           if (ToolUtil.isEmpty(followAttention.getCreateBy())) {

                       followAttention.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       followAttention.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(followAttention)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeFollowAttention(String ids) {

          CompletableFuture.runAsync(() -> this.remove(Wrappers.<FollowAttention>lambdaQuery()
                  .eq(FollowAttention::getUserId,ids).eq(FollowAttention::getCreateBy,securityUtil.getCurrUser().getId())));
      }

    /**
     * 获取用户关注的商家列表
     */
    @Override
    public List<GoodFollowAttentionVO> followAttentionList() {
           return followAttentionMapper.followAttentionList(securityUtil.getCurrUser().getId());
    }

    /**
     * 获取用户关注的用户列表
     */
    @Override
    public List<UserFollowAttentionVO> followUserList() {
        //获取用户关注的用户列表
        List<UserFollowAttentionVO> list =  followAttentionMapper.followUserList(securityUtil.getCurrUser().getId());

        //获取用户所关注的用户的粉丝数量
        for(UserFollowAttentionVO userList : list){
           List<FollowAttention> userData =
                   this.list(new QueryWrapper<FollowAttention>().lambda()
                           .eq(FollowAttention::getUserId, userList.getId()));
           //他的粉丝数量
            userList.setBeanVermicelliNum(userData.size());

        }

        return list;
    }

    /**
     * 获取我的粉丝
     */
    @Override
    public List<MefansListVO> mefansList() {
        //获取我的粉丝
        List<MefansListVO> mefansList = followAttentionMapper.mefansList(securityUtil.getCurrUser().getId());

        //获取我的粉丝的多少人关注他
        for(MefansListVO userList : mefansList){
            List<FollowAttention> userData =
                    this.list(new QueryWrapper<FollowAttention>().lambda()
                            .eq(FollowAttention::getUserId, userList.getUserid()));
            userList.setBeanVermicelliNum((userData.size() < 0 ? userData.size() : 0));

            //查询关注我的人，我是否关注他
            FollowAttention followAttention = super.getOne(new QueryWrapper<FollowAttention>().lambda()
                    .eq(FollowAttention::getCreateBy,userList.getUserid()).eq(FollowAttention::getUserId,securityUtil.getCurrUser().getId()));
            //查询结果是空，就没有关注，相反...
            userList.setUserFollow((followAttention != null ? 1 : 0));
        }
        return mefansList;
    }

    /**
     * 获取我的粉丝数量
     * @return
     */
    @Override
    public Integer getMefansNum(String ids) {
        if(ToolUtil.isEmpty(ids)){
            ids= securityUtil.getCurrUser().getId();
        }
        List<FollowAttention> list = this.list(new QueryWrapper<FollowAttention>().lambda()
                        .eq(FollowAttention::getUserId,ids));
        Integer num = list.size();
        return (num != null ? num : 0);
    }

    /**
     * 获取我的关注数量
     * @return
     */
    @Override
    public Long getfollowAttentionNum() {
        List<FollowAttention> list = this.list(new QueryWrapper<FollowAttention>().lambda().eq(FollowAttention::getCreateBy,securityUtil.getCurrUser().getId()));
        Long num = Long.valueOf(list.size());
        return (num != null ? num : 0);
    }


    /**
     * 查詢我是否关注这个商品
     * @param ids
     * @return
     */
    @Override
    public Integer getFollowAttentionGood(String ids) {

        FollowAttention followAttention = this.getOne(Wrappers.<FollowAttention>lambdaQuery()
                .eq(FollowAttention::getUserId,ids).eq(FollowAttention::getCreateBy,securityUtil.getCurrUser().getId())
        );
        return  (followAttention != null ? 1 : 0);
    }


    /**
     * 查詢我是否关注这个店铺
     * @param id
     * @return
     */
    @Override
    public Integer getMyFollowAttentionShop(String id) {
        FollowAttention followAttention = this.getOne(Wrappers.<FollowAttention>lambdaQuery()
                .eq(FollowAttention::getUserId,id).eq(FollowAttention::getCreateBy,securityUtil.getCurrUser().getId())
        );
        return  (followAttention != null ? 1 : 0);
    }

}