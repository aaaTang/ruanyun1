package cn.ruanyun.backInterface.modules.business.comment.service;


import cn.ruanyun.backInterface.modules.business.comment.VO.CommentVO;
import cn.ruanyun.backInterface.modules.business.comment.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;


import java.util.List;

/**
 * 评论接口
 * @author fei
 */
public interface ICommentService extends IService<Comment> {

    /**
     * 插入评价
     * @param comment
     */
     void insertComment(Comment comment);


    /**
     * 删除评价信息
     * @param id
     */
    void deleteComment(String id);


    /**
     * 评论信息封装类
     * @param id
     * @return
     */
    CommentVO getCommentVO(String id);


    /**
     * 通过商品查询所有评论信息limit  2
     * @param goodId
     * @return
     */
    List<CommentVO> getCommentVOByGoodIdByLimitTwo(String goodId);

    /**
     * 通过商品查询所有评论信息
     * @param goodId
     * @return
     */
    List<CommentVO> getCommentVOByGoodId(String goodId);


}