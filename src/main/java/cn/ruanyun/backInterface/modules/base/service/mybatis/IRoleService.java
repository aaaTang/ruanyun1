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
     * @param roleIds
     * @return
     */
    List<Role> getRolesByRoleIds(String roleIds);


    /**
     * 通过角色名获取角色id
     * @param roleName
     * @return
     */
    String getIdByRoleName(String roleName);
}
