package cn.ruanyun.backInterface.modules.business.comment.serviceimpl;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.business.comment.DTO.CommentDTO;
import cn.ruanyun.backInterface.modules.business.comment.VO.CommentVO;
import cn.ruanyun.backInterface.modules.business.comment.VO.PcCommentVO;
import cn.ruanyun.backInterface.modules.business.comment.mapper.CommentMapper;
import cn.ruanyun.backInterface.modules.business.comment.pojo.Comment;
import cn.ruanyun.backInterface.modules.business.comment.service.ICommentService;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import cn.ruanyun.backInterface.modules.business.grade.pojo.Grade;
import cn.ruanyun.backInterface.modules.business.grade.service.IGradeService;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.service.IItemAttrValService;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;
import cn.ruanyun.backInterface.modules.business.order.service.IOrderService;
import cn.ruanyun.backInterface.modules.business.orderDetail.pojo.OrderDetail;
import cn.ruanyun.backInterface.modules.business.orderDetail.service.IOrderDetailService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dm.jdbc.stat.support.json.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Resource;


/**
 * 评论接口实现
 * @author wj
 */
@Slf4j
@Service
@Transactional
public class ICommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {


    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IGradeService gradeService;
    @Autowired
    private IGoodService goodService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IOrderDetailService orderDetailService;
    @Autowired
    private IItemAttrValService iItemAttrValService;
    @Resource
    private UserMapper userMapper;


    @Override
    public void insertOrderUpdateComment(CommentDTO commentDTO) {
        JSONArray jsonArray = new JSONArray(commentDTO.getComments());
        String userId = "";
        for (int i = 0; i <jsonArray.length(); i++) {
            Comment comment = JSON.parseObject(jsonArray.get(i).toString(), Comment.class);
            comment.setOrderId(commentDTO.getOrderId());
            if (!StringUtils.isEmpty(commentDTO.getOrderId())){
                Good good = goodService.getById(comment.getGoodId());
                if (EmptyUtil.isNotEmpty(good)){
                    comment.setTypeEnum(good.getTypeEnum());
                    userId = good.getCreateBy();
                }
                Order order = orderService.getById(commentDTO.getOrderId());
                if (EmptyUtil.isNotEmpty(order)){
                    comment.setUserId(order.getUserId());
                }
            }
            if (ToolUtil.isEmpty(comment.getCreateBy())) {
                comment.setCreateBy(securityUtil.getCurrUser().getId());

            } else {
                comment.setUpdateBy(securityUtil.getCurrUser().getId());
            }
            Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(comment)))
                    .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                    .toFuture().join();
        }

        //评论结束，改变订单状态
        Order order = new Order();
        order.setId(commentDTO.getOrderId());
        order.setOrderStatus(OrderStatusEnum.IS_COMPLETE);
        orderService.updateById(order);

        //添加商铺评分
        Grade grade = new Grade();
        grade.setStartLevel(Double.parseDouble(commentDTO.getStartLevel()));
        grade.setUserId(userId);
        gradeService.save(grade);

    }

    @Override
    public void removeComment(String ids) {
        CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
    }

    /**
     * 通过商品id获取评论信息
     * @param id
     * @return
     */
    @Override
    public List getCommentVOByGoodId(String id) {
        return  Optional.ofNullable(this.list(Wrappers.<Comment>lambdaQuery()
                .eq(Comment::getGoodId,id))).orElse(null);
    }

    /**
     * 商家，商品获取商品评论
     * @param comment
     * @return
     */
    @Override
    public List<CommentVO> getCommentList(Comment comment) {
        List<Comment> list = this.list(Wrappers.<Comment>lambdaQuery()
                .eq(StringUtils.isNotBlank(comment.getGoodId()), Comment::getGoodId, comment.getGoodId())
                .eq(StringUtils.isNotBlank(comment.getUserId()), Comment::getUserId, comment.getUserId()));
        return Optional.ofNullable(ToolUtil.setListToNul(list)).map(comments -> {
            List<CommentVO> commentVOS = comments.parallelStream().map(comment1 -> {
               return this.getCommentVO(comment1.getId());
            }).collect(Collectors.toList());
            return commentVOS;
        }).orElse(null);
    }

    /***
     * 后台回复评论
     * @param comment
     */
    @Override
    public void replyComment(Comment comment) {

        //查询商家是否进行过评论
        Comment cm = this.getOne(Wrappers.<Comment>lambdaQuery()
                .eq(Comment::getCreateBy,securityUtil.getCurrUser().getId())
                .eq(Comment::getPid,comment.getPid())
                .eq(Comment::getDelFlag,CommonConstant.STATUS_NORMAL)
        );

        if (ToolUtil.isEmpty(cm)) {
            comment.setCreateBy(securityUtil.getCurrUser().getId());

        } else {
            comment.setUpdateBy(securityUtil.getCurrUser().getId());
            comment.setId(cm.getId());
        }
        Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(comment)))
                .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                .toFuture().join();
    }


    public CommentVO getCommentVO(String id){
        return Optional.ofNullable(this.getById(id)).map(comment -> {
            CommentVO commentVO = new CommentVO();
            ToolUtil.copyProperties(comment,commentVO);
            commentVO.setId(comment.getId());
            //处理评论人的名字跟头像
            User byId = userService.getById(comment.getCreateBy());
            if (EmptyUtil.isNotEmpty(byId)){
                commentVO.setAvatar(byId.getAvatar());
                commentVO.setNickName(byId.getNickName());
            }
            //处理下单的规格
            OrderDetail one = orderDetailService.getOne(Wrappers.<OrderDetail>lambdaQuery()
                    .eq(OrderDetail::getGoodId, comment.getGoodId())
                    .eq(OrderDetail::getOrderId, comment.getOrderId()));
            if(ToolUtil.isNotEmpty(one)&&ToolUtil.isNotEmpty(one.getAttrSymbolPath())){
                commentVO.setItemAttrKeys(iItemAttrValService.getItemAttrVals(one.getAttrSymbolPath()));
            }

            commentVO.setType(0);
            //处理商家后台的回复
            commentVO.setReply(Optional.ofNullable(this.getOne(Wrappers.<Comment>lambdaQuery().eq(Comment::getPid, comment.getId()))).map(comment3 ->{
                commentVO.setType(1);
                commentVO.setShopCommentId(comment3.getId());
                        return this.getCommentVO(comment3.getId()).getContent();
                    }).orElse(null));
            return commentVO;
        }).orElse(null);
    }

    /**
     * 计算商品评分
     */
    @Override
    public String getGoodScore(String ids) {
        List<Comment> list = this.list(Wrappers.<Comment>lambdaQuery().eq(Comment::getGoodId, ids).eq(Comment::getPid, CommonConstant.PARENT_ID));
        return Optional.ofNullable(ToolUtil.setListToNul(list)).map(comments -> {
            double score = 0;
            score = list.stream().mapToDouble(Comment::getStartLevel).sum()/comments.size();
            return score+"";
        }).orElse("0");

    }

    @Override
    public List PcGetGoodsComment(Comment comment) {

      /*  //先查询订单下的用户评论
        Comment commentList = this.getOne(new QueryWrapper<Comment>().lambda()
                .eq(Comment::getOrderId,orderId).eq(Comment::getDelFlag,0)
        );
        List<PcCommentVO> pcCommentVO = new ArrayList<>();

        PcCommentVO pc = new PcCommentVO();
        ToolUtil.copyProperties(commentList,pc);
        pc.setNickName(Optional.ofNullable(userMapper.selectById(commentList.getCreateBy())).map(User::getNickName).orElse("未知"));
        pc.setAvatar(Optional.ofNullable(userMapper.selectById(commentList.getCreateBy())).map(User::getAvatar).orElse(null));

        pc.setReplyType(1);
        pcCommentVO.add(pc);

        //获取商家回复评论
        if(ToolUtil.isNotEmpty(commentList)){
            Comment commentList2 = this.getOne(new QueryWrapper<Comment>().lambda()
                    .eq(Comment::getPid,commentList.getId()).eq(Comment::getDelFlag,0)
            );
            PcCommentVO pc2 = new PcCommentVO();
            ToolUtil.copyProperties(commentList2,pc2);
            pc2.setNickName(Optional.ofNullable(userMapper.selectById(commentList.getCreateBy())).map(User::getNickName).orElse("未知"));
            pc2.setAvatar(Optional.ofNullable(userMapper.selectById(commentList.getCreateBy())).map(User::getAvatar).orElse(null));
            pc2.setReplyType(2);
            pcCommentVO.add(pc2);
        }

        return pcCommentVO;*/

        return  this.getCommentList(comment);
    }



}