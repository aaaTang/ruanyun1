package cn.ruanyun.backInterface.modules.business.orderAfterSale.controller;

import cn.ruanyun.backInterface.common.exception.RuanyunException;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;
import cn.ruanyun.backInterface.modules.business.orderAfterSale.dto.OrderAfterSaleDto;
import cn.ruanyun.backInterface.modules.business.orderAfterSale.pojo.OrderAfterSale;
import cn.ruanyun.backInterface.modules.business.orderAfterSale.service.IOrderAfterSaleService;
import io.swagger.annotations.Api;
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
@Api(tags = "售后管理接口")
public class OrderAfterSaleController {


    @Autowired
    private IOrderAfterSaleService iOrderAfterSaleService;


    /**
     * 商家处理售后订单(&&管理员强制退款)
     * @param orderAfterSaleDto orderAfterSaleDto
     * @return Object
     */
    @PostMapping("/resolveOrderAfterSale")
    public Result<Object> resolveOrderAfterSale(OrderAfterSaleDto orderAfterSaleDto) {

        try {

            iOrderAfterSaleService.resolveOrderAfterSale(orderAfterSaleDto);

            return new ResultUtil<>().setSuccessMsg("处理退款成功！");
        }catch (RuanyunException e) {

            throw new RuanyunException(e.getMsg());
        }

    }


    /**
     * app通过订单id获取售后信息
     * @param orderId 订单id
     * @return Object
     */
    @PostMapping("/appOrderAfterSaleDetail")
    public Result<Object> appOrderAfterSaleDetail(String orderId) {
        return Optional.ofNullable(iOrderAfterSaleService.getByOrderId(orderId))
                .map(byid -> new ResultUtil<>().setData(iOrderAfterSaleService.getByOrderId(orderId),"操作成功！"))
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }



}
