package cn.ruanyun.backInterface.modules.business.followAttention.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.UserTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户关注
 * @author zhu
 */
@Data
@Entity
@Table(name = "t_follow_attention")
@TableName("t_follow_attention")
public class FollowAttention extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 关注商家或用户的id
     */
    private String userId;

}