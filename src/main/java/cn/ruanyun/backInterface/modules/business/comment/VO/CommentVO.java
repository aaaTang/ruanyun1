package cn.ruanyun.backInterface.modules.business.comment.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @program: ruanyun-plus
 * @description:
 * @author: fei
 * @create: 2020-02-14 12:49
 **/
@Data
@Accessors(chain = true)
public class CommentVO {

    private String id;

    /**
     * 姓名
     */
    private String username;

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
    private String goodName;

    /**
     * 评价时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;


    /**
     * 回复
     */
    private CommentVO reply;

}
