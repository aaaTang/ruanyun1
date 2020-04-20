package cn.ruanyun.backInterface.modules.business.orderDetail.service;

import cn.ruanyun.backInterface.modules.business.comment.pojo.Comment;
import cn.ruanyun.backInterface.modules.business.orderDetail.VO.OrderDetailListVO;
import cn.ruanyun.backInterface.modules.business.orderDetail.pojo.OrderDetail;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 子订单接口
 * @author wj
 */
public interface IOrderDetailService extends IService<OrderDetail> {


    /**
     * 插入或者更新orderDetail
     *
     * @param orderDetail
     */
    void insertOrderUpdateOrderDetail(OrderDetail orderDetail);


    /**
     * 移除orderDetail
     *
     * @param ids
     */
    void removeOrderDetail(String ids);


    /**
     * 获取销量
     *
     * @param id
     * @return
     */
    Integer getGoodSalesVolume(String id);

    /**
     * 获取订单列表
     *
     * @param orderId
     * @return
     */
    List<OrderDetailListVO> getOrderListByOrderId(String orderId);

}

