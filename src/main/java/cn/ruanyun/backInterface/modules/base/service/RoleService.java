package cn.ruanyun.backInterface.modules.base.service;




import cn.ruanyun.backInterface.base.RuanyunBaseService;
import cn.ruanyun.backInterface.modules.base.entity.Role;

import java.util.List;

/**
 * 角色接口
 * @author fei
 */
public interface RoleService extends RuanyunBaseService<Role,String> {

    /**
     * 获取默认角色
     * @param defaultRole
     * @return
     */
    List<Role> findByDefaultRole(Boolean defaultRole);

    /**
     * 通过id获取用户角色名
     * @param userId
     * @return
     */
    String getRoleNameByUserId(String userId);


}
