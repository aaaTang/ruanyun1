package cn.ruanyun.backInterface.modules.business.comment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.comment.pojo.Common;

import java.util.List;

/**
 * 评价接口
 * @author fei
 */
public interface ICommonService extends IService<Common> {


      /**
        * 插入或者更新common
        * @param common
       */
     void insertOrderUpdateCommon(Common common);



      /**
       * 移除common
       * @param ids
       */
     void removeCommon(String ids);
}