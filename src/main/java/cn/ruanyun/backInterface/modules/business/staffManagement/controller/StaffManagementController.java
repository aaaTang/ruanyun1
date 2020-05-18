package cn.ruanyun.backInterface.modules.business.staffManagement.controller;

import cn.ruanyun.backInterface.common.exception.RuanyunException;
import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.business.staffManagement.dto.StaffDto;
import cn.ruanyun.backInterface.modules.business.staffManagement.pojo.StaffManagement;
import cn.ruanyun.backInterface.modules.business.staffManagement.service.IStaffManagementService;
import cn.ruanyun.backInterface.modules.business.staffManagement.vo.StaffListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author z
 * 员工管理管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/staffManagement")
@Transactional
public class StaffManagementController {

    @Autowired
    private IStaffManagementService iStaffManagementService;


    /**
     * 添加员工
     * @param staffDto 入参
     * @return Object
     */
    @PostMapping("/addStaff")
    public Result<Object> addStaff(StaffDto staffDto) {

        return iStaffManagementService.addStaff(staffDto);
    }


    /**
     * 编辑员工
     * @param staffDto 入参
     * @return Object
     */
    @PostMapping("/updateStaff")
    public Result<Object> updateStaff(StaffDto staffDto) {

        try {

            iStaffManagementService.updateStaff(staffDto);
            return new ResultUtil<>().setSuccessMsg("编辑成功！");
        }catch (RuanyunException exception) {

            return new ResultUtil<>().setErrorMsg(exception.getMsg());
        }
    }


    /**
     * 移除员工
     * @param ids ids
     * @return Object
     */
    @PostMapping("/removeStaff")
    public Result<Object> removeStaff(String ids) {

        return iStaffManagementService.removeStaff(ids);
    }


    /**
     * 获取员工列表
     * @param pageVo 分页参数
     * @return Object
     */
    @PostMapping("/getStaffList")
    public Result<Object> getStaffList(PageVo pageVo) {

        return Optional.ofNullable(iStaffManagementService.getStaffList())
                .map(staffListVos -> {

                    DataVo<StaffListVo> result = new DataVo<>();
                    result.setDataResult(PageUtil.listToPage(pageVo, staffListVos))
                            .setTotalNumber(staffListVos.size());

                    return new ResultUtil<>().setData(result, "获取员工数据成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无数据！"));
    }

}
