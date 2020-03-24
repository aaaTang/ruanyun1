package cn.ruanyun.backInterface.modules.base.serviceimpl.mybatis;


import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserRoleMapper;
import cn.ruanyun.backInterface.modules.base.pojo.Role;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.pojo.UserRole;
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
public class IUserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {


    @Autowired
    private IUserService userService;

    @Override
    public String getRoleIdsByUserId(String userId) {

        return Optional.ofNullable(ToolUtil.setListToNul(super.list(Wrappers.<UserRole>lambdaQuery()
                .eq(UserRole::getUserId, userId)
                .orderByDesc(UserRole::getCreateTime))))
                .map(userRoles -> userRoles.parallelStream().map(UserRole::getRoleId)
                .collect(Collectors.joining(",")))
                .orElse(null);
    }

    @Override
    public List<User> getUserIdsByRoleId(String roleId) {

        return Optional.ofNullable(ToolUtil.setListToNul(super.list(Wrappers.<UserRole>lambdaQuery()
                .eq(UserRole::getRoleId, roleId)
                .orderByDesc(UserRole::getCreateTime))))
                .map(userRoles -> userRoles.parallelStream().map(userRole -> userService.getById(userRole.getUserId()))
                .collect(Collectors.toList()))
                .orElse(null);
    }
}
