package cn.ruanyun.backInterface.modules.business.followAttention.serviceimpl;

import cn.ruanyun.backInterface.common.enums.FollowTypeEnum;
import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.business.followAttention.VO.GoodFollowAttentionVO;
import cn.ruanyun.backInterface.modules.business.followAttention.VO.MefansListVO;
import cn.ruanyun.backInterface.modules.business.followAttention.VO.UserFollowAttentionVO;
import cn.ruanyun.backInterface.modules.business.followAttention.mapper.FollowAttentionMapper;
import cn.ruanyun.backInterface.modules.business.followAttention.pojo.FollowAttention;
import cn.ruanyun.backInterface.modules.business.followAttention.service.IFollowAttentionService;
import cn.ruanyun.backInterface.modules.business.myFavorite.entity.MyFavorite;
import cn.ruanyun.backInterface.modules.business.myFavorite.mapper.MyFavoriteMapper;
import cn.ruanyun.backInterface.modules.business.myFootprint.pojo.MyFootprint;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
       @Resource
       private UserMapper userMapper;


       @Override
       public void insertOrderUpdateFollowAttention(FollowAttention followAttention) {
           followAttention.setCreateBy(securityUtil.getCurrUser().getId());
           FollowAttention follow = this.getOne(Wrappers.<FollowAttention>lambdaQuery()
           .eq(FollowAttention::getCreateBy,securityUtil.getCurrUser().getId())
                   .eq(FollowAttention::getFollowTypeEnum,followAttention.getFollowTypeEnum())
                   .eq(FollowAttention::getUserId,followAttention.getUserId())
           );
           if(ToolUtil.isEmpty(follow)){
               this.save(followAttention);
           }

       }

      @Override
      public void removeFollowAttention(String userId) {

          /*FollowAttention followAttention = this.getOne(new QueryWrapper<FollowAttention>().lambda()
            .eq(FollowAttention::getCreateBy,securityUtil.getCurrUser().getId())
            .eq(FollowAttention::getUserId,userId)
          );

          if(ToolUtil.isNotEmpty(followAttention)){
              this.removeById(followAttention.getId());
          }*/

          this.remove(new QueryWrapper<FollowAttention>().lambda()
                  .eq(FollowAttention::getCreateBy,securityUtil.getCurrUser().getId())
                  .eq(FollowAttention::getUserId,userId)
          );

      }

    /**
     * 获取用户关注的商家列表
     */
    @Override
    public List<GoodFollowAttentionVO> followAttentionList() {

        List<FollowAttention> followAttentions = this.list(Wrappers.<FollowAttention>lambdaQuery()
                .eq(FollowAttention::getFollowTypeEnum, FollowTypeEnum.Follow_SHOP)
                .eq(FollowAttention::getCreateBy,securityUtil.getCurrUser().getId())
        );

        List<GoodFollowAttentionVO> goodFollowAttentionVO = followAttentions.parallelStream().map(follow -> {
            GoodFollowAttentionVO g = new GoodFollowAttentionVO();
            User user = userMapper.selectById(follow.getUserId());
            ToolUtil.copyProperties(user,g);
            return g;
        }).collect(Collectors.toList());
           return goodFollowAttentionVO;

    }

    /**
     * 获取我关注的用户列表
     */
    @Override
    public List<UserFollowAttentionVO> followUserList() {
        //获取我关注的用户列表
        List<FollowAttention> followAttention = this.list(new QueryWrapper<FollowAttention>().lambda()
        .eq(FollowAttention::getCreateBy,securityUtil.getCurrUser().getId())
                .eq(FollowAttention::getFollowTypeEnum,FollowTypeEnum.Follow_USER)
        );

        List<UserFollowAttentionVO> list = new ArrayList<>();

        if(ToolUtil.isNotEmpty(followAttention)){
            //获取我所关注的用户的粉丝数量
            for(FollowAttention userList : followAttention){
                UserFollowAttentionVO userfollVO = new UserFollowAttentionVO();
                //获取用户信息
                User user  = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getId,userList.getUserId()));
                List<FollowAttention> userData =
                        this.list(new QueryWrapper<FollowAttention>().lambda()
                                .eq(FollowAttention::getUserId, userList.getId()));

                userfollVO.setId(user.getId()).setUserid(user.getId()).setAvatar(user.getAvatar()).setUserName(user.getUsername());
                //他的粉丝数量
                userfollVO.setBeanVermicelliNum(userData.size());
                list.add(userfollVO);
            }
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
            userList.setBeanVermicelliNum((userData.size() > 0 ? userData.size() : 0));

            //关注我的人的类型
            userList.setFollowTypeEnum(userList.getFollowTypeEnum());
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
    public Integer getfollowAttentionNum() {
        List<FollowAttention> list = this.list(new QueryWrapper<FollowAttention>().lambda().eq(FollowAttention::getCreateBy,securityUtil.getCurrUser().getId()));
        Integer num = list.size();
        return (num != null ? num : 0);
    }


//    /**
//     * 查詢我是否关注这个商品
//     * @param ids
//     * @return
//     */
//    @Override
//    public Integer getFollowAttentionGood(String ids) {
//
//        FollowAttention followAttention = this.getOne(Wrappers.<FollowAttention>lambdaQuery()
//                .eq(FollowAttention::getUserId,ids).eq(FollowAttention::getCreateBy,securityUtil.getCurrUser().getId())
//        );
//        return  (followAttention != null ? 1 : 0);
//    }


    /**
     * 查詢我是否关注这个店铺或者用户
     * @param id
     * @return
     */
    @Override
    public Integer getMyFollowAttentionShop(String id, FollowTypeEnum followTypeEnum) {
        FollowAttention followAttention = this.getOne(Wrappers.<FollowAttention>lambdaQuery()
                .eq(FollowAttention::getUserId,id).eq(FollowAttention::getCreateBy,securityUtil.getCurrUser().getId())
                .eq(FollowAttention::getFollowTypeEnum,followTypeEnum)
        );
        return  (followAttention != null ? 1 : 0);
    }

}