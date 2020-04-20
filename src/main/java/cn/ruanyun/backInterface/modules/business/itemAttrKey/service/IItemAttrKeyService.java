package cn.ruanyun.backInterface.modules.business.itemAttrKey.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.itemAttrKey.pojo.ItemAttrKey;

import java.util.List;

/**
 * 规格管理接口
 * @author z
 */
public interface IItemAttrKeyService extends IService<ItemAttrKey> {


      /**
        * 插入或者更新itemAttrKey
        * @param itemAttrKey
       */
     void insertOrderUpdateItemAttrKey(ItemAttrKey itemAttrKey);



      /**
       * 移除itemAttrKey
       * @param ids
       */
     void removeItemAttrKey(String ids);
}