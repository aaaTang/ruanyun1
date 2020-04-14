package cn.ruanyun.backInterface.modules.business.giveLike.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.giveLike.pojo.GiveLike;

import java.util.List;

/**
 * 用户点赞接口
 * @author z
 */
public interface IGiveLikeService extends IService<GiveLike> {


      /**
        * 插入或者更新giveLike
        * @param giveLike
       */
     void insertOrderUpdateGiveLike(GiveLike giveLike);



      /**
       * 移除giveLike
       * @param dynamicVideoId
       */
     void removeGiveLike(String dynamicVideoId);
}