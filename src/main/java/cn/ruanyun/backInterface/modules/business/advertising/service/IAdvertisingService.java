package cn.ruanyun.backInterface.modules.business.advertising.service;

import cn.ruanyun.backInterface.modules.business.advertising.VO.AppAdvertisingListVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.advertising.pojo.Advertising;

import java.util.List;

/**
 * 广告管理接口
 * @author fei
 */
public interface IAdvertisingService extends IService<Advertising> {


      /**
        * 插入或者更新advertising
        * @param advertising
       */
     void insertOrderUpdateAdvertising(Advertising advertising);



      /**
       * 移除advertising
       * @param ids
       */
     void removeAdvertising(String ids);


    /**
     * pp查询广告数据列表
     * @param advertisingType 1.开屏,  2.轮播
     * @param advertisingJumpType  1.编辑详情页  2.H5网页链接  3.活动页面  4.商家店铺首页
     * @return
     */
     List<AppAdvertisingListVO> APPgetAdvertisingList(String advertisingType, String advertisingJumpType);

    /**
     * 后端查询广告数据列表
     * @param advertisingType 1.开屏,  2.轮播
     * @param advertisingJumpType  1.编辑详情页  2.H5网页链接  3.活动页面  4.商家店铺首页
     * @return
     */
     List<Advertising> BackGetAdvertisingList(String advertisingType, String advertisingJumpType);



}