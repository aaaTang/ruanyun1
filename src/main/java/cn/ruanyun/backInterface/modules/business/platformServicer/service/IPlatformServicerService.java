package cn.ruanyun.backInterface.modules.business.platformServicer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.platformServicer.pojo.PlatformServicer;

import java.util.List;

/**
 * 平台客服接口
 * @author z
 */
public interface IPlatformServicerService extends IService<PlatformServicer> {


      /**
        * 插入或者更新platformServicer
        * @param platformServicer
       */
     void insertOrderUpdatePlatformServicer(PlatformServicer platformServicer);



      /**
       * 移除platformServicer
       * @param ids
       */
     void removePlatformServicer(String ids);
}