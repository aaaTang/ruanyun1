package cn.ruanyun.backInterface.modules.business.dynamicVideo.service;

import cn.ruanyun.backInterface.common.enums.DynamicTypeEnum;
import cn.ruanyun.backInterface.modules.business.dynamicVideo.VO.DynamicVideoListVO;
import cn.ruanyun.backInterface.modules.business.dynamicVideo.VO.QuestionsAndAnswersVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.dynamicVideo.pojo.DynamicVideo;

import java.util.List;

/**
 * 动态视频接口
 * @author z
 */
public interface IDynamicVideoService extends IService<DynamicVideo> {


      /**
        * 插入或者更新dynamicVideo
        * @param dynamicVideo
       */
     void insertOrderUpdateDynamicVideo(DynamicVideo dynamicVideo);



      /**
       * 移除dynamicVideo
       * @param ids
       */
     void removeDynamicVideo(String ids);


     List<DynamicVideoListVO> getDynamicVideo(String userId, String label, DynamicTypeEnum dynamicType);


    List<QuestionsAndAnswersVO> getQuestionsAndAnswers(String userId);
}