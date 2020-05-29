package cn.ruanyun.backInterface.modules.business.sizeAndRolor.service;


import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.goodCategory.VO.FourDevarajasCategoryVo;
import cn.ruanyun.backInterface.modules.business.goodCategory.entity.GoodCategory;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.VO.inventoryVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.pojo.SizeAndRolor;

import java.util.List;
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
     * @param goodsId  商品id
     * @param buyState 购买状态 1购买 2租赁
     * @return
     */
    Map<String,Object> SizeAndRolorList(String goodsId,Integer buyState);

    /**
     * 获取规格属性的图片价格库存
     * @return
     */
    Map<String,Object> getInventory(String ids,String goodsId);

    /**
     * 获取配置信息
     * @param attrSymbolPath
     * @return
     */
    SizeAndRolor getOneByAttrSymbolPath(String attrSymbolPath,String createBy,String goodId);


    /**
     * WEB按商品获取库存
     * @return
     */
    List getWebInventory(String ids,String goodsId);


    /**
     * 查询规格名称
     * @param id 规格拼接id
     * @return
     */
    String attrSymbolPathName(String id);


    /**
     * 获取婚宴酒店分类数据
     * @return
     */
    Result<Object>  gerRceptionhotelCategory();



}