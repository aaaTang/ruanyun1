package cn.ruanyun.backInterface.modules.business.goodService.service;

import cn.ruanyun.backInterface.modules.business.goodService.GoodServerVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.goodService.pojo.GoodService;

import java.util.List;

/**
 * 商品服务接口
 * @author z
 */
public interface IGoodServiceService extends IService<GoodService> {


      /**
        * 插入或者更新goodService
        * @param goodService
       */
     void insertOrderUpdateGoodService(GoodService goodService);



      /**
       * 移除goodService
       * @param ids
       */
     void removeGoodService(String ids);


     List<GoodServerVO> getGoodsServiceList(String goodsId);
}