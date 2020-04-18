package cn.ruanyun.backInterface.modules.business.orderAfterSale.service;

import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.orderAfterSale.pojo.OrderAfterSale;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 售后申请接口
 * @author wj
 */
public interface IOrderAfterSaleService extends IService<OrderAfterSale> {


      /**
        * 插入或者更新orderAfterSale
        * @param orderAfterSale
       */
      Result<Object> insertUpdate(OrderAfterSale orderAfterSale);



      /**
       * 移除orderAfterSale
       * @param ids
       */
     void removeOrderAfterSale(String ids);

    /***
     * 改变售后状态
     * @param orderAfterSale
     * @return
     */
    Object changeStatus(OrderAfterSale orderAfterSale);


    /**
     * app通过订单id获取售后信息
     * @param orderId
     * @return
     */
    Object getByOrderId(String orderId);
}