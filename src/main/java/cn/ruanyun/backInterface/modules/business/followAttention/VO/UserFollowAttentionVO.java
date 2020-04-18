package cn.ruanyun.backInterface.modules.business.followAttention.VO;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserFollowAttentionVO {

    private String id;

    /**
     * 关注用户id
     */
    private String userid;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 关注数量
     */
    private Integer beanVermicelliNum;
//
//    /**
//     * 关注的用户是否关注你
//     */
//    private Integer userFollow;
}
