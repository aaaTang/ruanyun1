package cn.ruanyun.backInterface.modules.business.discountShop.service;

import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.discountShop.DTO.DiscountShopDTO;
import cn.ruanyun.backInterface.modules.business.discountShop.VO.DiscountShopListVO;
import cn.ruanyun.backInterface.modules.business.discountShop.pojo.DiscountShop;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 优惠券参加的商家接口
 * @author z
 */
public interface IDiscountShopService extends IService<DiscountShop> {


      /**
        * 插入或者更新discountShop
        * @param discountShop
       */
     void insertOrderUpdateDiscountShop(DiscountShop discountShop);



      /**
       * 移除discountShop
       * @param ids
       */
     void removeDiscountShop(String ids);


    /**
     * APP查询优惠券参与的商家列表
     * @return
     */
    List<DiscountShopListVO> getDiscountShopList(DiscountShopDTO discountShopDTO);
}