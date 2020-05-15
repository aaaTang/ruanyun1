package cn.ruanyun.backInterface.modules.business.comment.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class PcCommentVO {


    private String id;

    /**
     * 姓名
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 评价星级
     */
    private Integer startLevel;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 照片
     */
    private String pics;

    /**
     * 评价时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;


    /**
     * 回复 1用户评论  2商家回复
     */
    private Integer replyType;
}
