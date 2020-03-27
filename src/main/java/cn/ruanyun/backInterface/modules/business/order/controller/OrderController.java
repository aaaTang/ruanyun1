package cn.ruanyun.backInterface.modules.business.order.controller;


import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.PaymentKit;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.comment.entity.Comment;
import cn.ruanyun.backInterface.modules.business.order.DTO.OrderDTO;
import cn.ruanyun.backInterface.modules.business.order.entity.Order;
import cn.ruanyun.backInterface.modules.business.order.service.IOrderService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * @author fei
 */
@Slf4j
@RestController
@Api(description = "订单管理接口")
@RequestMapping("/ruanyun/order")
@Transactional
public class OrderController {

    @Autowired
    private IOrderService iOrderService;

    /**
     * 成功的标识
     */
    private final static String SUCCESS="success";

    /**
     * 返回状态码的变量名
     *
     */
    private final static String RETURN_CODE="return_code";


    /**
     * 后台发货
     * @param orderDTO
     * @return
     */
    @PostMapping("/sendGood")
    public Result<Object> sendGood(OrderDTO orderDTO) {

        iOrderService.sendGood(orderDTO);
        return new ResultUtil<>().setSuccessMsg("发货成功！");
    }

    /**
     * 确认收货
     * @param id
     * @return
     */
    @PostMapping("/confirmOrder")
    public Result<Object> confirmOrder(String id) {

        iOrderService.confirmOrder(id);
        return new ResultUtil<>().setSuccessMsg("确认收货成功！");
    }


    /**
     * 添加评论订单信息
     * @param comment
     * @return
     */
    @PostMapping("/addComment")
    public Result<Object> addComment(Comment comment) {

        iOrderService.addComment(comment);
        return new ResultUtil<>().setSuccessMsg("评价订单成功！");
    }

    /**
     * 获取后台订单列表
     * @param orderDTO
     * @param pageVo
     * @return
     */
    @PostMapping("/getBackOrderList")
    public Result<Object> getBackOrderList(OrderDTO orderDTO, PageVo pageVo) {

        return Optional.ofNullable(iOrderService.getBackOrderList(orderDTO)).map(backOrderListVOS -> {

            Map<String,Object> result = Maps.newHashMap();
            result.put("size",backOrderListVOS.size());
            result.put("data", PageUtil.listToPage(pageVo,backOrderListVOS));
            return new ResultUtil<>().setData(result,"获取订单列表成功！");

        }).orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }

    /**
     * 获取订单详情
     * @param id
     * @return
     */
    @PostMapping("/getOrderDetailVO")
    public Result<Object> getOrderDetailVO(String id) {

        return new ResultUtil<>().setData(iOrderService.getOrderDetailVO(id),"获取订单详情成功！");
    }


    /**
     * app我的订单列表
     * @param orderDTO
     * @param pageVo
     * @return
     */
    @PostMapping("/myOrderList")
    public Result<Object> myOrderList(OrderDTO orderDTO,PageVo pageVo) {

        return Optional.ofNullable(iOrderService.myOrderList(orderDTO)).map(orderListVOS -> {

            Map<String,Object> result = Maps.newHashMap();
            result.put("size",orderListVOS.size());
            result.put("data",PageUtil.listToPage(pageVo,orderListVOS));
            return new ResultUtil<>().setData(result,"获取我的订单列表成功！");

        }).orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }


    /**
     * 获取总价格
     * @param order
     * @return
     */
    @PostMapping("/getGoodTotalMoney")
    public Result<Object> getGoodTotalMoney(Order order) {

        return new ResultUtil<>().setData(iOrderService.getGoodTotalMoney(order),"获取总价格成功！");
    }


    /*---------------------------支付接口---------------------------------*/

    /**
     * 小程序支付
     * @param order
     * @param ids
     * @return
     */
    @PostMapping("/wxAppletPayOrder")
    public Result<Object> wxAppletPayOrder(Order order, String ids) {

        try {
            return new ResultUtil<>().setData(iOrderService.wxAppletPayOrder(order, ids),"预支付成功！");
        }catch (Exception e) {
            return new ResultUtil<>().setErrorMsg(201,e.getMessage());
        }
    }
    /**
     * 微信支付回调
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/wxProPayNotify/anon")
    public void OrderNotify(HttpServletRequest request, HttpServletResponse response) {

        log.info("进入支付回调");
        String xmlMsg = PaymentKit.readData(request);

        log.info("微信小程序通知消息" + xmlMsg);

        Map<String, String> resultMap = PaymentKit.xmlToMap(xmlMsg);
        if (resultMap.get(RETURN_CODE).equals(SUCCESS)) {
            String orderNo = resultMap.get("out_trade_no");
            log.info("支付成功,订单号{}", orderNo);

            //处理支付回调信息
            iOrderService.orderNotify(orderNo);
        }
        String result = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
