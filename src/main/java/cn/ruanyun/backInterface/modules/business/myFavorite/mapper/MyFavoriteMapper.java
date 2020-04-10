package cn.ruanyun.backInterface.modules.business.myFavorite.mapper;


import cn.ruanyun.backInterface.modules.business.myFavorite.entity.MyFavorite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 我的收藏数据处理层
 * @author fei
 */
public interface MyFavoriteMapper extends BaseMapper<MyFavorite> {

    void deleteMyFavorite(String goodsId, String userid);

}