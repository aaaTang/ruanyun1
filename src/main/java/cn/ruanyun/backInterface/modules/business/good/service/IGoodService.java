package cn.ruanyun.backInterface.modules.business.good.service;

import cn.ruanyun.backInterface.modules.business.good.DTO.GoodDTO;
import cn.ruanyun.backInterface.modules.business.good.VO.AppGoodDetailVO;
import cn.ruanyun.backInterface.modules.business.good.VO.AppGoodInfoVO;
import cn.ruanyun.backInterface.modules.business.good.VO.AppGoodListVO;
import cn.ruanyun.backInterface.modules.business.good.VO.AppGoodOrderVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品接口
 * @author fei
 */
public interface IGoodService extends IService<Good> {

      /**
        * 插入或者更新good
        * @param good
       */
     void insertOrderUpdateGood(Good good);


      /**
       * 移除good
       * @param ids
       */
     void removeGood(String ids);


    /**
     * 封装类，获取商品列表字段
     * @param id
     * @return
     */
    AppGoodListVO getAppGoodListVO(String id);


    /**
     * 获取商品列表
     * @param goodDTO
     * @return
     */
     List<AppGoodListVO> getAppGoodList(GoodDTO goodDTO);


    /**
     * 获取商品详情
     * @param id
     * @return
     */
    AppGoodDetailVO getAppGoodDetail(String id);


    /**
     * 获取商品购买信息
     * @param id
     * @return
     */
    AppGoodInfoVO getAppGoodInfo(String id);


    /**
     * 获取商品第一张图片
     * @param id
     * @return
     */
    String getPicLimit1(String id);


    /**
     * 获取商品名称
     * @param id
     * @return
     */
    String getGoodName(String id);

    /**
     * 获取商品积分
     * @param id
     * @return
     */
    Integer getGoodIntegral(String id);


    /**
     * 获取商品购买信息
     * @param id
     * @return
     */
    AppGoodOrderVO getAppGoodOrder(String id,String color,String size);

    /**
     * 获取商品单价
     * @param id
     * @return
     */
    BigDecimal getGoodPrice(String  id);

    /**
     * 获取商品库存
     * @param id
     * @return
     */
    Integer getInventory(String  id);


}