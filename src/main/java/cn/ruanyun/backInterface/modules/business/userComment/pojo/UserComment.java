package cn.ruanyun.backInterface.modules.business.userComment.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import cn.ruanyun.backInterface.common.enums.UserGiveLikeTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户评论
 * @author z
 */
@Data
@Entity
@Table(name = "t_user_comment")
@TableName("t_user_comment")
public class UserComment extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;



    /**
     * 上级id
     */
    private String pid = CommonConstant.PARENT_ID;

    /**
     * 动态，视频，问答，评论
     */
//    private String ;

    /**
     * 类型
     */
    private UserGiveLikeTypeEnum userGiveLikeTypeEnum;
    /**
     * 评价内容
     */
    private String content;

    /**
     * 评价图片
     */
    private String pics;
}