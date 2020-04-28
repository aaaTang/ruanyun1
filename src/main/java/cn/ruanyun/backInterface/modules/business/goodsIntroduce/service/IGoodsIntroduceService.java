package cn.ruanyun.backInterface.modules.business.goodsIntroduce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.goodsIntroduce.pojo.GoodsIntroduce;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * 商品介绍接口
 * @author z
 */
public interface IGoodsIntroduceService extends IService<GoodsIntroduce> {


      /**
        * 插入或者更新goodsIntroduce
        * @param goodsIntroduce
       */
     void insertOrderUpdateGoodsIntroduce(GoodsIntroduce goodsIntroduce);



      /**
       * 移除goodsIntroduce
       * @param ids
       */
     void removeGoodsIntroduce(String ids);


    /**
     * 按商品id查询商品介绍
     * @param ids
     * @param goodsPackageId  套餐id
     * @param introduceAndDuy   1商品线详情   2 购买须知
     * @return
     */
     List goodsIntroduceList(String ids,String goodsPackageId,Integer introduceAndDuy);
}