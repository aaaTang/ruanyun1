package cn.ruanyun.backInterface.modules.base.service.mybatis;


import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.pojo.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

/**
 * @author fei
 */
public interface IUserRoleService extends IService<UserRole> {


    /**
     * 通过用户id获取该用户的角色信息
     * @param userId
     * @return
     */
    String getRoleIdsByUserId(String userId);


    /**
     * 通过角色id获取用户
     * @param roleId
     * @return
     */
    List<User> getUserIdsByRoleId(String roleId);
}
