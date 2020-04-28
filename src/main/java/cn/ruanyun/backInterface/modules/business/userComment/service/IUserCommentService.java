package cn.ruanyun.backInterface.modules.business.userComment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.userComment.pojo.UserComment;

import java.util.List;

/**
 * 用户评论接口
 * @author z
 */
public interface IUserCommentService extends IService<UserComment> {


      /**
        * 插入或者更新userComment
        * @param userComment
       */
     void insertOrderUpdateUserComment(UserComment userComment);



      /**
       * 移除userComment
       * @param ids
       */
     void removeUserComment(String ids);
}