package cn.ruanyun.backInterface.modules.business.classification.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.classification.pojo.Classification;

import java.util.List;

/**
 * 分类管理接口
 * @author fei
 */
public interface IClassificationService extends IService<Classification> {


      /**
        * 插入或者更新classification
        * @param classification
       */
     void insertOrderUpdateClassification(Classification classification);



      /**
       * 移除classification
       * @param ids
       */
     void removeClassification(String ids);
}