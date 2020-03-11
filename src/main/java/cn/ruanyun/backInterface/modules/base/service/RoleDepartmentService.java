package cn.ruanyun.backInterface.modules.base.service;



import cn.ruanyun.backInterface.base.RuanyunBaseService;
import cn.ruanyun.backInterface.modules.base.entity.RoleDepartment;

import java.util.List;

/**
 * 角色部门接口
 * @author fei
 */
public interface RoleDepartmentService extends RuanyunBaseService<RoleDepartment,String> {

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