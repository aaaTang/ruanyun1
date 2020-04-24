package cn.ruanyun.backInterface.modules.business.giveLike.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.UserGiveLikeTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户点赞
 * @author z
 */
@Data
@Entity
@Table(name = "t_give_like")
@TableName("t_give_like")
public class GiveLike extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;



    private UserGiveLikeTypeEnum userGiveLikeTypeEnum;

    /**
     * 喜欢数量
     */
    private Integer likeNum;

    /**
     * 不喜欢数量
     */
    private Integer dislikeNum;
}