package cn.ruanyun.backInterface.modules.business.orderAfterSale.controller;

import cn.ruanyun.backInterface.common.exception.RuanyunException;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;
import cn.ruanyun.backInterface.modules.business.orderAfterSale.dto.OrderAfterCommitDto;
import cn.ruanyun.backInterface.modules.business.orderAfterSale.dto.OrderAfterSaleDto;
import cn.ruanyun.backInterface.modules.business.orderAfterSale.pojo.OrderAfterSale;
import cn.ruanyun.backInterface.modules.business.orderAfterSale.service.IOrderAfterSaleService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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


    @PostMapping("/commitOrderAfterSale")
    @ApiOperation("提交售后")
    public Result<Object> commitOrderAfterSale(OrderAfterCommitDto orderAfterCommitDto) {

        if (ToolUtil.isNotEmpty(iOrderAfterSaleService.getOne(Wrappers.<OrderAfterSale>lambdaQuery()
                .eq(OrderAfterSale::getOrderId, orderAfterCommitDto.getOrderId())))) {

            return new ResultUtil<>().setErrorMsg(208, "已经提交过售后，不可重复提交！");
        }

        iOrderAfterSaleService.commitOrderAfterSale(orderAfterCommitDto);
        return new ResultUtil<>().setSuccessMsg("添加售后成功！");
    }


    @PostMapping("/resolveOrderAfterSale")
    @ApiOperation("商家处理售后订单(&&管理员强制退款")
    public Result<Object> resolveOrderAfterSale(OrderAfterSaleDto orderAfterSaleDto) {

        try {

            iOrderAfterSaleService.resolveOrderAfterSale(orderAfterSaleDto);

            return new ResultUtil<>().setSuccessMsg("处理退款成功！");
        }catch (RuanyunException e) {

            throw new RuanyunException(e.getMsg());
        }

    }


    @PostMapping("/revocationAfterOrder")
    @ApiOperation("撤销售后")
    public Result<Object> revocationAfterOrder(String orderId) {

        return iOrderAfterSaleService.revocationAfterOrder(orderId);
    }

    @PostMapping("/appOrderAfterSaleDetail")
    @ApiOperation("app通过订单id获取售后信息")
    public Result<Object> appOrderAfterSaleDetail(String orderId) {
        return Optional.ofNullable(iOrderAfterSaleService.getByOrderId(orderId))
                .map(orderAfterSaleVo -> new ResultUtil<>().setData(orderAfterSaleVo,"操作成功！"))
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }



}
