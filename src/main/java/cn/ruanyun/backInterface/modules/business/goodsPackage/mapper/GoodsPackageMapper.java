package cn.ruanyun.backInterface.modules.business.goodsPackage.mapper;

import cn.ruanyun.backInterface.modules.business.goodsPackage.DTO.ShopParticularsDTO;
import cn.ruanyun.backInterface.modules.business.goodsPackage.VO.AppGoodsPackageListVO;
import cn.ruanyun.backInterface.modules.business.goodsPackage.VO.ShopParticularsVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.ruanyun.backInterface.modules.business.goodsPackage.pojo.GoodsPackage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品套餐数据处理层
 * @author fei
 */
public interface GoodsPackageMapper extends BaseMapper<GoodsPackage> {

    /**
     * 获取App店铺详情数据成功
     */
    ShopParticularsVO  getShopParticulars(String ids);

    /**
     * 查询商家精选套餐
     */
    List<AppGoodsPackageListVO>  AppGoodsPackageList(String ids);

    /**
     * 修改店铺详情
     */
    void UpdateShopParticulars(@Param("obj")ShopParticularsDTO shopParticularsDTO);
}