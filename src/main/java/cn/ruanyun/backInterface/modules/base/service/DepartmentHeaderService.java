package cn.ruanyun.backInterface.modules.base.service;



import cn.ruanyun.backInterface.base.RuanyunBaseService;
import cn.ruanyun.backInterface.modules.base.entity.DepartmentHeader;

import java.util.List;

/**
 * 部门负责人接口
 * @author fei
 */
public interface DepartmentHeaderService extends RuanyunBaseService<DepartmentHeader,String> {

    /**
     * 通过部门和负责人类型获取
     * @param departmentId
     * @param type
     * @return
     */
    List<String> findHeaderByDepartmentId(String departmentId, Integer type);

    /**
     * 通过部门获取
     * @param departmentIds
     * @return
     */
    List<DepartmentHeader> findByDepartmentIdIn(List<String> departmentIds);

    /**
     * 通过部门id删除
     * @param departmentId
     */
    void deleteByDepartmentId(String departmentId);

    /**
     * 通过userId删除
     * @param userId
     */
    void deleteByUserId(String userId);
}