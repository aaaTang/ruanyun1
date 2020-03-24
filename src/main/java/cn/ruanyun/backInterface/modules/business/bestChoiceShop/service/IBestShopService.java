package cn.ruanyun.backInterface.modules.business.bestChoiceShop.service;

import cn.ruanyun.backInterface.modules.business.bestChoiceShop.VO.BackBestShopListVO;
import cn.ruanyun.backInterface.modules.business.bestChoiceShop.VO.BestShopListVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.bestChoiceShop.pojo.BestShop;

import java.util.List;

/**
 * 严选商家接口
 * @author zhu
 */
public interface IBestShopService extends IService<BestShop> {


      /**
        * 插入或者更新bestShop
        * @param bestShop
       */
     void insertOrderUpdateBestShop(BestShop bestShop);



      /**
       * 移除bestShop
       * @param ids
       */
     void removeBestShop(String ids);

    /**
     * APP严选商家
     * @param strict 是否严选1是  0否
     * @return
     */
    List<BestShopListVO> getBestChoiceShopList(String strict);

    /**
     * 后端严选商家
     * @param strict 是否严选1是  0否
     * @return
     */
    List<BackBestShopListVO> BackBestChoiceShopList(String strict);
}