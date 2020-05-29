package cn.ruanyun.backInterface.modules.base.serviceimpl.mybatis;


import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.RoleMapper;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.Role;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author fei
 */
@Service
public class IRoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {


    @Autowired
    private IUserRoleService userRoleService;

    @Override
    public List<Role> getRolesByRoleIds(String roleIds) {

        if (ToolUtil.isNotEmpty(roleIds)) {

            return Optional.ofNullable(ToolUtil.setListToNul(super.listByIds(ToolUtil.splitterStr(roleIds))))
                    .orElse(null);
        }

        return null;
    }

    @Override
    public String getIdByRoleName(String roleName) {

        return Optional.ofNullable(super.getOne(Wrappers.<Role>lambdaQuery()
                .eq(Role::getName, roleName)))
                .map(Role::getId)
                .orElse(null);

    }

    @Override
    public List<String> getRoleNameByUserId(String userId) {

        return Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<Role>lambdaQuery()
        .in(Role::getId, ToolUtil.splitterStr(userRoleService.getRoleIdsByUserId(userId))))))
        .map(roles -> roles.parallelStream().map(Role::getName).collect(Collectors.toList()))
        .orElse(null);
    }
}
