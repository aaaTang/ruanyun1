package cn.ruanyun.backInterface.modules.weChat.entity;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author fei
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_we_chat")
@TableName("t_we_chat")
@ApiModel(value = "微信")
@Accessors(chain = true)
public class WeChat extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一id")
    private String openId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "是否绑定账号 默认false")
    private Boolean isRelated = false;

    @ApiModelProperty(value = "绑定用户账号")
    private String relateUsername;

}