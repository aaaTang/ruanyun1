package cn.ruanyun.backInterface.modules.business.group.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.group.pojo.Group;
import cn.ruanyun.backInterface.modules.business.group.service.IGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author z
 * 群组列表管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/group")
@Transactional
public class GroupController {

    @Autowired
    private IGroupService iGroupService;


   /**
     * 更新或者插入数据
     * @param group
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateGroup")
    public Result<Object> insertOrderUpdateGroup(Group group){

        try {

            iGroupService.insertOrderUpdateGroup(group);
            return new ResultUtil<>().setSuccessMsg("插入或者更新成功!");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 移除数据
     * @param ids
     * @return
    */
    @PostMapping(value = "/removeGroup")
    public Result<Object> removeGroup(String ids){

        try {

            iGroupService.removeGroup(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


}
