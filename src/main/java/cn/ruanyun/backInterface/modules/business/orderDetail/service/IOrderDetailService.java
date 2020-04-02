package cn.ruanyun.backInterface.modules.business.orderDetail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.orderDetail.pojo.OrderDetail;

import java.util.List;

/**
 * 子订单接口
 * @author wj
 */
public interface IOrderDetailService extends IService<OrderDetail> {


      /**
        * 插入或者更新orderDetail
        * @param orderDetail
       */
     void insertOrderUpdateOrderDetail(OrderDetail orderDetail);



      /**
       * 移除orderDetail
       * @param ids
       */
     void removeOrderDetail(String ids);
}