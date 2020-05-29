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
import java.util.Optional;

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




}
