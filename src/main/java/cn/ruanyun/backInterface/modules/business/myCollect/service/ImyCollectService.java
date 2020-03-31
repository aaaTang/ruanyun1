package cn.ruanyun.backInterface.modules.business.myCollect.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.myCollect.pojo.myCollect;

import java.util.List;

/**
 * 我的收藏接口
 * @author fei
 */
public interface ImyCollectService extends IService<myCollect> {


      /**
        * 插入或者更新myCollect
        * @param myCollect
       */
     void insertOrderUpdatemyCollect(myCollect myCollect);



      /**
       * 移除myCollect
       * @param ids
       */
     void removemyCollect(String ids);
}