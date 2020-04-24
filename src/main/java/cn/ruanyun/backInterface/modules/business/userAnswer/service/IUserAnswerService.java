package cn.ruanyun.backInterface.modules.business.userAnswer.service;

import cn.ruanyun.backInterface.modules.business.userAnswer.DTO.UserAnswerDTO;
import cn.ruanyun.backInterface.modules.business.userDynamic.DTO.UserDynamicDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.userAnswer.pojo.UserAnswer;

import java.util.List;

/**
 * 用户问答接口
 * @author z
 */
public interface IUserAnswerService extends IService<UserAnswer> {


      /**
        * 插入或者更新userAnswer
        * @param userAnswer
       */
     void insertOrderUpdateUserAnswer(UserAnswer userAnswer);



      /**
       * 移除userAnswer
       * @param ids
       */
     void removeUserAnswer(String ids);

    /**
     * App查询用户问答列表
     * @return
     */
    List getUserAnswer(UserAnswerDTO userAnswerDTO);
}