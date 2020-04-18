package cn.ruanyun.backInterface.modules.business.orderReturnReason.service;

import cn.ruanyun.backInterface.modules.business.orderReturnReason.pojo.OrderReturnReason;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 退货原因接口
 * @author wj
 */
public interface IOrderReturnReasonService extends IService<OrderReturnReason> {


      /**
        * 插入或者更新OrderReturnReason
        * @param orderReturnReason
       */
     void insertOrderUpdateOrderReturnReason(OrderReturnReason orderReturnReason);



      /**
       * 移除OrderReturnReason
       * @param ids
       */
     void removeOrderReturnReason(String ids);


    /**
     *app获取退款原因
     * @param orderReturnReason
     * @return
     */
    List getOrderReturnReasonList(OrderReturnReason orderReturnReason);
}