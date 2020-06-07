package cn.ruanyun.backInterface.modules.business.kotlinDemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.kotlinDemo.pojo.KotlinDemo;

import java.util.List;

/**
 * kotlin栗子接口
 * @author z
 */
public interface IKotlinDemoService extends IService<KotlinDemo> {


      /**
        * 插入或者更新kotlinDemo
        * @param kotlinDemo
       */
     void insertOrderUpdateKotlinDemo(KotlinDemo kotlinDemo);



      /**
       * 移除kotlinDemo
       * @param ids
       */
     void removeKotlinDemo(String ids);
}