package cn.ruanyun.backInterface.modules.business.commodity.service;

import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.commodity.VO.CommodityListVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.commodity.pojo.Commodity;

import java.util.List;


/**
 * 商品管理接口
 * @author zhu
 */
public interface ICommodityService extends IService<Commodity> {


      /**
        * 插入或者更新commodity
        * @param commodity
       */
     void insertOrderUpdateCommodity(Commodity commodity);



      /**
       * 移除commodity
       * @param ids
       */
     void removeCommodity(String ids);

    /**
     * 获取获取商品详情成功
     * @param ids
     * @return
     */
    Result<Object> AppCommodityDetails(String ids);

    /**
     * 获取商品列表
     */
    List<CommodityListVO> CommodityList(Commodity commodity);
}