package cn.ruanyun.backInterface.modules.business.sizeAndRolor.service;


import cn.ruanyun.backInterface.modules.business.sizeAndRolor.VO.inventoryVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.pojo.SizeAndRolor;

import java.util.Map;

/**
 * 规格和大小接口
 * @author zhu
 */
public interface ISizeAndRolorService extends IService<SizeAndRolor> {


      /**
        * 插入或者更新sizeAndRolor
        * @param sizeAndRolor
       */
     void insertOrderUpdateSizeAndRolor(SizeAndRolor sizeAndRolor);



      /**
       * 移除sizeAndRolor
       * @param ids
       */
     void removeSizeAndRolor(String ids);


    /**
     * 获取商品规格和大小
     * @return
     */
    Map<String,Object> SizeAndRolorList(String goodsId);

    /**
     * 获取规格属性的图片价格库存
     * @return
     */
    Map<String,Object> getInventory(String ids);
//
//    /**
//     * 获取商品尺码名称
//     * @param sizeId
//     * @return
//     */
//     String getSizeName(String sizeId);
//
//    /**
//     * 获取商品颜色名称
//     * @param sizeId
//     * @return
//     */
//     String getColorName(String sizeId);
//
//    /**
//     * 获取商品库存
//     * @param sizeId
//     * @return
//     */
//     Integer getInventory(String sizeId);

}