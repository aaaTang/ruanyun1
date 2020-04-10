package cn.ruanyun.backInterface.modules.business.order.controller;

import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.order.DTO.OrderDTO;
import cn.ruanyun.backInterface.modules.business.order.DTO.OrderShowDTO;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;
import cn.ruanyun.backInterface.modules.business.order.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author fei
 * 订单管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/order")
@Transactional
public class OrderController {

    @Autowired
    private IOrderService iOrderService;


   /**
     * app 商品直接下单
     * @param orderDTO
     * @return
    */
    @PostMapping(value = "/insertOrder")
    public Result<Object> insertOrder(OrderDTO orderDTO){
        try {
            return  iOrderService.insertOrderUpdateOrder(orderDTO);
        }catch (Exception e) {
            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

    /**
     * 支付
     * @param payTypeEnum
     * @return
     */
    @PostMapping(value = "/orderPay")
    public Result<Object> orderPay(String id , PayTypeEnum payTypeEnum){
        try {
            return iOrderService.payOrder(id,payTypeEnum);
        }catch (Exception e) {
            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 下单之前获取订单的信息
     * @param orderShowDTO
     * @return
     */
    @PostMapping(value = "/showOrder")
    public Result<Object> showOrder(OrderShowDTO orderShowDTO){
        return new ResultUtil<>().setData(iOrderService.showOrder(orderShowDTO),"获取详情成功！");
    }

    /**
     * 下单之前获取订单的信息  直接购买套餐商品
     * @param orderShowDTO
     * @return
     */
    @PostMapping(value = "/showGoodsPackageOrder")
    public Result<Object> showGoodsPackageOrder(OrderShowDTO orderShowDTO){
        return new ResultUtil<>().setData(iOrderService.showGoodsPackageOrder(orderShowDTO),"获取详情成功！");
    }

    /**
     * 移除数据
     * @param ids
     * @return
    */
    @PostMapping(value = "/removeOrder")
    public Result<Object> removeOrder(String ids){
        try {
            iOrderService.removeOrder(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

}
