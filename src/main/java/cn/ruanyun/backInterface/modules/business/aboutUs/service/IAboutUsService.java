package cn.ruanyun.backInterface.modules.business.aboutUs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.aboutUs.pojo.AboutUs;

import java.util.List;
import java.util.Map;

/**
 * 关于我们接口
 * @author z
 */
public interface IAboutUsService extends IService<AboutUs> {


      /**
        * 插入或者更新aboutUs
        * @param aboutUs
       */
     void insertOrderUpdateAboutUs(AboutUs aboutUs);



      /**
       * 移除aboutUs
       * @param ids
       */
     void removeAboutUs(String ids);

     AboutUs getAboutUs();
}