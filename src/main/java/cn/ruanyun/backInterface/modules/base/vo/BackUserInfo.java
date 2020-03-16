package cn.ruanyun.backInterface.modules.base.vo;

import cn.ruanyun.backInterface.modules.base.pojo.Permission;
import cn.ruanyun.backInterface.modules.base.pojo.Role;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class BackUserInfo {

    private String id;
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 性别
     */
    private String sex;

    /**
     * 地址
     */
    private String address;

    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 用户类型 0普通用户 1管理员
     */
    private Integer type;

    /**
     * 状态 默认0正常 -1拉黑
     */
    private Integer status;

    /**
     * 用户的角色信息
     */
    private List<Role> roles;

    /**
     * 用户的权限信息
     */
    private List<Permission> permissions;

}
