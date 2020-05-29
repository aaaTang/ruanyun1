package cn.ruanyun.backInterface.modules.business.inviteMessage.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 邀请信息
 * @author fei
 */
@Data
@Entity
@Table(name = "t_invite_message")
@TableName("t_invite_message")
public class InviteMessage extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("工作室id")
    private String studioId;

    @ApiModelProperty(value = "发起人id")
    private String initiatorUserId;

}