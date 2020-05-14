package cn.ruanyun.backInterface.modules.business.profitDetail.service;

import cn.ruanyun.backInterface.modules.business.profitDetail.vo.ProfitDetailVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.profitDetail.pojo.ProfitDetail;

import java.util.List;

/**
 * 分红明细接口
 * @author z
 */
public interface IProfitDetailService extends IService<ProfitDetail> {


      /**
        * 插入或者更新profitDetail
        * @param profitDetail
       */
     void insertOrderUpdateProfitDetail(ProfitDetail profitDetail);



      /**
       * 移除profitDetail
       * @param ids
       */
     void removeProfitDetail(String ids);


    /**
     * 获取分佣明细列表数据
     * @return ProfitDetailVo
     */
    List<ProfitDetailVo> getProfitDetailList();

}