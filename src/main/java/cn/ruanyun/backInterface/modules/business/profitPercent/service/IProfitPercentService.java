package cn.ruanyun.backInterface.modules.business.profitPercent.service;

import cn.ruanyun.backInterface.common.enums.ProfitTypeEnum;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.modules.business.profitPercent.vo.ProfitPercentVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.profitPercent.pojo.ProfitPercent;

import java.util.List;

/**
 * 分红比例接口
 * @author z
 */
public interface IProfitPercentService extends IService<ProfitPercent> {


    /**
     * 插入或者更新profitPercent
     * @param profitPercent 实体
     */
     void insertOrderUpdateProfitPercent(ProfitPercent profitPercent);

     /**
      * 移除profitPercent
      * @param ids id集合
      */
     void removeProfitPercent(String ids);

    /**
     * 获取分红列表
     * @return  List<ProfitPercentVo>
     */
    List<ProfitPercentVo> getProfitPercentList();

    /**
     * 获取最新的一个分佣数据
     * @param profitTypeEnum 分佣类型
     * @return ProfitPercent
     */
    ProfitPercent getProfitPercentLimitOne(ProfitTypeEnum profitTypeEnum);
}