package cn.ruanyun.backInterface.modules.business.firstRateService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.firstRateService.pojo.FirstRateService;

import java.util.List;

/**
 * 优质服务接口
 * @author fei
 */
public interface IFirstRateServiceService extends IService<FirstRateService> {


    /**
     * 插入或者更新firstRateService
     * @param firstRateService firstRateService
     */
    void insertOrderUpdateFirstRateService(FirstRateService firstRateService);


    /**
     * 移除firstRateService
     * @param ids ids
     */
    void removeFirstRateService(String ids);


    /**
     * 获取优质服务的名字
     * @param ids 优质服务的id
     * @return String
     */
    List<String> getFirstRateName(String ids);

    /**
     * 获取优质服务列表
     * @param firstRateService 实体类
     * @return
     */
    List<FirstRateService> getFirstRateService(FirstRateService firstRateService);
}