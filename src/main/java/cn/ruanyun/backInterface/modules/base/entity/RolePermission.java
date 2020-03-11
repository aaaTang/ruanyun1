package cn.ruanyun.backInterface.modules.base.entity;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author fei
 */
@Data
@Entity
@Table(name = "t_role_permission")
@TableName("t_role_permission")
@ApiModel(value = "角色权限")
public class RolePermission extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色id")
    private String roleId;

    @ApiModelProperty(value = "权限id")
    private String permissionId;
}