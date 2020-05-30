package cn.ruanyun.backInterface.modules.merchant.shopServiceTag.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.merchant.shopServiceTag.pojo.shopServiceTag;

import java.util.List;

/**
 * 商家优质服务标签接口
 * @author z
 */
public interface IshopServiceTagService extends IService<shopServiceTag> {


      /**
        * 插入或者更新shopServiceTag
        * @param shopServiceTag
       */
     void insertOrderUpdateshopServiceTag(shopServiceTag shopServiceTag);



      /**
       * 移除shopServiceTag
       * @param ids
       */
     void removeshopServiceTag(String ids);
}