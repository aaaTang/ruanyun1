package cn.ruanyun.backInterface.modules.business.orderAfterSale.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;
import cn.ruanyun.backInterface.modules.business.orderAfterSale.pojo.OrderAfterSale;
import cn.ruanyun.backInterface.modules.business.orderAfterSale.service.IOrderAfterSaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author wj
 * 售后申请管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/orderAfterSale")
@Transactional
public class OrderAfterSaleController {

    @Autowired
    private IOrderAfterSaleService iOrderAfterSaleService;


   /**
     * 更新或者插入数据
     * @param orderAfterSale
     * @return
    */
    @PostMapping(value = "/insertUpdate")
    public Result<Object> insertUpdate(OrderAfterSale orderAfterSale){

        try {
            return iOrderAfterSaleService.insertUpdate(orderAfterSale);
        }catch (Exception e) {
            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 移除数据
     * @param ids
     * @return
    */
    @PostMapping(value = "/removeOrderAfterSale")
    public Result<Object> removeOrderAfterSale(String ids){
        try {
            iOrderAfterSaleService.removeOrderAfterSale(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

    /**
     * 改变订单状态  取消订单 确认收货
     * @param orderAfterSale
     * @return
     */
    @PostMapping("/changeStatus")
    public Result<Object> changeStatus(OrderAfterSale orderAfterSale) {
        return Optional.ofNullable(iOrderAfterSaleService.getById(orderAfterSale.getId()))
                .map(byid -> new ResultUtil<>().setData(iOrderAfterSaleService.changeStatus(orderAfterSale),"操作成功！"))
                .orElse(new ResultUtil<>().setErrorMsg(202,"订单不存在！"));
    }

    /**
     * app通过订单id获取售后信息
     * @param orderId
     * @return
     */
    @PostMapping("/appOrderAfterSaleDetail")
    public Result<Object> appOrderAfterSaleDetail(String orderId) {
        return Optional.ofNullable(iOrderAfterSaleService.getByOrderId(orderId))
                .map(byid -> new ResultUtil<>().setData(iOrderAfterSaleService.getByOrderId(orderId),"操作成功！"))
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }

}
