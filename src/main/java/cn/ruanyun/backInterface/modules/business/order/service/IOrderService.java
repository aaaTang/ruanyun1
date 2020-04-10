package cn.ruanyun.backInterface.modules.business.order.service;

import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.order.DTO.OrderDTO;
import cn.ruanyun.backInterface.modules.business.order.DTO.OrderShowDTO;
import cn.ruanyun.backInterface.modules.business.order.VO.MyOrderVO;
import cn.ruanyun.backInterface.modules.business.order.VO.ShowOrderVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 订单接口
 *
 * @author fei
 */
public interface IOrderService extends IService<Order> {


    /**
     * 下单
     *
     * @param orderDTO
     */
    Result<Object> insertOrderUpdateOrder(OrderDTO orderDTO);

    /**
     * 支付
     *
     * @param id
     * @param payTypeEnum
     */
    Result<Object> payOrder(String id, PayTypeEnum payTypeEnum);

    /**
     * 移除order
     *
     * @param ids
     */
    void removeOrder(String ids);

    /**
     * 移除order
     *
     * @param orderShowDTO
     */
    ShowOrderVO showOrder(OrderShowDTO orderShowDTO);

    Object showGoodsPackageOrder(OrderShowDTO orderShowDTO);
}