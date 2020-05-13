package cn.ruanyun.backInterface.modules.business.orderMessage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.orderMessage.pojo.OrderMessage;

import java.util.List;

/**
 * 订单消息接口
 * @author z
 */
public interface IOrderMessageService extends IService<OrderMessage> {


      /**
        * 插入或者更新orderMessage
        * @param orderMessage
       */
     void insertOrderUpdateOrderMessage(OrderMessage orderMessage);



      /**
       * 移除orderMessage
       * @param ids
       */
     void removeOrderMessage(String ids);

     List getOrderMessage();
}