package cn.ruanyun.backInterface.modules.base.mapper;


import cn.ruanyun.backInterface.base.RuanyunBaseDao;
import cn.ruanyun.backInterface.modules.base.pojo.UserRole;

import java.util.List;

/**
 * 用户角色数据处理层
 * @author fei
 */
public interface UserRoleDao extends RuanyunBaseDao<UserRole,String> {

    /**
     * 通过roleId查找
     * @param roleId
     * @return
     */
    List<UserRole> findByRoleId(String roleId);

    /**
     * 删除用户角色
     * @param userId
     */
    void deleteByUserId(String userId);
}
