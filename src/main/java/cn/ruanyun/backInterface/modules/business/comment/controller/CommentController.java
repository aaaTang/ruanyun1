package cn.ruanyun.backInterface.modules.business.comment.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.comment.DTO.CommentDTO;
import cn.ruanyun.backInterface.modules.business.comment.pojo.Comment;
import cn.ruanyun.backInterface.modules.business.comment.service.ICommentService;
import com.google.api.client.util.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
     * 后台回复评论
     * @return
     */
    @PostMapping(value = "/replyComment")
    public Result<Object> replyComment(Comment comment){
        try {
            iCommentService.replyComment(comment);
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

    /**
     * app 商品获取评论 商家获取评论
     * @param comment
     * @param pageVo
     * @return
     */
    @PostMapping("/getAppCommonList")
    public Result<Object> getAppGoodList(Comment comment, PageVo pageVo) {
        return Optional.ofNullable(iCommentService.getCommentList(comment))
                .map(commentListVOS -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",commentListVOS.size());
                    result.put("data", PageUtil.listToPage(pageVo,commentListVOS));
                    return new ResultUtil<>().setData(result,"获取商品列表成功！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }


    /**
     * 后端按订单获取评论
     * @param //goodId 商品id
     * @return
     */
    @PostMapping(value = "/PcGetGoodsComment")
    public Result<Object> PcGetGoodsComment(Comment comment){
        try {

            return new ResultUtil<>().setData(iCommentService.PcGetGoodsComment(comment),"获取成功!");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

}
