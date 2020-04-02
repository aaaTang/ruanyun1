package cn.ruanyun.backInterface.modules.business.classification.service;

import cn.ruanyun.backInterface.modules.business.classification.VO.AppCategoryListVO;
import cn.ruanyun.backInterface.modules.business.classification.VO.AppCategoryVO;
import cn.ruanyun.backInterface.modules.business.classification.VO.BackAreaListVO;
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


    /**
     * 获取APP分类集合一级加二级
     */
     List<AppCategoryListVO> getAppCategoryList();

    /**
     * 按一级分类ID查询二级分类
     */
     List<AppCategoryVO> getSecondLevelCategory(String ids);


    /**
     * 后端查询一级及二级
     */
     List<BackAreaListVO> getCategoryList();

    //向上查询分类
    String getClassificationName(String id);
}