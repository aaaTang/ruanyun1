package cn.ruanyun.backInterface.modules.business.order.controller;

import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
import cn.ruanyun.backInterface.common.exception.RuanyunException;
import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.business.comment.DTO.CommentDTO;
import cn.ruanyun.backInterface.modules.business.order.dto.*;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;
import cn.ruanyun.backInterface.modules.business.order.service.IOrderService;
import cn.ruanyun.backInterface.modules.business.order.vo.AppMyOrderDetailVo;
import cn.ruanyun.backInterface.modules.business.order.vo.AppMyOrderListVo;
import cn.ruanyun.backInterface.modules.business.order.vo.BackOrderListVO;
import com.google.api.client.util.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.recycler.Recycler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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
@Api(tags = "订单接口")
public class OrderController {

    @Autowired
    private IOrderService iOrderService;


    /*-----------------------------创建订单----------------------*/

    @PostMapping("/insertOrder")
    @ApiOperation(value = "下单")
    public Result<Object> insertOrder(OrderDto orderDTO) {

        return iOrderService.insertOrder(orderDTO);
    }


    @PostMapping("/inserAuctionCalendartOrder")
    @ApiOperation(value = "档期下单")
    public Result<Object> inserAuctionCalendartOrder(AuctionCalendartOrderDTO auctionCalendartOrderDTO) {

        return iOrderService.inserAuctionCalendartOrder(auctionCalendartOrderDTO);
    }


    @PostMapping("/insertOffLineOrder")
    @ApiOperation(value = "新增线下订单")
    public Result<Object> insertOffLineOrder(OffLineOrderDto offLineOrderDto) {

        return iOrderService.insertOffLineOrder(offLineOrderDto);
    }

    @GetMapping("/insertOffLinePayTheBalanceOrder/{staffId}/{orderNum}")
    @ApiOperation("新增线下尾款支付订单")
    public Result<Object> insertOffLinePayTheBalanceOrder(@PathVariable String staffId, @PathVariable String orderNum ) {

        return iOrderService.insertOffLinePayTheBalanceOrder(staffId, orderNum);
    }

    @PostMapping("/insertDepositOrder")
    @ApiOperation("新增保证金订单")
    public Result<Object> insertDepositOrder() {

        return iOrderService.insertDepositOrder();
    }


    /*-----------------------------支付----------------------*/

    @PostMapping("/payOrder")
    @ApiOperation(value = "订单支付")
    public Result<Object> payOrder(AppPayOrderDto appPayOrder) {

        return iOrderService.payOrder(appPayOrder);
    }

    @PostMapping("/aliPayNotify")
    @ApiOperation(value = "支付回调")
    public Result<Object> aliPayNotify(HttpServletRequest request) {

        return new ResultUtil<>().setData(iOrderService.aliPayNotify(request), "支付回调");
    }

    @PostMapping("/wxPayNotify")
    @ApiOperation(value = "微信回调")
    public Result<Object> wxPayNotify(HttpServletRequest request) {

        return new ResultUtil<>().setData(iOrderService.wxPayNotify(request), "微信回调");
    }


    /*-----------------------------订单操作----------------------*/

    @PostMapping("/sendGood")
    @ApiOperation(value = "订单发货")
    public Result<Object> sendGood(OrderOperateDto orderOperateDto) {

        return iOrderService.sendGood(orderOperateDto);
    }

    @PostMapping("/updateInsurance")
    @ApiOperation(value = "领取保险")
    public Result<Object> updateInsurance(OrderOperateDto orderOperateDto) {

        return iOrderService.updateInsurance(orderOperateDto);
    }

    @PostMapping("/confirmReceive")
    @ApiOperation(value = "确认收货")
    @ApiImplicitParams(@ApiImplicitParam(name = "orderId", value = "订单id", dataType = "string", paramType = "query"))
    public Result<Object> confirmReceive(String orderId) {

        return iOrderService.confirmReceive(orderId);
    }

    @PostMapping("/payTheBalance")
    @ApiOperation(value = "支付尾款")
    public Result<Object> payTheBalance(OrderOperateDto orderOperateDto) {

        return iOrderService.payTheBalance(orderOperateDto);
    }

    @PostMapping("/confirmTheBalance")
    @ApiOperation(value = "确认尾款")
    public Result<Object> confirmTheBalance(OrderOperateDto orderOperateDto) {

        return iOrderService.confirmTheBalance(orderOperateDto);
    }

    @PostMapping("/toEvaluate")
    @ApiOperation(value = "去评价订单")
    public Result<Object> toEvaluate(CommentDTO commentDTO) {

        return iOrderService.toEvaluate(commentDTO);
    }


    @PostMapping("/cancelOrder")
    @ApiOperation(value = "取消订单")
    public Result<Object> cancelOrder(OrderOperateDto orderOperateDto) {

        return iOrderService.cancelOrder(orderOperateDto);
    }


    /*-----------------------------查询订单----------------------*/

    @PostMapping("/getMyOrderList")
    @ApiOperation(value = "获取我的订单列表")
    public Result<DataVo<AppMyOrderListVo>> getMyOrderList(PageVo pageVo, OrderStatusEnum orderStatus) {

        return iOrderService.getMyOrderList(pageVo, orderStatus);
    }


    @PostMapping("/getMyOrderDetail")
    @ApiOperation(value = "获取我的订单详情")
    public Result<AppMyOrderDetailVo> getMyOrderDetail(String id) {

        return iOrderService.getMyOrderDetail(id);
    }


    @PostMapping("/getBackOrderList")
    @ApiOperation(value = "获取后台订单列表")
    public Result<DataVo<BackOrderListVO>> getBackOrderList(BackOrderListDto backOrderListDto, PageVo pageVo) {

        return iOrderService.getBackOrderList(backOrderListDto, pageVo);
    }

    @PostMapping("/getInsuranceList")
    @ApiOperation(value = "后端获取保险订单列表")
    public Result<DataVo<BackOrderListVO>> getInsuranceList(BackOrderListDto backOrderListDto, PageVo pageVo) {

        return iOrderService.getInsuranceList(pageVo, backOrderListDto);
    }


}