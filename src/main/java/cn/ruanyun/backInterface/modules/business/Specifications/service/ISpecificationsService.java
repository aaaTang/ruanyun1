package cn.ruanyun.backInterface.modules.business.Specifications.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.Specifications.pojo.Specifications;

import java.util.List;

/**
 * 商品规格接口
 * @author zhu
 */
public interface ISpecificationsService extends IService<Specifications> {


      /**
        * 插入或者更新specifications
        * @param specifications
       */
     void insertOrderUpdateSpecifications(Specifications specifications);



      /**
       * 移除specifications
       * @param ids
       */
     void removeSpecifications(String ids);
}