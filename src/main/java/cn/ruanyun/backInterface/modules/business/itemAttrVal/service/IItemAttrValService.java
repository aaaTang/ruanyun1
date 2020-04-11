package cn.ruanyun.backInterface.modules.business.itemAttrVal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.pojo.ItemAttrVal;

import java.util.List;

/**
 * 规格属性管理接口
 * @author z
 */
public interface IItemAttrValService extends IService<ItemAttrVal> {


      /**
        * 插入或者更新itemAttrVal
        * @param itemAttrVal
       */
     void insertOrderUpdateItemAttrVal(ItemAttrVal itemAttrVal);



      /**
       * 移除itemAttrVal
       * @param ids
       */
     void removeItemAttrVal(String ids);
}