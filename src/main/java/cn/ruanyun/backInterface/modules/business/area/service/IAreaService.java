package cn.ruanyun.backInterface.modules.business.area.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.area.pojo.Area;

import java.util.List;

/**
 * 城市管理接口
 * @author fei
 */
public interface IAreaService extends IService<Area> {


      /**
        * 插入或者更新area
        * @param area
       */
     void insertOrderUpdateArea(Area area);



      /**
       * 移除area
       * @param ids
       */
     void removeArea(String ids);
}