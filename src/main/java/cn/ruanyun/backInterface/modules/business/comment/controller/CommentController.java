package cn.ruanyun.backInterface.modules.business.comment.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.comment.DTO.CommentDTO;
import cn.ruanyun.backInterface.modules.business.comment.pojo.Comment;
import cn.ruanyun.backInterface.modules.business.comment.service.ICommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wj
 * 评论管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/comment")
@Transactional
public class CommentController {

    @Autowired
    private ICommentService iCommentService;


   /**
     * 更新或者插入数据
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateComment")
    public Result<Object> insertOrderUpdateComment(CommentDTO commentDTO){
        try {
            iCommentService.insertOrderUpdateComment(commentDTO);
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
    @PostMapping(value = "/removeComment")
    public Result<Object> removeComment(String ids){
        try {
            iCommentService.removeComment(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

}
