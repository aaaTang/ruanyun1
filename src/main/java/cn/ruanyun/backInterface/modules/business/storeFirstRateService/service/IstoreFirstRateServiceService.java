package cn.ruanyun.backInterface.modules.business.storeFirstRateService.service;

import cn.ruanyun.backInterface.common.vo.Result;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.storeFirstRateService.pojo.StoreFirstRateService;

import java.util.List;

/**
 * 商家优质服务接口
 * @author fei
 */
public interface IstoreFirstRateServiceService extends IService<StoreFirstRateService> {


    /**
     * 插入或者更新storeFirstRateService
     * @param storeFirstRateService 参数
     */
    void insertOrderUpdateStoreFirstRateService(StoreFirstRateService storeFirstRateService);


    /**
     * 移除storeFirstRateService
     * @param ids ids
     */
    void removeStoreFirstRateService(String ids);


    /**
     * 审核商家优质服务
     * @param storeFirstRateService storeFirstRateService
     * @return Object
     */
    Result<Object> checkStoreFirstRate(StoreFirstRateService storeFirstRateService);


    /**
     *
     * @param storeId 商家id
     * @return String
     */
    List<String> getStoreFirstRateService(String storeId);


}