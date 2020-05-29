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
     * @param orderReturnReason orderReturnReason
     */
    void insertOrderUpdateOrderReturnReason(OrderReturnReason orderReturnReason);


    /**
     * 移除OrderReturnReason
     * @param ids ids
     */
    void removeOrderReturnReason(String ids);


    /**
     * 获取退款原因
     * @param id id
     * @return String
     */
    String getReturnReason(String id);

    /**
     *app获取退款原因
     * @param orderReturnReason orderReturnReason
     * @return List<OrderReturnReason>
     */
    List<OrderReturnReason> getOrderReturnReasonList(OrderReturnReason orderReturnReason);

}