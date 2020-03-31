package cn.ruanyun.backInterface.modules.business.order.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
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
     * 更新或者插入数据
     * @param order
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateOrder")
    public Result<Object> insertOrderUpdateOrder(Order order){

        try {

            iOrderService.insertOrderUpdateOrder(order);
            return new ResultUtil<>().setSuccessMsg("插入或者更新成功!");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
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
