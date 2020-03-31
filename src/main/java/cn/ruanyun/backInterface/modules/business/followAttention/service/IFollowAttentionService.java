package cn.ruanyun.backInterface.modules.business.followAttention.service;

import cn.ruanyun.backInterface.modules.business.followAttention.VO.GoodFollowAttentionVO;
import cn.ruanyun.backInterface.modules.business.followAttention.VO.MefansListVO;
import cn.ruanyun.backInterface.modules.business.followAttention.VO.UserFollowAttentionVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.followAttention.pojo.FollowAttention;

import java.util.List;

/**
 * 用户关注接口
 * @author zhu
 */
public interface IFollowAttentionService extends IService<FollowAttention> {


      /**
        * 插入或者更新followAttention
        * @param followAttention
       */
     void insertOrderUpdateFollowAttention(FollowAttention followAttention);

      /**
       * 移除followAttention
       * @param ids
       */
     void removeFollowAttention(String ids);
    /**
     * 获取用户关注的商家列表
     */
     List<GoodFollowAttentionVO> followAttentionList();

    /**
     * 获取用户关注的用户列表
     */
     List<UserFollowAttentionVO> followUserList();

    /**
     * 获取我的粉丝
     */
     List<MefansListVO> mefansList();


}