package cn.ruanyun.backInterface.modules.business.balance.service;

import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.modules.business.balance.VO.AppBalanceVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.balance.pojo.Balance;

import java.util.List;

/**
 * 余额明细接口
 * @author zhu
 */
public interface IBalanceService extends IService<Balance> {


      /**
        * 插入或者更新balance
        * @param balance
       */
     void insertOrderUpdateBalance(Balance balance);



      /**
       * 移除balance
       * @param ids
       */
     void removeBalance(String ids);

    /**
     * app 获取用户明细
     * @return
     */
    AppBalanceVO getAppBalance(PageVo pageVo);

}