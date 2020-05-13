package cn.ruanyun.backInterface.modules.business.userRelationship.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.userRelationship.pojo.UserRelationship;
import cn.ruanyun.backInterface.modules.business.userRelationship.service.IUserRelationshipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author z
 * 用户关联管理管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/userRelationship")
@Transactional
public class UserRelationshipController {

    @Autowired
    private IUserRelationshipService iUserRelationshipService;


   /**
     * 更新或者插入数据
     * @param userRelationship
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateUserRelationship")
    public Result<Object> insertOrderUpdateUserRelationship(UserRelationship userRelationship){

        try {

            iUserRelationshipService.insertOrderUpdateUserRelationship(userRelationship);
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
    @PostMapping(value = "/removeUserRelationship")
    public Result<Object> removeUserRelationship(String ids){

        try {

            iUserRelationshipService.removeUserRelationship(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

}
