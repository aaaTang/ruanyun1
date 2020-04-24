package cn.ruanyun.backInterface.modules.business.userDynamic.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Accessors(chain = true)
public class GetUserDynamicListVO {

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
     * 动态内容
     */
    private String title;
    /**
     * 动态图片
     */
    private String pic;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    /**
     * 点赞数量
     */
    private Date giveLikeNum;
    /**
     *评论数量
     */
    private Integer commentNum;
}
