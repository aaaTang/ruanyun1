package cn.ruanyun.backInterface.modules.business.userRelationship.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.userRelationship.pojo.UserRelationship;

import java.util.List;

/**
 * 用户关联管理接口
 * @author z
 */
public interface IUserRelationshipService extends IService<UserRelationship> {


      /**
        * 插入或者更新userRelationship
        * @param userRelationship
       */
     void insertOrderUpdateUserRelationship(UserRelationship userRelationship);



      /**
       * 移除userRelationship
       * @param ids
       */
     void removeUserRelationship(String ids);


    /**
     * 获取推荐人id
     * @param userId
     * @return
     */
    String getRelationUser(String userId);


    /**
     * 查看我的下级
     * @param userId 用户id
     * @return List<UserRelationship>
     */
    List<UserRelationship> getUserRelationshipListByUserId(String userId);

}