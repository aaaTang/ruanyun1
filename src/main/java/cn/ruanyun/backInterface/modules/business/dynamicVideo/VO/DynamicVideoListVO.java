package cn.ruanyun.backInterface.modules.business.dynamicVideo.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Accessors(chain = true)
public class DynamicVideoListVO {

    private String id;
    /**
     * 标题
     */
    private String title;
    /**
     * 视频，图片，文字
     */
    private String video;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 喜欢数量
     */
    private Integer likeNum;

    /**
     * 用户id
     */
    private String userid;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户头像
     */
    private String avatar;




}
