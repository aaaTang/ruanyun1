package cn.ruanyun.backInterface.modules.business.bookingOrder.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.bookingOrder.pojo.BookingOrder;
import cn.ruanyun.backInterface.modules.business.bookingOrder.service.IBookingOrderService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

/**
 * @author fei
 * 预约订单管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/bookingOrder")
@Transactional
public class BookingOrderController {

    @Autowired
    private IBookingOrderService ibookingOrderService;


   /**
     * 更新或者插入数据
     * @param bookingOrder
     * @return
    */
    @PostMapping(value = "/insertOrderUpdatebookingOrder")
    public Result<Object> insertOrderUpdatebookingOrder(BookingOrder bookingOrder){


            return  ibookingOrderService.insertOrderUpdatebookingOrder(bookingOrder);

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


    /**
     * 获取预约订单列表
     * @param pageVo
     * @return
     */
    @PostMapping(value = "/bookingOrderList")
    public Result<Object> bookingOrderList(PageVo pageVo,String classId){

        return Optional.ofNullable(ibookingOrderService.bookingOrderList(classId))
                .map(book->{
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",book.size());
                    result.put("data", PageUtil.listToPage(pageVo,book));

                    return new ResultUtil<>().setData(result,"获取预约订单列表成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据"));
    }


}
