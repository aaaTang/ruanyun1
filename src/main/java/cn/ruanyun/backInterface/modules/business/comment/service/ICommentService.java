package cn.ruanyun.backInterface.modules.business.comment.service;

import cn.ruanyun.backInterface.modules.business.comment.DTO.CommentDTO;
import cn.ruanyun.backInterface.modules.business.comment.VO.CommentVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.comment.pojo.Comment;

import java.util.List;

/**
 * 评论接口
 * @author wj
 */
public interface ICommentService extends IService<Comment> {


    /**
     * 插入或者更新comment
     *
     * @param commentDTO
     */
    void insertOrderUpdateComment(CommentDTO commentDTO);




    /**
     * 移除comment
     *
     * @param ids
     */
    void removeComment(String ids);

    /**
     *
     * @param id
     * @return
     */
    List getCommentVOByGoodId(String id);

    /**
     *
     * @param comment
     * @return
     */
    List<CommentVO> getCommentList(Comment comment);

    /***
     * 后台回复评论
     * @param comment
     */
    void replyComment(Comment comment);

    /**
     * 计算商品评分
     */
    String getGoodScore(String ids);

    /**
     * 后端按订单获取评论
     * @return
     */
    List PcGetGoodsComment(Comment comment);
}