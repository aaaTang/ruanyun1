package cn.ruanyun.backInterface.modules.base.serviceimpl;


import cn.ruanyun.backInterface.modules.base.mapper.RoleDao;
import cn.ruanyun.backInterface.modules.base.pojo.Role;
import cn.ruanyun.backInterface.modules.base.pojo.UserRole;
import cn.ruanyun.backInterface.modules.base.service.RoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserRoleService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 角色接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private IUserRoleService userRoleService;

    @Override
    public RoleDao getRepository() {
        return roleDao;
    }

    @Override
    public List<Role> findByDefaultRole(Boolean defaultRole) {
        return roleDao.findByDefaultRole(defaultRole);
    }

    @Override
    public String getRoleNameByUserId(String userId) {

        //1.获取改用户的角色
        CompletableFuture<List<UserRole>> userRoles = CompletableFuture.supplyAsync(() ->
                userRoleService.list(Wrappers.<UserRole>lambdaQuery()
                .eq(UserRole::getUserId,userId)));


        //2.根据角色获取角色名
        CompletableFuture<String> roleName = userRoles.thenApplyAsync(userRoles1 -> {

            String roleId = userRoles1.get(0).getRoleId();

            return this.get(roleId).getName();
        });

        return roleName.join();
    }
}
