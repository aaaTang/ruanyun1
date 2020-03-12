package cn.ruanyun.backInterface.modules.base.serviceimpl.mybatis;


import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.RolePermissionMapper;
import cn.ruanyun.backInterface.modules.base.pojo.Permission;
import cn.ruanyun.backInterface.modules.base.pojo.RolePermission;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IPermissionService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IRolePermissionService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author fei
 */
@Service
public class IRolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements IRolePermissionService {


    @Autowired
    private IPermissionService permissionService;

    @Override
    public List<Permission> getPermissionByRoles(String roleIds) {

        if (ToolUtil.isEmpty(roleIds)) {

            return null;
        }
        return Optional.ofNullable(ToolUtil.setListToNul(ToolUtil.splitterStr(roleIds)))
                .flatMap(rIds -> Optional.ofNullable(ToolUtil.setListToNul(super.list(Wrappers.<RolePermission>lambdaQuery()
                        .in(RolePermission::getRoleId,rIds)
                        .orderByDesc(RolePermission::getCreateTime))))
                        .map(rolePermissions -> rolePermissions.parallelStream().flatMap(rolePermission ->
                                Stream.of(permissionService.getById(rolePermission.getPermissionId())))
                        .collect(Collectors.toList())))
                .orElse(null);
    }
}
