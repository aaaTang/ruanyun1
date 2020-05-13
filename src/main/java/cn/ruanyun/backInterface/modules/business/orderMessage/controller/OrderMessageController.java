package cn.ruanyun.backInterface.modules.business.orderMessage.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;
import cn.ruanyun.backInterface.modules.business.orderMessage.mapper.OrderMessageMapper;
import cn.ruanyun.backInterface.modules.business.orderMessage.pojo.OrderMessage;
import cn.ruanyun.backInterface.modules.business.orderMessage.service.IOrderMessageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.api.client.util.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author z
 * 订单消息管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/orderMessage")
@Transactional
public class OrderMessageController {

    @Autowired
    private IOrderMessageService iOrderMessageService;
    @Resource
    private OrderMessageMapper orderMessageMapper;
    @Autowired
    private SecurityUtil securityUtil;
   /**
     * 更新或者插入数据
     * @param orderMessage
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateOrderMessage")
    public Result<Object> insertOrderUpdateOrderMessage(OrderMessage orderMessage){

        try {

            iOrderMessageService.insertOrderUpdateOrderMessage(orderMessage);
            return new ResultUtil<>().setSuccessMsg("插入或者更新成功!");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 修改成已读
     * @return
     */
    @PostMapping(value = "/updateOrderMessage")
    public void updateOrderMessage(){
        iOrderMessageService.list(new QueryWrapper<OrderMessage>().lambda().eq(OrderMessage::getCreateBy,securityUtil.getCurrUser().getId()))
                .forEach(messages ->{
                    messages.setStatus(1);
                    iOrderMessageService.saveOrUpdate(messages);
                });
    }


    /**
     * 移除数据
     * @param ids
     * @return
    */
    @PostMapping(value = "/removeOrderMessage")
    public Result<Object> removeOrderMessage(String ids){

        try {

            iOrderMessageService.removeOrderMessage(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 获取我的订单信息列表
     * @param pageVo
     * @return
     */
    @PostMapping("/getOrderMessage")
    public Result<Object> getOrderMessage( PageVo pageVo) {
        return Optional.ofNullable(iOrderMessageService.getOrderMessage())
                .map(orderMessage -> {
                    //修改成已读
                    iOrderMessageService.list(new QueryWrapper<OrderMessage>().lambda().eq(OrderMessage::getCreateBy,securityUtil.getCurrUser().getId()))
                            .forEach(messages ->{
                                messages.setStatus(1);
                                iOrderMessageService.saveOrUpdate(messages);
                            });
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",orderMessage.size());
                    result.put("data", PageUtil.listToPage(pageVo,orderMessage));
                    return new ResultUtil<>().setData(result,"获取我的订单信息成功！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }


    /**
     * 获取我的订单信息未读数量
     * @return
     */
    @PostMapping("/getOrderMessageNum")
    public Result<Object> getOrderMessageNum() {
        return Optional.ofNullable(orderMessageMapper.selectList(Wrappers.<OrderMessage>lambdaQuery()
                .eq(OrderMessage::getCreateBy,securityUtil.getCurrUser().getId())
                .eq(OrderMessage::getStatus,0)))
                .map(orderMessage -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",orderMessage.size());
                    return new ResultUtil<>().setData(result,"获取我的订单信息未读数量成功！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }

}
