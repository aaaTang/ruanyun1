package cn.ruanyun.backInterface.modules.business.advertising.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.advertising.pojo.Advertising;

import java.util.List;

/**
 * 广告管理接口
 * @author fei
 */
public interface IAdvertisingService extends IService<Advertising> {


      /**
        * 插入或者更新advertising
        * @param advertising
       */
     void insertOrderUpdateAdvertising(Advertising advertising);



      /**
       * 移除advertising
       * @param ids
       */
     void removeAdvertising(String ids);
}