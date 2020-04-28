package cn.ruanyun.backInterface.modules.business.userComment.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.userComment.pojo.UserComment;
import cn.ruanyun.backInterface.modules.business.userComment.service.IUserCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author z
 * 用户评论管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/userComment")
@Transactional
public class UserCommentController {

    @Autowired
    private IUserCommentService iUserCommentService;


   /**
     * 更新或者插入数据
     * @param userComment
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateUserComment")
    public Result<Object> insertOrderUpdateUserComment(UserComment userComment){

        try {

            iUserCommentService.insertOrderUpdateUserComment(userComment);
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
    @PostMapping(value = "/removeUserComment")
    public Result<Object> removeUserComment(String ids){

        try {

            iUserCommentService.removeUserComment(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

}
