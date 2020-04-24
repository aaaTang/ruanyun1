package cn.ruanyun.backInterface.modules.business.userVideo.VO;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class UserVideoListVO {

    private String id;

    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户名称
     */
    private String nickName;
    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户视频标题
     */
    private String title;

    /**
     * 用户视频
     */
    private String video;

    /**
     * 点赞数量
     */
    private Date giveLikeNum;
}
