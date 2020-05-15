package cn.ruanyun.backInterface.modules.business.withdrawDeposit.service;

import cn.ruanyun.backInterface.common.vo.Result;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.withdrawDeposit.pojo.WithdrawDeposit;

import java.util.List;

/**
 * 提现管理接口
 * @author z
 */
public interface IWithdrawDepositService extends IService<WithdrawDeposit> {


      /**
        * 插入数据
        * @param withdrawDeposit
       */
      Result<Object> insertWithdrawDeposit(WithdrawDeposit withdrawDeposit);

      /**
        * 插入数据
        * @param withdrawDeposit
       */
      Result<Object> UpdateWithdrawDeposit(WithdrawDeposit withdrawDeposit);


      /**
       * 移除withdrawDeposit
       * @param ids
       */
     void removeWithdrawDeposit(String ids);

    /**
     *  后端获取提现记录
     * @param withdrawDeposit
     * @return
     */
     List PcGetWithdrawDeposit(WithdrawDeposit withdrawDeposit);
}