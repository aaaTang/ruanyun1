package cn.ruanyun.backInterface.modules.base.service.mybatis;


import cn.ruanyun.backInterface.modules.base.pojo.Permission;
import cn.ruanyun.backInterface.modules.base.pojo.RolePermission;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @author fei
 */
public interface IRolePermissionService extends IService<RolePermission> {


    /**
     * 通过多个角色id获取权限信息
     * @param roleIds
     * @return
     */
    List<Permission> getPermissionByRoles(String roleIds);
}
