package cn.ruanyun.backInterface.modules.social.entity;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.MappedSuperclass;

/**
 * @author fei
 */
@Data
@MappedSuperclass
@Accessors(chain = true)
public class BaseSocialEntity extends RuanyunBaseEntity {

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
