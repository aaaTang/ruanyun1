package cn.ruanyun.backInterface.modules.business.userRelationship.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户关联管理
 * @author z
 */
@Data
@Entity
@Table(name = "t_user_relationship")
@TableName("t_user_relationship")
public class UserRelationship extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 邀请人id
     */
    private String parentUserid;


}