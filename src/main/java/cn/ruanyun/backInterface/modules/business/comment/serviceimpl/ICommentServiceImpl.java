package cn.ruanyun.backInterface.modules.business.comment.serviceimpl;

import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.modules.business.comment.DTO.CommentDTO;
import cn.ruanyun.backInterface.modules.business.comment.VO.CommentVO;
import cn.ruanyun.backInterface.modules.business.comment.mapper.CommentMapper;
import cn.ruanyun.backInterface.modules.business.comment.pojo.Comment;
import cn.ruanyun.backInterface.modules.business.comment.service.ICommentService;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import cn.ruanyun.backInterface.modules.business.grade.pojo.Grade;
import cn.ruanyun.backInterface.modules.business.grade.service.IGradeService;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;
import cn.ruanyun.backInterface.modules.business.order.service.IOrderService;
import cn.ruanyun.backInterface.modules.business.orderDetail.pojo.OrderDetail;
import cn.ruanyun.backInterface.modules.business.orderDetail.service.IOrderDetailService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dm.jdbc.stat.support.json.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


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


    @Override
    public void insertOrderUpdateComment(CommentDTO commentDTO) {
        JSONArray jsonArray = new JSONArray(commentDTO.getComment());
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
        orderService.changeStatus(order);
        //添加商铺评分
        Grade grade = new Grade();
        grade.setStartLevel(Integer.parseInt(commentDTO.getStartLevel()));
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
     *
     * @param comment
     * @return
     */
    @Override
    public List<CommentVO> getCommentList(Comment comment) {
        return null;
    }
}