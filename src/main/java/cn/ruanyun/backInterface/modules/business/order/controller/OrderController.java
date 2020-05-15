package cn.ruanyun.backInterface.modules.business.order.controller;

import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.good.DTO.GoodDTO;
import cn.ruanyun.backInterface.modules.business.order.DTO.OrderDTO;
import cn.ruanyun.backInterface.modules.business.order.DTO.OrderShowDTO;
import cn.ruanyun.backInterface.modules.business.order.DTO.PcOrderDTO;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;
import cn.ruanyun.backInterface.modules.business.order.service.IOrderService;
import com.google.api.client.util.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public Result<Object> orderPay(String id , PayTypeEnum payTypeEnum, String payPassword){
        try {
            return iOrderService.payOrder(id, payTypeEnum, payPassword);
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


    /**
     * 获取 我的订单
     * @param order
     * @param pageVo
     * @return
     */
    @PostMapping("/getMyOrderList")
    public Result<Object> getMyOrderList(Order order, PageVo pageVo) {
        return Optional.ofNullable(iOrderService.getOrderList(order))
                .map(orderListVOS -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",orderListVOS.size());
                    result.put("data", PageUtil.listToPage(pageVo,orderListVOS));
                    return new ResultUtil<>().setData(result,"获取订单列表成功！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }

    /**
     * 获取订单详情
     * @param id
     * @return
     */
    @PostMapping("/getOrderDetailVO")
    public Result<Object> getOrderDetailVO(String id) {
        return Optional.ofNullable(iOrderService.getById(id))
                .map(good -> new ResultUtil<>().setData(iOrderService.getAppGoodDetail(id),"获取订单详情成功！"))
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }


    /**
     * 改变订单状态  取消订单 确认收货
     * @param order
     * @return
     */
    @PostMapping("/changeStatus")
    public Result<Object> changeStatus(Order order) {
        return Optional.ofNullable(iOrderService.getById(order))
                .map(byid -> new ResultUtil<>().setData(iOrderService.changeStatus(order),"操作成功！"))
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }

    /**
     * @param request
     * @Description TD: 微信回调地址 异步通知
     * @Return java.lang.String
     * @Author sangsang
     * @Date 2020/2/11 11:56
     **/
    @RequestMapping(value = "/wxPayNotify", method = {RequestMethod.POST, RequestMethod.GET})
    public String wxReturnUrl(HttpServletRequest request) {
        return iOrderService.wxPayNotify(request);
    }

    /**
     * 支付宝回调
     * @param request
     * @return
     */
    @RequestMapping(value = "/aliPayNotify", method = {RequestMethod.POST, RequestMethod.GET})
    public String aliReturnUrl(HttpServletRequest request) {
        return iOrderService.aliPayNotify(request);
    }


    /**
     * 确认收货
     * @param orderId 订单id
     * @return  Result<Object>
     */
    @PostMapping("/confirmReceive")
    public Result<Object> confirmReceive(String orderId) {

        return iOrderService.confirmReceive(orderId);
    }


    /*************************************************后端管理开始*****************************************************/


    /**
     * 后端获取订单信息列表
     * @param pageVo
     * @return
     */
    @PostMapping("/PCgetShopOrderList")
    public Result<Object> PCgetShopOrderList(PcOrderDTO pcOrderDTO, PageVo pageVo) {
        return Optional.ofNullable(iOrderService.PCgetShopOrderList(pcOrderDTO))
                .map(pcMyorderListVOS -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",pcMyorderListVOS.size());
                    result.put("data", PageUtil.listToPage(pageVo,pcMyorderListVOS));
                    return new ResultUtil<>().setData(result,"后端获取订单信息列表成功！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }


    /*************************************************后端管理结束*****************************************************/
}