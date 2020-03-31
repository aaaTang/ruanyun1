package cn.ruanyun.backInterface.modules.business.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;

import java.util.List;

/**
 * 订单接口
 * @author fei
 */
public interface IOrderService extends IService<Order> {


      /**
        * 插入或者更新order
        * @param order
       */
     void insertOrderUpdateOrder(Order order);



      /**
       * 移除order
       * @param ids
       */
     void removeOrder(String ids);
}