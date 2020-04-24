package cn.ruanyun.backInterface.modules.business.userDynamic.service;

import cn.ruanyun.backInterface.modules.business.userDynamic.DTO.UserDynamicDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.userDynamic.pojo.UserDynamic;

import java.util.List;

/**
 * 用户动态接口
 * @author z
 */
public interface IUserDynamicService extends IService<UserDynamic> {


      /**
        * 插入或者更新userDynamic
        * @param userDynamic
       */
     void insertOrderUpdateUserDynamic(UserDynamic userDynamic);


      /**
       * 移除userDynamic
       * @param ids
       */
     void removeUserDynamic(String ids);

    /**
     * App查询用户动态
     * @return
     */
    List getUserDynamic(UserDynamicDTO userDynamicDTO);
}