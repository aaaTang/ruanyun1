package cn.ruanyun.backInterface.modules.business.selectedStore.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.selectedStore.pojo.SelectedStore;

import java.util.List;

/**
 * 严选商家接口
 * @author fei
 */
public interface ISelectedStoreService extends IService<SelectedStore> {


      /**
        * 插入或者更新selectedStore
        * @param selectedStore
       */
     void insertOrderUpdateSelectedStore(SelectedStore selectedStore);



      /**
       * 移除selectedStore
       * @param ids
       */
     void removeSelectedStore(String ids);
}