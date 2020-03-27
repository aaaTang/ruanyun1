package cn.ruanyun.backInterface.modules.business.myFavorite.service;


import cn.ruanyun.backInterface.modules.business.good.VO.AppGoodListVO;
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
     * @param id
     */
    void deleteMyFavorite(String id);

    /**
     * 获取我的收藏列表
     * @return
     */
    List<AppGoodListVO> getMyFavoriteList();


}