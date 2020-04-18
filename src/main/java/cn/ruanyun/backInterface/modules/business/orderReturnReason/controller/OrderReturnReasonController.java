package cn.ruanyun.backInterface.modules.business.orderReturnReason.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.orderReturnReason.pojo.OrderReturnReason;
import cn.ruanyun.backInterface.modules.business.orderReturnReason.service.IOrderReturnReasonService;
import com.google.api.client.util.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

/**
 * @author wj
 * 退货原因管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/orderReturnReason")
@Transactional
public class OrderReturnReasonController {

    @Autowired
    private IOrderReturnReasonService iOrderReturnReasonService;


   /**
     * 更新或者插入数据
     * @param orderReturnReason
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateorderReturnReason")
    public Result<Object> insertOrderUpdateorderReturnReason(OrderReturnReason orderReturnReason){
        try {
            iOrderReturnReasonService.insertOrderUpdateOrderReturnReason(orderReturnReason);
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
    @PostMapping(value = "/remOveorderReturnReason")
    public Result<Object> removeOrderReturnReason(String ids){
        try {
            iOrderReturnReasonService.removeOrderReturnReason(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

    /**
     *APP获取退款原因
     * @param orderReturnReason
     * @return
     */
    @PostMapping("/appOrderReturnReasonList")
    public Result<Object> getOrderReturnReasonList(OrderReturnReason orderReturnReason) {
        return Optional.ofNullable(iOrderReturnReasonService.getOrderReturnReasonList(orderReturnReason))
                .map(orderReturnReasons -> new ResultUtil<>().setData(orderReturnReasons,"操作成功！")
                ).orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }

    /**
     *后台获取退款原因
     * @param orderReturnReason
     * @param pageVo
     * @return
     */
    @PostMapping("/backstageOrderReturnReasonList")
    public Result<Object> backstageOrderReturnReasonList(OrderReturnReason orderReturnReason, PageVo pageVo) {
        return Optional.ofNullable(iOrderReturnReasonService.getOrderReturnReasonList(orderReturnReason))
                .map(orderReturnReasonListVOS -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",orderReturnReasonListVOS.size());
                    result.put("data", PageUtil.listToPage(pageVo,orderReturnReasonListVOS));
                    return new ResultUtil<>().setData(result,"获取订单列表成功！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }

}
