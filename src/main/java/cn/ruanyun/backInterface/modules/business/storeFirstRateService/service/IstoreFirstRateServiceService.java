package cn.ruanyun.backInterface.modules.business.storeFirstRateService.service;

import cn.ruanyun.backInterface.common.enums.CheckEnum;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.storeFirstRateService.DTO.StoreFirstRateServiceDTO;
import cn.ruanyun.backInterface.modules.business.storeFirstRateService.VO.StoreFirstRateServiceVO;
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
     * 获取商家请申请优质服务的记录列表
     * @param storeFirstRateServiceDTO 实体类
     * @return
     */
    List<StoreFirstRateServiceVO> getStoreFirstRateService(StoreFirstRateServiceDTO storeFirstRateServiceDTO);

    /**
     * 获取商家的优质服务名称
     * @param createBy  创建人
     * @param checkEnum 状态
     * @return
     */
    List<String> getStoreFirstRateServiceName(String createBy, CheckEnum checkEnum);
}