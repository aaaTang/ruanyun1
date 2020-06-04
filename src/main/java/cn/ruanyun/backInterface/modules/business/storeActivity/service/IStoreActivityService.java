package cn.ruanyun.backInterface.modules.business.storeActivity.service;

import cn.ruanyun.backInterface.common.enums.UserTypeEnum;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.storeActivity.DTO.StoreActivityDTO;
import cn.ruanyun.backInterface.modules.business.storeActivity.VO.StoreActivityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.storeActivity.pojo.StoreActivity;

import java.util.List;

/**
 * 店铺活动接口
 * @author z
 */
public interface IStoreActivityService extends IService<StoreActivity> {


      /**
        * 插入或者更新storeActivity
        * @param storeActivity
       */
     void insertOrderUpdateStoreActivity(StoreActivity storeActivity);



      /**
       * 移除storeActivity
       * @param ids
       */
     void removeStoreActivity(String ids);


    /**
     * 获取商家活动数据
     * @param createBy
     * @return
     */
    List<StoreActivityVO> getStoreActivity(String createBy, UserTypeEnum userTypeEnum);

    /**
     * 获取商家活动列表
     * @param
     * @return
     */
    List<StoreActivityVO> getStoreActivityList(StoreActivityDTO storeActivityDTO);

    /**
     * 获取活动详情
     * @param id
     * @return
     */
    Result<Object> getActivity(String id);
}