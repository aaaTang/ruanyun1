package cn.ruanyun.backInterface.modules.base.mapper;

import cn.ruanyun.backInterface.base.RuanyunBaseDao;
import cn.ruanyun.backInterface.modules.base.pojo.Role;

import java.util.List;

/**
 * 角色数据处理层
 * @author fei
 */
public interface RoleDao extends RuanyunBaseDao<Role,String> {

    /**
     * 获取默认角色
     * @param defaultRole
     * @return
     */
    List<Role> findByDefaultRole(Boolean defaultRole);
}
