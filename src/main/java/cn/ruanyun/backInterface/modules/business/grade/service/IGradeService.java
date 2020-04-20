package cn.ruanyun.backInterface.modules.business.grade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.grade.pojo.Grade;

import java.util.List;

/**
 * 评分接口
 * @author wj
 */
public interface IGradeService extends IService<Grade> {


      /**
        * 插入或者更新grade
        * @param grade
       */
     void insertOrderUpdateGrade(Grade grade);



      /**
       * 移除grade
       * @param ids
       */
     void removeGrade(String ids);
}