package cn.ruanyun.backInterface.modules.base.service.mybatis;


import cn.ruanyun.backInterface.modules.base.pojo.Role;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author fei
 */
public interface IRoleService extends IService<Role> {


    /**
     * 通过多个角色id获取角色列表
     * @param roleIds 角色id
     * @return Role
     */
    List<Role> getRolesByRoleIds(String roleIds);


    /**
     * 通过角色名获取角色id
     * @param roleName 角色名
     * @return 角色id
     */
    String getIdByRoleName(String roleName);


    /**
     *
     * @param userId 用户id
     * @return 角色
     */
    List<String> getRoleNameByUserId(String userId);

}
