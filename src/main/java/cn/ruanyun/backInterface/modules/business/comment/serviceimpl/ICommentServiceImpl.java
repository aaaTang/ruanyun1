package cn.ruanyun.backInterface.modules.business.comment.serviceimpl;


import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.modules.base.service.UserService;
import cn.ruanyun.backInterface.modules.business.color.service.IcolorService;
import cn.ruanyun.backInterface.modules.business.comment.VO.CommentVO;
import cn.ruanyun.backInterface.modules.business.comment.entity.Comment;
import cn.ruanyun.backInterface.modules.business.comment.mapper.CommentMapper;
import cn.ruanyun.backInterface.modules.business.comment.service.ICommentService;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import cn.ruanyun.backInterface.modules.business.order.service.IOrderService;
import cn.ruanyun.backInterface.modules.business.size.service.IsizeService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 评论接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class ICommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private IGoodService goodService;

    @Autowired
    private IsizeService sizeService;

    @Autowired
    private IcolorService colorService;

    @Autowired
    private IOrderService orderService;

    /**
     * 插入评价
     *
     * @param comment
     */
    @Override
    public void insertComment(Comment comment) {

        comment.setCreateBy(securityUtil.getCurrUser().getId());
        CompletableFuture.runAsync(() -> {

            comment.setGoodId(orderService.getById(comment.getOrderId()).getGoodId());
            this.save(comment);
        });
    }

    /**
     * 删除评价信息
     *
     * @param id
     */
    @Override
    public void deleteComment(String id) {

        CompletableFuture.runAsync(() -> this.removeById(id));
    }

    /**
     * 评论信息封装类
     *
     * @param id
     * @return
     */
    @Override
    public CommentVO getCommentVO(String id) {

      return   Optional.ofNullable(this.getById(id)).map(comment -> {

            CommentVO commentVO = new CommentVO();

            //1用户信息
            Optional.ofNullable(userService.get(comment.getCreateBy()))
                    .ifPresent(user -> ToolUtil.copyProperties(user,commentVO));

            //2.评论信息
            ToolUtil.copyProperties(comment,commentVO);

            //3.商品信息
            Optional.ofNullable(orderService.getById(comment.getOrderId())).ifPresent(order ->
                    commentVO.setGoodName(goodService.getGoodName(order.getGoodId()))
                    .setSizeName(sizeService.getSizeName(order.getSizeId()))
                    .setColorName(colorService.getColorName(order.getColorId())));

            return commentVO;

        }).orElse(null);
    }


    /**
     * 通过商品查询所有评论信息limit  2
     *
     * @param goodId
     * @return
     */
    @Override
    public List<CommentVO> getCommentVOByGoodIdByLimitTwo(String goodId) {

        return Optional.ofNullable(getCommentVOByGoodId(goodId))
                .map(commentVOS -> commentVOS.parallelStream().limit(2)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()))
                .orElse(null);
    }

    /**
     * 通过商品查询所有评论信息
     *
     * @param goodId
     * @return
     */
    @Override
    public List<CommentVO> getCommentVOByGoodId(String goodId) {

        //1.基础数据
        CompletableFuture<Optional<List<Comment>>> commentList = CompletableFuture.supplyAsync(() ->
                Optional.ofNullable(getComments(goodId)));

        //2.封装数据
        CompletableFuture<List<CommentVO>> commentVOList = commentList.thenApplyAsync(comments ->
                comments.map(comments1 -> comments1.parallelStream().flatMap(comment ->
                        Stream.of(getCommentVO(comment.getId()))).collect(Collectors.toList()))
                .orElse(null));

        return commentVOList.join();
    }


    /**
     * 找出全部的基础数据
     * @param goodId
     * @return
     */
    public List<Comment> getComments(String goodId) {

        return ToolUtil.setListToNul(this.list(Wrappers.<Comment>lambdaQuery()
                .eq(Comment::getGoodId,goodId)
                .orderByDesc(Comment::getCreateTime)));
    }

}