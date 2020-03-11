package cn.ruanyun.backInterface.modules.base.dao;


import cn.ruanyun.backInterface.base.RuanyunBaseDao;
import cn.ruanyun.backInterface.modules.base.entity.RoleDepartment;

import java.util.List;

/**
 * 角色部门数据处理层
 * @author fei
 */
public interface RoleDepartmentDao extends RuanyunBaseDao<RoleDepartment,String> {

    /**
     * 通过roleId获取
     * @param roleId
     * @return
     */
    List<RoleDepartment> findByRoleId(String roleId);

    /**
     * 通过角色id删除
     * @param roleId
     */
    void deleteByRoleId(String roleId);

    /**
     * 通过角色id删除
     * @param departmentId
     */
    void deleteByDepartmentId(String departmentId);
}