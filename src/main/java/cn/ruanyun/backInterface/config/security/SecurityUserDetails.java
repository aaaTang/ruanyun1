package cn.ruanyun.backInterface.config.security;


import cn.hutool.core.util.StrUtil;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.modules.base.pojo.Permission;
import cn.ruanyun.backInterface.modules.base.pojo.Role;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IRolePermissionService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import io.netty.util.internal.UnstableApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author fei
 */
@Slf4j
public class SecurityUserDetails extends User implements UserDetails {

    private static final long serialVersionUID = 1L;

    /*@Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRolePermissionService rolePermissionService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserService userService;*/

    public SecurityUserDetails(User user) {

        if(user!=null) {
            this.setUsername(user.getUsername());
            this.setPassword(user.getPassword());
            this.setStatus(user.getStatus());
        }
    }


    /**
     * 添加用户拥有的权限和角色
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        /*List<Permission> permissions = rolePermissionService.getPermissionByRoles(userRoleService.getRoleIdsByUserId(userService.getUserIdByName(this.getUsername())));
        // 添加请求权限
        if(permissions!=null&&permissions.size()>0){
            for (Permission permission : permissions) {
                if(CommonConstant.PERMISSION_OPERATION.equals(permission.getType())
                        &&StrUtil.isNotBlank(permission.getTitle())
                        &&StrUtil.isNotBlank(permission.getPath())) {

                    authorityList.add(new SimpleGrantedAuthority(permission.getTitle()));
                }
            }
        }
        // 添加角色
        List<Role> roles = roleService.getRolesByRoleIds(userRoleService.getRoleIdsByUserId(userService.getUserIdByName(this.getUsername())));
        if(roles!=null&&roles.size()>0){
            // lambda表达式
            roles.forEach(item -> {
                if(StrUtil.isNotBlank(item.getName())){
                    authorityList.add(new SimpleGrantedAuthority(item.getName()));
                }
            });
        }*/
        return new ArrayList<>();
    }


    /**
     * 账户是否过期
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    /**
     * 是否禁用
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {

        return !CommonConstant.USER_STATUS_LOCK.equals(this.getStatus());
    }

    /**
     * 密码是否过期
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    /**
     * 是否启用
     * @return
     */
    @Override
    public boolean isEnabled() {

        return CommonConstant.USER_STATUS_NORMAL.equals(this.getStatus());
    }
}