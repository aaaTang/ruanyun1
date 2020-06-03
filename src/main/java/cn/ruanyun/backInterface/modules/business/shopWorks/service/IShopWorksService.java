package cn.ruanyun.backInterface.modules.business.shopWorks.service;

import cn.ruanyun.backInterface.modules.business.shopWorks.DTO.ShopWorksDTO;
import cn.ruanyun.backInterface.modules.business.shopWorks.VO.AppGetShopWorksListVO;
import cn.ruanyun.backInterface.modules.business.shopWorks.serviceimpl.PcShopWorksListVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.shopWorks.pojo.ShopWorks;

import java.util.List;

/**
 * 商家作品接口
 * @author z
 */
public interface IShopWorksService extends IService<ShopWorks> {


      /**
        * 插入或者更新shopWorks
        * @param shopWorks
       */
     void insertOrderUpdateShopWorks(ShopWorks shopWorks);



      /**
       * 移除shopWorks
       * @param ids
       */
     void removeShopWorks(String ids);

    /**
     * App获取商家作品列表
     * @param shopWorksDTO 实体类
     * @return
     */
    List<AppGetShopWorksListVO> AppGetShopWorksList(ShopWorksDTO shopWorksDTO);


    /**
     * 后端获取商家作品列表
     * @return
     */
    List<PcShopWorksListVO> getShopWorksList(ShopWorksDTO shopWorksDTO);
}