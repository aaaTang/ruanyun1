package cn.ruanyun.backInterface.modules.business.orderDetail.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.orderDetail.pojo.OrderDetail;
import cn.ruanyun.backInterface.modules.business.orderDetail.service.IOrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wj
 * 子订单管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/orderDetail")
@Transactional
public class OrderDetailController {

    @Autowired
    private IOrderDetailService iOrderDetailService;


   /**
     * 更新或者插入数据
     * @param orderDetail
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateOrderDetail")
    public Result<Object> insertOrderUpdateOrderDetail(OrderDetail orderDetail){

        try {

            iOrderDetailService.insertOrderUpdateOrderDetail(orderDetail);
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
    @PostMapping(value = "/removeOrderDetail")
    public Result<Object> removeOrderDetail(String ids){

        try {

            iOrderDetailService.removeOrderDetail(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

}
