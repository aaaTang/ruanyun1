package cn.ruanyun.backInterface.modules.business.storeServicer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.storeServicer.pojo.StoreServicer;

import java.util.List;

/**
 * 店铺客服接口
 * @author z
 */
public interface IStoreServicerService extends IService<StoreServicer> {


      /**
        * 插入或者更新storeServicer
        * @param storeServicer
       */
     void insertOrderUpdateStoreServicer(StoreServicer storeServicer);



      /**
       * 移除storeServicer
       * @param ids
       */
     void removeStoreServicer(String ids);
}