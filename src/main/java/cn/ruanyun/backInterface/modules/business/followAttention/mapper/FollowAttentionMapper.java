package cn.ruanyun.backInterface.modules.business.followAttention.mapper;

import cn.ruanyun.backInterface.modules.business.followAttention.VO.GoodFollowAttentionVO;
import cn.ruanyun.backInterface.modules.business.followAttention.VO.MefansListVO;
import cn.ruanyun.backInterface.modules.business.followAttention.VO.UserFollowAttentionVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.ruanyun.backInterface.modules.business.followAttention.pojo.FollowAttention;

import java.util.List;

/**
 * 用户关注数据处理层
 * @author zhu
 */
public interface FollowAttentionMapper extends BaseMapper<FollowAttention> {

    List<GoodFollowAttentionVO> followAttentionList(String ids);


    List<UserFollowAttentionVO> followUserList(String ids);


    List<MefansListVO> mefansList(String ids);

}