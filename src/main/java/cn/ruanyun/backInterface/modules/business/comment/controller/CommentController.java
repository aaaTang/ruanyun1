package cn.ruanyun.backInterface.modules.business.comment.controller;


import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.comment.service.ICommentService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

/**
 * @author fei
 */
@Slf4j
@RestController
@Api(description = "评论管理接口")
@RequestMapping("/ruanyun/comment")
@Transactional
public class CommentController {

    @Autowired
    private ICommentService iCommentService;

    /**
     * 后台管理系统删除评论
     * @param id
     * @return
     */
    @PostMapping("/deleteComment")
    public Result<Object> deleteComment(String id) {

        iCommentService.deleteComment(id);
        return new ResultUtil<>().setSuccessMsg("删除成功！");
    }

    /**
     * app获取评论信息
     * @param goodId
     * @param pageVo
     * @return
     */
    @PostMapping("/getCommentVOByGoodId")
    public Result<Object> getCommentVOByGoodId(String goodId, PageVo pageVo) {

        return Optional.ofNullable(iCommentService.getCommentVOByGoodId(goodId))
                .map(commentVOS -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",commentVOS.size());
                    result.put("data", PageUtil.listToPage(pageVo,commentVOS));
                    return new ResultUtil<>().setData(result,"获取数据成功！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }

}
