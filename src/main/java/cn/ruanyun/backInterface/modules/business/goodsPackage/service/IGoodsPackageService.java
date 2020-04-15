package cn.ruanyun.backInterface.modules.business.goodsPackage.service;

import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.goodsPackage.DTO.ShopParticularsDTO;
import cn.ruanyun.backInterface.modules.business.goodsPackage.VO.*;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.goodsPackage.pojo.GoodsPackage;

import java.util.List;

/**
 * 商品套餐接口
 * @author fei
 */
public interface IGoodsPackageService extends IService<GoodsPackage> {


      /**
        * 插入或者更新goodsPackage
        * @param goodsPackage
       */
     void insertOrderUpdateGoodsPackage(GoodsPackage goodsPackage);



      /**
       * 移除goodsPackage
       * @param ids
       */
     void removeGoodsPackage(String ids);

    /**
     * App查询商家商品详情
     * @param ids
     * @return
     */
     Result<Object>  GetGoodsPackage(String ids);

    /**
     * App分类商家商品筛选
     */
    List<GoodsPackageListVO>  GetGoodsPackageList(String classId, String areaId,Integer newPrice ,String createBy);

    /**
     * 后端查询商品全部数据
     */
    List<GoodsPackage> BackGoodsPackageList();
    /**
     * 获取App店铺详情数据成功
     */
    ShopParticularsVO getShopParticulars(String ids);

    /**
     * 查询商家精选套餐
     */
    List<AppGoodsPackageListVO> AppGoodsPackageList(String ids);

    /**
     * 修改店铺详情
     */
    void UpdateShopParticulars(ShopParticularsDTO shopParticularsDTO);

    /**
     * 后端获取店铺列表
     */
    List<ShopDatelistVO> getShopDateList(String username, String shopName, Integer storeType );

    /**
     * 获取App店铺详情参数
     */
    ShopParticularsParameterVO getShopParticularsParameter(String ids);
}
