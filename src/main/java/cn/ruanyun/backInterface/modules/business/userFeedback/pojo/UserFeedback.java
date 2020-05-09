package cn.ruanyun.backInterface.modules.business.userFeedback.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户意见反馈
 * @author z
 */
@Data
@Entity
@Table(name = "t_user_feedback")
@TableName("t_user_feedback")
public class UserFeedback extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 意见
     */
    private String feedback;

    /**
     * 手机号码
     */
    private String mobile;
}