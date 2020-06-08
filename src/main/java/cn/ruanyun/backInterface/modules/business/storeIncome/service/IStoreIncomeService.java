package cn.ruanyun.backInterface.modules.business.storeIncome.service;

import cn.ruanyun.backInterface.modules.business.storeIncome.vo.StoreIncomeCountVo;
import cn.ruanyun.backInterface.modules.business.storeIncome.vo.StoreIncomeVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.storeIncome.pojo.StoreIncome;

import java.util.List;

/**
 * 店铺收入接口
 * @author z
 */
public interface IStoreIncomeService extends IService<StoreIncome> {

    /**
     * 插入或者更新storeIncome
     * @param storeIncome storeIncome
     */
    void insertOrderUpdateStoreIncome(StoreIncome storeIncome);

    /**
     * 移除storeIncome
     * @param ids ids
     */
    void removeStoreIncome(String ids);


    /**
     * 获取收入明细列表
     * @return StoreIncomeVo
     */
    List<StoreIncomeVo> getStoreIncomeList();


    /**
     * 获取当前门店收入统计
     * @return StoreIncomeCountVo
     */
    StoreIncomeCountVo getStoreIncomeCount(String storeId);
}