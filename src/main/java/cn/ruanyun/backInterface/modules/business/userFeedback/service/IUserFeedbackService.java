package cn.ruanyun.backInterface.modules.business.userFeedback.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.userFeedback.pojo.UserFeedback;

import java.util.List;

/**
 * 用户意见反馈接口
 * @author z
 */
public interface IUserFeedbackService extends IService<UserFeedback> {


      /**
        * 插入或者更新userFeedback
        * @param userFeedback
       */
     void insertOrderUpdateUserFeedback(UserFeedback userFeedback);



      /**
       * 移除userFeedback
       * @param ids
       */
     void removeUserFeedback(String ids);
}