package cn.ruanyun.backInterface.modules.business.bookingOrder.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.bookingOrder.pojo.bookingOrder;
import cn.ruanyun.backInterface.modules.business.bookingOrder.service.IbookingOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author fei
 * 预约订单管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/bookingOrder")
@Transactional
public class bookingOrderController {

    @Autowired
    private IbookingOrderService ibookingOrderService;


   /**
     * 更新或者插入数据
     * @param bookingOrder
     * @return
    */
    @PostMapping(value = "/insertOrderUpdatebookingOrder")
    public Result<Object> insertOrderUpdatebookingOrder(bookingOrder bookingOrder){

        try {

            ibookingOrderService.insertOrderUpdatebookingOrder(bookingOrder);
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
    @PostMapping(value = "/removebookingOrder")
    public Result<Object> removebookingOrder(String ids){

        try {

            ibookingOrderService.removebookingOrder(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

}
