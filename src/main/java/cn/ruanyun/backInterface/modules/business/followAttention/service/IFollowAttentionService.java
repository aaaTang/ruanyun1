package cn.ruanyun.backInterface.modules.business.followAttention.service;

import cn.ruanyun.backInterface.common.enums.FollowTypeEnum;
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
       */
     void removeFollowAttention(String userId);
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

    /**
     * 获取我的粉丝数量
     * @return
     */
    Integer getMefansNum(String ids);
    /**
     * 获取我的关注数量
     * @return
     */
    Integer getfollowAttentionNum();

//    /**
//     * 查詢我是否关注这个商品
//     * @return
//     */
//    Integer getFollowAttentionGood(String ids);

    /**
     * 查詢我是否关注这个店铺
     * @return
     */
    Integer getMyFollowAttentionShop(String ids, FollowTypeEnum followTypeEnum);
}