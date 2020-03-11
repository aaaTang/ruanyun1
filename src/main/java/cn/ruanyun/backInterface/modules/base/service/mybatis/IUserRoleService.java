package cn.ruanyun.backInterface.modules.base.service.mybatis;


import cn.ruanyun.backInterface.modules.base.pojo.Role;
import cn.ruanyun.backInterface.modules.base.pojo.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @author fei
 */
@CacheConfig(cacheNames = "userRole")
public interface IUserRoleService extends IService<UserRole> {


    /**
     * 通过用户id获取该用户的角色信息
     * @param userId
     * @return
     */
    String getRoleIdsByUserId(String userId);
}
