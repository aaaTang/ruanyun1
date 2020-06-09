package cn.ruanyun.backInterface.modules.business.advertising.service;

import cn.ruanyun.backInterface.common.enums.AdvertisingTypeEnum;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.business.advertising.vo.AppAdvertisingListVo;
import cn.ruanyun.backInterface.modules.business.advertising.vo.BackAdvertisingListVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.advertising.pojo.Advertising;

import java.util.List;

/**
 * 广告管理接口
 * @author fei
 */
public interface IAdvertisingService extends IService<Advertising> {


    /**
     * 添加轮播图数据
     * @param advertising 广告
     * @return Object
     */
    Result<Object> insertOrderUpdateAdvertising(Advertising advertising);

    /**
     * 移除advertising
     * @param ids ids
     */
    void removeAdvertising(String ids);


    /**
     * App查询广告数据列表
     * @param advertisingTypeEnum 广告类型
     * @return AppAdvertisingListVo
      */
    List<AppAdvertisingListVo> getAppAdvertisingList(AdvertisingTypeEnum advertisingTypeEnum);


    /**
     * 后端查询广告数据列表
     * @param pageVo 分页
     * @param advertising 广告
     * @return BackAdvertisingListVo
     */
    Result<DataVo<BackAdvertisingListVo>> getBackAdvertisingList(PageVo pageVo, Advertising advertising);


}