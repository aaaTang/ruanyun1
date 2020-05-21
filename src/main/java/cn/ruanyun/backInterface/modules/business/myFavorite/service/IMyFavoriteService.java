package cn.ruanyun.backInterface.modules.business.myFavorite.service;


import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.good.VO.AppGoodListVO;
import cn.ruanyun.backInterface.modules.business.myFavorite.VO.GoodsFavoriteVO;
import cn.ruanyun.backInterface.modules.business.myFavorite.VO.PackageFavotiteVO;
import cn.ruanyun.backInterface.modules.business.myFavorite.entity.MyFavorite;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 我的收藏接口
 * @author fei
 */
public interface IMyFavoriteService extends IService<MyFavorite> {


    /**
     * 插入我的收藏
     * @param myFavorite
     */
    void insertMyFavorite(MyFavorite myFavorite);

    /**
     * 移除我的收藏
     */
    Result<Object> deleteMyFavorite(String goodId, GoodTypeEnum goodTypeEnum);

    /**
     * 获取我的收藏商品列表
     * @return
     */
    List<GoodsFavoriteVO> getMyGoodsFavoriteList();

    /**
     * 获取我的收藏数量
     * @return
     */
    Long getMyFavoriteNum();

    /**
     * 获取我的收藏套餐
     * @return
     */
    List<PackageFavotiteVO> getMyGoodsPackageFavoriteList();

    /**
     * 查询我是否收藏此商品
     * @param id
     * @return
     */
    Integer getMyFavorite(String id, GoodTypeEnum goodTypeEnum);
}