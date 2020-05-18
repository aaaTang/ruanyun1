package cn.ruanyun.backInterface.modules.business.staffManagement.service;

import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.staffManagement.dto.StaffDto;
import cn.ruanyun.backInterface.modules.business.staffManagement.vo.StaffListVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.staffManagement.pojo.StaffManagement;

import java.util.List;

/**
 * 员工管理接口
 * @author z
 */
public interface IStaffManagementService extends IService<StaffManagement> {


    /**
     * 添加员工
     * @param staffDto 入参
     * @return Object
     */
    Result<Object> addStaff(StaffDto staffDto);


    /**
     * 编辑员工
     * @param staffDto 入参
     */
    void updateStaff(StaffDto staffDto);


    /**
     * 移除员工
     * @param ids 入参
     * @return Object
     */
    Result<Object> removeStaff(String ids);


    /**
     * 获取员工列表
     * @return StaffListVo
     */
    List<StaffListVo> getStaffList();

}