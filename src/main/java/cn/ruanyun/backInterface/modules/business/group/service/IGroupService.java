package cn.ruanyun.backInterface.modules.business.group.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.group.pojo.Group;

import java.util.List;

/**
 * 群组列表接口
 * @author z
 */
public interface IGroupService extends IService<Group> {


      /**
        * 插入或者更新group
        * @param group
       */
     void insertOrderUpdateGroup(Group group);



      /**
       * 移除group
       * @param ids
       */
     void removeGroup(String ids);
}