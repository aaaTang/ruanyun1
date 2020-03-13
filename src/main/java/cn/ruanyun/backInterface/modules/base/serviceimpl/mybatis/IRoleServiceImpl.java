package cn.ruanyun.backInterface.modules.base.serviceimpl.mybatis;


import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.RoleMapper;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.Role;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author fei
 */
@Service
public class IRoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

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
}
