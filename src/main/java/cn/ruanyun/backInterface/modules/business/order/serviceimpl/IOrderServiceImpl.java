package cn.ruanyun.backInterface.modules.business.order.serviceimpl;


import cn.ruanyun.backInterface.common.constant.WxProgramPayConstant;
import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.common.wxpay.PaymentApi;
import cn.ruanyun.backInterface.modules.base.pojo.Message;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.MessageService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.business.color.service.IcolorService;
import cn.ruanyun.backInterface.modules.business.comment.entity.Comment;
import cn.ruanyun.backInterface.modules.business.comment.service.ICommentService;
import cn.ruanyun.backInterface.modules.business.discountCoupon.service.IDiscountCouponService;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import cn.ruanyun.backInterface.modules.business.harvestAddress.service.IHarvestAddressService;
import cn.ruanyun.backInterface.modules.business.order.DTO.OrderDTO;
import cn.ruanyun.backInterface.modules.business.order.VO.BackOrderListVO;
import cn.ruanyun.backInterface.modules.business.order.VO.OrderDetailVO;
import cn.ruanyun.backInterface.modules.business.order.VO.OrderListVO;
import cn.ruanyun.backInterface.modules.business.order.entity.Order;
import cn.ruanyun.backInterface.modules.business.order.mapper.OrderMapper;
import cn.ruanyun.backInterface.modules.business.order.service.IOrderService;
import cn.ruanyun.backInterface.modules.business.shoppingCart.entity.ShoppingCart;
import cn.ruanyun.backInterface.modules.business.shoppingCart.service.IShoppingCartService;
import cn.ruanyun.backInterface.modules.business.size.service.IsizeService;
import cn.ruanyun.backInterface.modules.weChat.service.IweChatService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 订单接口实现
 * @author fei
 */
@Slf4j
@Service
/*@Transactional(rollbackFor = Exception.class)*/
public class IOrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private IGoodService goodService;

    @Autowired
    private IcolorService colorService;

    @Autowired
    private IsizeService sizeService;

    @Autowired
    private IHarvestAddressService harvestAddressService;

    @Autowired
    private IDiscountCouponService discountCouponService;

    @Autowired
    private IShoppingCartService shoppingCartService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private IweChatService weChatService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IUserService userService;



    @Override
    public Order insertOrder(Order order) {

        String currentUser = ToolUtil.isEmpty(order.getCreateBy()) ? securityUtil.getCurrUser().getId()
                : order.getCreateBy();

        return CompletableFuture.supplyAsync(() -> {

            order.setCreateBy(currentUser);
            this.save(order);

            return order;
        }).join();
    }

    /**
     * 发货
     *
     * @param orderDTO
     */
    @Override
    public void sendGood(OrderDTO orderDTO) {

        String currentUser = securityUtil.getCurrUser().getId();

        CompletableFuture.runAsync(() -> Optional.ofNullable(this.getById(orderDTO.getId()))
                .ifPresent(order -> {

                    order.setCourierNumber(orderDTO.getCourierNumber())
                            .setOrderStatus(OrderStatusEnum.PRE_RECEIVING)
                            .setUpdateBy(currentUser);
                    this.updateById(order);
                }));
    }

    @Override
    public void confirmOrder(String id) {

        CompletableFuture.runAsync(() -> Optional.ofNullable(this.getById(id)).ifPresent(order -> {

            order.setOrderStatus(OrderStatusEnum.PRE_COMMENT);
            this.updateById(order);
        }));
    }

    /**
     * 评价订单
     *
     * @param comment
     */
    @Override
    public void addComment(Comment comment) {

        Optional.ofNullable(this.getById(comment.getOrderId())).ifPresent(order -> {

            String currentUser = securityUtil.getCurrUser().getId();
            CompletableFuture.allOf(

                    //更新订单状态
                    CompletableFuture.runAsync(() -> {

                                order.setOrderStatus(OrderStatusEnum.IS_COMPLETE)
                                        .setUpdateBy(currentUser);
                                this.updateById(order);

                            }),
                    //插入评论
                    CompletableFuture.runAsync(() -> commentService.insertComment(comment))

            ).join();
        });

    }


    /**
     * app获取我的订单列表
     *
     * @param orderDTO
     * @return
     */
    @Override
    public List<OrderListVO> myOrderList(OrderDTO orderDTO) {

        String currentUser = securityUtil.getCurrUser().getId();

        //1.基础数据
        CompletableFuture<Optional<List<Order>>> orderList = CompletableFuture.supplyAsync(() -> {
            orderDTO.setBack(false);
            return Optional.ofNullable(getOrderList(orderDTO,currentUser));
        });

        //2.封装数据
        CompletableFuture<List<OrderListVO>> orderVOList = orderList.thenApplyAsync(orders ->
                orders.map(orders1 -> orders1.parallelStream().flatMap(order ->
                        Stream.of(getOrderListVO(order.getId()))).collect(Collectors.toList()))
                        .orElse(null));

        return orderVOList.join();
    }

    /**
     * 后台获取订单列表
     *
     * @param orderDTO
     * @return
     */
    @Override
    public List<BackOrderListVO> getBackOrderList(OrderDTO orderDTO) {

        String currentUser = securityUtil.getCurrUser().getId();

        //1.基础数据
        CompletableFuture<Optional<List<Order>>> orderList = CompletableFuture.supplyAsync(() -> {
            orderDTO.setBack(true);
            return Optional.ofNullable(getOrderList(orderDTO,currentUser));
        });

        //2.封装数据
        CompletableFuture<List<BackOrderListVO>> backOrderVOList = orderList.thenApplyAsync(orders ->
                orders.map(orders1 -> orders1.parallelStream().flatMap(order ->
                        Stream.of(getBackOrderListVO(order.getId()))).collect(Collectors.toList()))
                        .orElse(null));

        return backOrderVOList.join();
    }


    /**
     * 获取订单数据列表
     * @param orderDTO
     * @param userId
     * @return
     */
    public List<Order> getOrderList(OrderDTO orderDTO,String userId) {


        //筛选条件
        Map<SFunction<Order,?>, Object > param  = Maps.newHashMap();
        param.put(Order::getOrderType,orderDTO.getOrderType());
        param.put(Order::getOrderStatus,orderDTO.getOrderStatus());

        //默认条件构造器
        LambdaQueryWrapper<Order> orderWrappers = Wrappers.<Order>lambdaQuery()
                .allEq(param,false)
                .orderByDesc(Order::getCreateTime);


        //1.后台
        if (!orderDTO.getBack()) {

            orderWrappers.eq(Order::getCreateBy,userId);
        }

        return ToolUtil.setListToNul(this.list(orderWrappers));
    }


    /**
     * 获取订单列表字段
     *
     * @param id
     * @return
     */
    @Override
    public OrderListVO getOrderListVO(String id) {

        return Optional.ofNullable(this.getById(id)).map(order -> {

            OrderListVO orderListVO = new OrderListVO();
            ToolUtil.copyProperties(order,orderListVO);

            orderListVO.setName(goodService.getGoodName(order.getGoodId()))
                    .setColorName(colorService.getColorName(order.getColorId()))
                    .setSizeName(sizeService.getSizeName(order.getSizeId()))
                    .setPic(goodService.getPicLimit1(order.getGoodId()));

            return orderListVO;
        }).orElse(null);
    }

    /**
     * 获取订单详情字段
     *
     * @param id
     * @return
     */
    @Override
    public OrderDetailVO getOrderDetailVO(String id) {
        return Optional.ofNullable(this.getById(id)).map(order -> {

            OrderDetailVO orderDetailVO = new OrderDetailVO();

            ToolUtil.copyProperties(order,orderDetailVO);
            ToolUtil.copyProperties(getOrderListVO(id),orderDetailVO);

            return orderDetailVO;

        }).orElse(null);
    }

    /**
     * 获取后台管理系统订单列表字段
     *
     * @param id
     * @return
     */
    @Override
    public BackOrderListVO getBackOrderListVO(String id) {

        return Optional.ofNullable(this.getById(id)).map(order -> {

            BackOrderListVO backOrderListVO = new BackOrderListVO();
            ToolUtil.copyProperties(getOrderListVO(id),backOrderListVO);
            ToolUtil.copyProperties(harvestAddressService.getAddressDetail(order.getAddressId()),backOrderListVO);
            ToolUtil.copyProperties(order,backOrderListVO);

            return backOrderListVO;
        }).orElse(null);
    }

    @Override
    public Integer getGoodSalesVolume(String goodId) {

        return this.count(Wrappers.<Order>lambdaQuery()
            .eq(Order::getGoodId,goodId)
            .eq(Order::getOrderStatus,OrderStatusEnum.PRE_SEND));
    }

    @Override
    public BigDecimal getGoodTotalMoney(Order order) {

       return null;
    }

    @Override
    public Map<String, String> wxAppletPayOrder(Order order, String ids) {


        Map<String,Object> orderParam = getPayInfo(order,ids);

        Map<String, String> reqParams = new HashMap<>(12);
        //微信分配的小程序ID
        reqParams.put("appid", WxProgramPayConstant.APPID);
        //微信支付分配的商户号
        reqParams.put("mch_id", WxProgramPayConstant.MCH_ID);
        //随机字符串
        reqParams.put("nonce_str", System.currentTimeMillis() / 1000 + "");
        //签名类型
        reqParams.put("sign_type", "MD5");
        //充值订单 商品描述
        reqParams.put("body", "商品购买:" + goodService.getGoodName(order.getGoodId()) + "-微信小程序");

        //商户订单号
        reqParams.put("out_trade_no", orderParam.get("orderNum").toString());
        //订单总金额，单位为分
        reqParams.put("total_fee", new BigDecimal(orderParam.get("totalMoney").toString())
                .multiply(BigDecimal.valueOf(100)).intValue() + "");
        //终端IP
        reqParams.put("spbill_create_ip", "127.0.0.1");
        //通知地址
        reqParams.put("notify_url", WxProgramPayConstant.NOTIFY_URL);
        //交易类型
        reqParams.put("trade_type", "JSAPI");
        //用户标识
        reqParams.put("openid", weChatService.getOpenId());
        //签名
        String sign = PaymentKit.createSign(reqParams, WxProgramPayConstant.KEY);
        reqParams.put("sign", sign);
        /*
            调用支付定义下单API,返回预付单信息 prepay_id
         */
        String xmlResult = PaymentApi.pushOrder(reqParams);
        log.info(xmlResult);
        Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
        //预付单信息
        String prepayId = result.get("prepay_id");

        /*
            小程序调起支付数据签名
         */
        Map<String, String> packageParams = new HashMap<>(6);
        packageParams.put("appId", WxProgramPayConstant.APPID);
        packageParams.put("timeStamp", System.currentTimeMillis() / 1000 + "");
        packageParams.put("nonceStr", System.currentTimeMillis() + "");
        packageParams.put("package", "prepay_id=" + prepayId);
        packageParams.put("signType", "MD5");
        String packageSign = PaymentKit.createSign(packageParams, WxProgramPayConstant.KEY);
        packageParams.put("paySign", packageSign);
        return packageParams;
    }


    @Override
    public void orderNotify(String orderNumStr) {

        String currentUser = securityUtil.getCurrUser().getId();

        //统一按照多订单处理
        List<String> orderNums = ToolUtil.splitterStr(orderNumStr);

        Optional.ofNullable(ToolUtil.setListToNul(orderNums)).ifPresent(orderNumList ->
                orderNumList.parallelStream().forEach(orderNum -> {

                    Order order = this.getOne(Wrappers.<Order>lambdaQuery()
                    .eq(Order::getOrderNum,orderNum));

                    CompletableFuture.allOf(

                            //1.更新订单状态
                            CompletableFuture.runAsync(() -> Optional.ofNullable(order).ifPresent(orderUpdate -> {
                                //改变状态
                                orderUpdate.setOrderStatus(OrderStatusEnum.PRE_SEND)
                                        .setUpdateBy(currentUser);
                                this.updateById(orderUpdate);

                                log.info("支付订单成功,订单编号为:{}",orderUpdate.getOrderNum());

                                //发送消息
                                Message messageUpdate = new Message();
                                messageUpdate.setTitle("订单消息").setContent("您有一个待发货订单消息,订单编号为"
                                        + orderUpdate.getOrderNum());
                                messageService.sendMessage(messageUpdate);

                            })),

                            //2.更新库存信息
                            CompletableFuture.runAsync(() -> Optional.ofNullable(order).ifPresent(inventoryUpdate -> {

                                Good good = goodService.getById(inventoryUpdate.getGoodId());
                                good.setInventory(good.getInventory() - 1);
                                goodService.insertOrderUpdateGood(good);

                                log.info("更新库存成功！,当前库存为:{}",good.getInventory());

                            }))


                    ).join();
                }));
    }


    /**
     * 获取订单信息(1.订单编号，2.总金额)
     * @param order
     * @param ids
     * @return
     */
    private Map<String,Object>  getPayInfo(Order order, String ids) {

        Map<String,Object> result = Maps.newHashMap();


       if (ToolUtil.isEmpty(order.getId()) && ToolUtil.isEmpty(ids)) {

           //1.商城直接下单
           Order mallOrder = insertOrder(order);
           result.put("orderNum",mallOrder.getOrderNum());
           result.put("totalMoney",mallOrder.getTotalPrice());
           return result;

       }else if (ToolUtil.isNotEmpty(order.getId()) && ToolUtil.isEmpty(ids)) {

           //2.在待支付订单下单
           Order prePayOrder = this.getById(order.getId());
           result.put("orderNum",prePayOrder.getOrderNum());
           result.put("totalMoney",prePayOrder.getTotalPrice());
           return result;

       }else if (ToolUtil.isNotEmpty(ids) && ToolUtil.isEmpty(order.getId())) {

           //3.在购物车下单
           List<String> shoppingCartIds = ToolUtil.splitterStr(ids);

           //3.1先获取所有购物车数据
           List<ShoppingCart> shoppingCarts = shoppingCartService.listByIds(shoppingCartIds);

           //3.2先插入待支付订单,然后删除购物车
           List<Order> orders = shoppingCarts.stream().flatMap(shoppingCart -> {

              Order orderInsert = new Order();

              ToolUtil.copyProperties(order,orderInsert);
              ToolUtil.copyProperties(shoppingCart,orderInsert);
              orderInsert.setId(null);
              orderInsert.setCreateBy(securityUtil.getCurrUser().getId());

              shoppingCartService.removeShoppingCart(shoppingCart.getId());

              if (this.save(orderInsert)) {
                  return Stream.of(this.getOne(Wrappers.<Order>lambdaQuery()
                      .orderByDesc(Order::getCreateTime).last("limit 1")));
              }
              return null;
          }).collect(Collectors.toList());

           //统计数据
           String orderNum = orders.parallelStream().map(Order::getId)
                   .collect(Collectors.joining(","));
           BigDecimal totalPrice = orders.parallelStream().map(Order::getTotalPrice)
                   .reduce(BigDecimal.ZERO,BigDecimal::add);

           result.put("orderNum", orderNum);
           result.put("totalMoney", totalPrice);
           return result;
       }
       return result;
    }
}