package cn.ruanyun.backInterface.modules.business.myFootprint.service;

import cn.ruanyun.backInterface.modules.business.myFootprint.VO.MyFootprintVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.myFootprint.pojo.MyFootprint;

import java.util.List;

/**
 * 用户足迹接口
 * @author zhu
 */
public interface IMyFootprintService extends IService<MyFootprint> {


      /**
        * 插入或者更新myFootprint
        * @param myFootprint
       */
     void insertOrderUpdateMyFootprint(MyFootprint myFootprint);



      /**
       * 移除myFootprint
       * @param ids
       */
     void removeMyFootprint(String ids);

    /**
     * 获取用户足迹列表
     * @return
     */
     List<MyFootprintVO> MyFootprintList();

    /**
     * 获取足迹数量
     * @return
     */
    Long getMyFootprintNum();


    /**
     * 通过商家id获取我的足迹列表
     * @return  MyFootprint
     */
    List<MyFootprint> getMyFootPrintByStoreId(String storeId);

}