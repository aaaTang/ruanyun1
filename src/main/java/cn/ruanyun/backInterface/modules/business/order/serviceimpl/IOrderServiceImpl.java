package cn.ruanyun.backInterface.modules.business.order.serviceimpl;

import cn.hutool.json.JSONUtil;
import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
import cn.ruanyun.backInterface.common.pay.common.alipay.AliPayUtilTool;
import cn.ruanyun.backInterface.common.pay.common.alipay.AlipayConfig;
import cn.ruanyun.backInterface.common.pay.common.wxpay.WeChatConfig;
import cn.ruanyun.backInterface.common.pay.model.PayModel;
import cn.ruanyun.backInterface.common.pay.service.IPayService;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DictData;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.DictDataService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.business.balance.pojo.Balance;
import cn.ruanyun.backInterface.modules.business.balance.service.IBalanceService;
import cn.ruanyun.backInterface.modules.business.discountMy.VO.DiscountVO;
import cn.ruanyun.backInterface.modules.business.discountMy.service.IDiscountMyService;
import cn.ruanyun.backInterface.modules.business.good.VO.AppGoodOrderVO;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import cn.ruanyun.backInterface.modules.business.goodsPackage.pojo.GoodsPackage;
import cn.ruanyun.backInterface.modules.business.goodsPackage.service.IGoodsPackageService;
import cn.ruanyun.backInterface.modules.business.harvestAddress.entity.HarvestAddress;
import cn.ruanyun.backInterface.modules.business.harvestAddress.service.IHarvestAddressService;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.service.IItemAttrValService;
import cn.ruanyun.backInterface.modules.business.order.DTO.OrderDTO;
import cn.ruanyun.backInterface.modules.business.order.DTO.OrderShowDTO;
import cn.ruanyun.backInterface.modules.business.order.VO.GoodsPackageOrderVO;
import cn.ruanyun.backInterface.modules.business.order.VO.OrderDetailVO;
import cn.ruanyun.backInterface.modules.business.order.VO.OrderListVO;
import cn.ruanyun.backInterface.modules.business.order.VO.ShowOrderVO;
import cn.ruanyun.backInterface.modules.business.order.mapper.OrderMapper;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;
import cn.ruanyun.backInterface.modules.business.order.service.IOrderService;
import cn.ruanyun.backInterface.modules.business.orderDetail.VO.OrderDetailListVO;
import cn.ruanyun.backInterface.modules.business.orderDetail.mapper.OrderDetailMapper;
import cn.ruanyun.backInterface.modules.business.orderDetail.pojo.OrderDetail;
import cn.ruanyun.backInterface.modules.business.orderDetail.service.IOrderDetailService;
import cn.ruanyun.backInterface.modules.business.orderMessage.pojo.OrderMessage;
import cn.ruanyun.backInterface.modules.business.orderMessage.service.IOrderMessageService;
import cn.ruanyun.backInterface.modules.business.shoppingCart.entity.ShoppingCart;
import cn.ruanyun.backInterface.modules.business.shoppingCart.service.IShoppingCartService;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.mapper.SizeAndRolorMapper;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.pojo.SizeAndRolor;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.service.ISizeAndRolorService;
import cn.ruanyun.backInterface.modules.business.userRelationship.mapper.UserRelationshipMapper;
import cn.ruanyun.backInterface.modules.business.userRelationship.pojo.UserRelationship;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.api.client.util.ArrayMap;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.WxPayKit;
import dm.jdbc.stat.support.json.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


/**
 * 订单接口实现
 *
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IOrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {


    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private IHarvestAddressService harvestAddressService;
    @Autowired
    private IShoppingCartService shoppingCartService;
    @Autowired
    private IGoodService goodService;
    @Autowired
    private IOrderDetailService orderDetailService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IBalanceService iBalanceService;
    @Autowired
    private IDiscountMyService discountMyService;
    @Autowired
    private ISizeAndRolorService sizeAndRolorService;
    @Autowired
    private IGoodsPackageService goodsPackageService;
    @Autowired
    private IItemAttrValService iItemAttrValService;
    @Autowired
    private IPayService payService;
    @Autowired
    private IOrderMessageService iOrderMessageService;
    @Resource
    private OrderDetailMapper orderDetailMapper;


    @Override
    public Result<Object> insertOrderUpdateOrder(OrderDTO orderDTO) {
        //返回给前端的订单id 如果是多商铺的，就返回多个订单id
        String ids = "";
        //处理商品，如果商品不是同一个店铺下面的，需要生成过个订单
        //"[{"sizeId":"256005326325682176","buyCount":5,"colorId":"256004810652782592","goodId":"1243581862115827714"},{"sizeId":"256005326325682176","buyCount":5,"colorId":"256004810652782592","goodId":"1243581862115827714"}]"
        JSONArray jsonArray = new JSONArray(orderDTO.getGoods().toString());
        //处理商品是多商铺的情况
        Map<String, List<OrderDetail>> goodMap = this.settingStore(jsonArray);

        //下单的商品中存在多商品，生成多个订单
        Set<String> strings = goodMap.keySet();
        double sumPrice = 0d;
        for (String userId : strings) {
            Order order = new Order();
            order.setOrderStatus(OrderStatusEnum.PRE_PAY);
            order.setUserId(userId);
            double totalPrice = 0d;
            //下单的地址信息
            HarvestAddress harvestAddress = harvestAddressService.getById(orderDTO.getAddressId());
            harvestAddress.setId(null);
            order.setAddressId(orderDTO.getAddressId());
            ToolUtil.copyProperties(harvestAddress, order);
            //订单下面的商品
            List<OrderDetail> orderDetails = goodMap.get(userId);
            for (OrderDetail orderDetail : orderDetails) {
                orderDetail.setOrderId(order.getId());
                orderDetail.setCreateBy(null);
                orderDetailService.insertOrderUpdateOrderDetail(orderDetail);
                //将购物车数据删除
                ShoppingCart one = shoppingCartService.getOne(Wrappers.<ShoppingCart>lambdaQuery()
                        .eq(ShoppingCart::getGoodId, orderDetail.getGoodId())
                        .eq(ShoppingCart::getAttrSymbolPath, orderDetail.getAttrSymbolPath())
                        .eq(RuanyunBaseEntity::getCreateBy, orderDetail.getCreateBy())
                );
                if (EmptyUtil.isNotEmpty(one)) {
                    shoppingCartService.removeById(one.getId());
                }

                totalPrice = totalPrice + (orderDetail.getBuyCount() * orderDetail.getGoodNewPrice().doubleValue());
                if (orderDetail.getSubtractMoney().doubleValue() > 0) {
                    totalPrice = totalPrice - orderDetail.getSubtractMoney().doubleValue();
                }
            }
            order.setTotalPrice(new BigDecimal(totalPrice));
            if (ToolUtil.isEmpty(order.getCreateBy())) {
                order.setCreateBy(securityUtil.getCurrUser().getId());
            } else {
                order.setUpdateBy(securityUtil.getCurrUser().getId());
            }
            Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(order)))
                    .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                    .toFuture().join();
            ids += "," + order.getId();
            sumPrice += order.getTotalPrice();

        }
        Map<String, Object> map = new ArrayMap<>();
        map.put("id", ids.substring(1));
        map.put("balance", userService.getAccountBalance(null));
        map.put("totalPrice", sumPrice);
        return new ResultUtil<>().setData(map, "插入或者更新成功!");
    }

    /***
     * 判断商品是否是一个店铺下面的
     * @param jsonArray
     * @return
     */
    private Map<String, List<OrderDetail>> settingStore(JSONArray jsonArray) {
        Map<String, List<OrderDetail>> goodMap = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            OrderDetail orderDetail = JSON.parseObject(jsonArray.get(i).toString(), OrderDetail.class);

            //将商品是一个店铺的归类
            Good byId = goodService.getById(orderDetail.getGoodId());
            if (EmptyUtil.isNotEmpty(byId)) {
                byId.setId(null);
                ToolUtil.copyProperties(byId, orderDetail);
                List<String> strings = ToolUtil.splitterStr(byId.getGoodPics());
                orderDetail.setGoodPics(strings.get(0));
            }
            if (!StringUtils.isEmpty(orderDetail.getDiscountMyId())) {
                //处理商品优惠卷信息
                DiscountVO detailById = discountMyService.getDetailById(orderDetail.getDiscountMyId());
                if (EmptyUtil.isNotEmpty(detailById)) {
                    int orderDetailMoney = detailById.getFullMoney().compareTo(orderDetail.getGoodNewPrice().divide(new BigDecimal(orderDetail.getBuyCount())));
                    if (orderDetailMoney == -1) {
                        detailById.setId(null);
                        ToolUtil.copyProperties(detailById, orderDetail);
                        orderDetail.setDiscountMyId(detailById.getId());
                    }
                }

            }
            if (!StringUtils.isEmpty(orderDetail.getAttrSymbolPath())) {
                SizeAndRolor sizeAndRolor = sizeAndRolorService.getOneByAttrSymbolPath(orderDetail.getAttrSymbolPath());
                if (EmptyUtil.isNotEmpty(sizeAndRolor)) {
                    sizeAndRolor.setId(null);
                    orderDetail.setIntegral(sizeAndRolor.getInventory());
                    orderDetail.setGoodNewPrice(sizeAndRolor.getGoodPrice());
                    orderDetail.setGoodPics(sizeAndRolor.getPic());
                }
            }
            List<OrderDetail> goodList = goodMap.get(byId.getCreateBy());
            if (EmptyUtil.isEmpty(goodList)) {
                ArrayList<OrderDetail> orderDetails = new ArrayList<>();
                orderDetails.add(orderDetail);
                goodMap.put(byId.getCreateBy(), orderDetails);
            } else {
                goodList.add(orderDetail);
            }
        }
        return goodMap;
    }

    /**
     * 支付
     *
     * @param ids
     * @param payTypeEnum
     */
    @Override
    public Result<Object> payOrder(String ids, PayTypeEnum payTypeEnum) {
        //统计订单总金额
        BigDecimal totalPrice = new BigDecimal(0);
        List<Order> orders = this.listByIds(ToolUtil.splitterStr(ids));
        if (orders.size() == 0) {
            return new ResultUtil<>().setErrorMsg("该订单不存在!");
        }
        totalPrice = BigDecimal.valueOf(orders.stream().mapToDouble(Order::getTotalPrice).sum());

        Result<Object> objectResult = new ResultUtil<>().setData(200, "支付成功!");

        PayModel payModel = new PayModel();
        payModel.setOrderNums(ids);
        payModel.setTotalPrice(totalPrice);
        //微信
        if (payTypeEnum.getCode() == PayTypeEnum.WE_CHAT.getCode()) {
            objectResult = payService.wxPayMethod(payModel);
            //支付宝
        } else if (payTypeEnum.getCode() == PayTypeEnum.ALI_PAY.getCode()) {
            objectResult = payService.aliPayMethod(payModel);
            //余额支付
        } else if (payTypeEnum.getCode() == PayTypeEnum.BALANCE.getCode()) {
//            payService.accountMoney(orders.get(0));
            User byId = userService.getById(securityUtil.getCurrUser().getId());
            int i = byId.getBalance().compareTo(totalPrice);
            if (i < 0) {
                return new ResultUtil<>().setErrorMsg("余额不足!");
            }
            orders.forEach(order -> {
                order.setOrderStatus(OrderStatusEnum.PRE_SEND);
                order.setPayTypeEnum(payTypeEnum);
                this.updateById(order);

                // 生成账单流水
                Balance balance1 = new Balance();
                balance1.setTotalPrice(new BigDecimal(order.getTotalPrice()))
                        .setType(1)
                        .setStatus(2)
                        .setTableOid(ids)
                        .setTotalPrice(new BigDecimal(order.getTotalPrice()))
                        .setPayMoney(byId.getBalance())
                        .setSurplusMoney(byId.getBalance().subtract(new BigDecimal(order.getTotalPrice())));//支付后的余额
                iBalanceService.insertOrderUpdateBalance(balance1);//添加余额使用明细
            });
            objectResult = new ResultUtil<>().setData(200, "支付成功!");
        }
        return objectResult;
    }

    @Override
    public void removeOrder(String ids) {
        CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
    }

    @Override
    public GoodsPackageOrderVO showGoodsPackageOrder(OrderShowDTO orderShowDTO) {
        GoodsPackageOrderVO showOrderVO = new GoodsPackageOrderVO();
        GoodsPackage byId = goodsPackageService.getById(orderShowDTO.getGoodId());
        goodService.listByIds(ToolUtil.splitterStr(byId.getGoodIds()));

        return showOrderVO;
    }

    /***
     * 查询我的订单
     * @param order
     * @return
     */
    @Override
    public List<OrderListVO> getOrderList(Order order) {
        String id = securityUtil.getCurrUser().getId();
        List<Order> list = this.list(Wrappers.<Order>lambdaQuery()
                .eq(!StringUtils.isEmpty(order.getUserId()), Order::getUserId, order.getUserId())
                .eq(Order::getCreateBy, id)
                .eq(!EmptyUtil.isEmpty(order.getOrderStatus()), Order::getOrderStatus, order.getOrderStatus())
                .orderByDesc(Order::getCreateTime));
        return Optional.ofNullable(ToolUtil.setListToNul(list)).map(orders -> {
            List<OrderListVO> orderListVOS = orders.parallelStream().map(orderO -> {
                OrderListVO orderListVO = new OrderListVO();
                List<OrderDetail> orderDetailList = orderDetailService.list(Wrappers.<OrderDetail>lambdaQuery()
                        .eq(OrderDetail::getOrderId, orderO.getId()));
                if (orderDetailList.size() > 0) {
                    ToolUtil.copyProperties(orderDetailList.get(0), orderListVO);
                    if (ToolUtil.isNotEmpty(orderDetailList.get(0).getAttrSymbolPath())) {
                        orderListVO.setAttrSymbolPath(iItemAttrValService.getItemAttrVals(orderDetailList.get(0).getAttrSymbolPath()));
                    }
                }
                ToolUtil.copyProperties(orderO, orderListVO);
                orderListVO.setOrderStatusInt(orderO.getOrderStatus().getCode());
                orderListVO.setOrderStatus(orderO.getOrderStatus().getValue());
                return orderListVO;
            }).collect(Collectors.toList());
            return orderListVOS;
        }).orElse(null);
    }

    /**
     * 获取订单详情
     *
     * @param id
     * @return
     */
    @Override
    public OrderDetailVO getAppGoodDetail(String id) {
        return Optional.ofNullable(this.getById(id)).map(order -> {
            OrderDetailVO orderDetailVO = new OrderDetailVO();
            List<OrderDetailListVO> orderListByOrderId = orderDetailService.getOrderListByOrderId(id);
            orderDetailVO.setOrderDetails(orderListByOrderId);
            ToolUtil.copyProperties(order, orderDetailVO);
            if (EmptyUtil.isNotEmpty(order.getPayTypeEnum())) {
                orderDetailVO.setPayTypeEnum(order.getPayTypeEnum().getValue());
            }
            orderDetailVO.setOrderStatus(order.getOrderStatus().getCode() + "");
            return orderDetailVO;
        }).orElse(null);
    }

    /**
     * @param order
     * @return
     */
    @Override
    public Object changeStatus(Order order) {
        //确认发货 确认收货 评价
        Order byId = this.getById(order.getId());
        switch (order.getOrderStatus()) {
            //已付款
            case PRE_SEND:
                if (byId.getOrderStatus().equals(OrderStatusEnum.PRE_PAY)) {
                    return new ResultUtil<>().setErrorMsg(202, "该订单未生成");
                }
                byId.setPayTypeEnum(order.getPayTypeEnum());
                break;
            //待收货
            case DELIVER_SEND:
                if (!byId.getOrderStatus().equals(OrderStatusEnum.PRE_SEND)) {
                    return new ResultUtil<>().setErrorMsg(202, "该订单未支付");
                }
                byId.setExpressCode(order.getExpressCode());
                break;
            //待确定
            case PRE_COMMENT:
                if (!byId.getOrderStatus().equals(OrderStatusEnum.DELIVER_SEND)) {
                    return new ResultUtil<>().setErrorMsg(202, "该订单未发货");
                }
                break;

            //退款售后
            case SALE_AFTER:
                break;
            //取消订单
            case CANCEL_ORDER:

                break;
            //完成
            case IS_COMPLETE:
               /* if (!byId.getOrderStatus().equals(OrderStatusEnum.SALE_AFTER)){
                    return new ResultUtil<>().setErrorMsg(202,"该订单未支付");
                }*/
                break;
            default:
                break;
        }

        byId.setOrderStatus(order.getOrderStatus());
        this.updateById(byId);

        //添加订单消息
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setTitle(Optional.ofNullable(orderDetailMapper.selectOne(Wrappers.<OrderDetail>lambdaQuery().eq(OrderDetail::getOrderId, byId.getId()))).map(OrderDetail::getGoodName).orElse(null))
                .setPic(Optional.ofNullable(orderDetailMapper.selectOne(Wrappers.<OrderDetail>lambdaQuery().eq(OrderDetail::getOrderId, byId.getId()))).map(OrderDetail::getGoodPics).orElse(null))
                .setOrderNum(byId.getOrderNum())
                .setOrderStatus(byId.getOrderStatus().getValue())
                .setTime(new Date())
                .setCreateBy(byId.getCreateBy());
        iOrderMessageService.insertOrderUpdateOrderMessage(orderMessage);

        //分销
        if (byId.getOrderStatus().equals(OrderStatusEnum.DELIVER_SEND)) {
            this.distribution(byId);
        }

        return null;
    }


    /**
     * 下单展示
     *
     * @param orderShowDTO
     * @return
     */
    @Override
    public ShowOrderVO showOrder(OrderShowDTO orderShowDTO) {
        String id = securityUtil.getCurrUser().getId();
        ShowOrderVO showOrderVO = new ShowOrderVO();

        //查询地址信息
        showOrderVO.setHarvestAddressVO(harvestAddressService.getAddressDetail(orderShowDTO.getAddressId()));
        //获取商品的信息
        //直接下单
        List<AppGoodOrderVO> appGoodOrderVOS = new ArrayList<>();
        if (orderShowDTO.getType() == 1) {
            appGoodOrderVOS.add(this.getShowOrderVOOne(orderShowDTO));
            //购物车多选下单
        } else if (orderShowDTO.getType() == 2) {
            List<ShoppingCart> shoppingCarts = shoppingCartService.listByIds(ToolUtil.splitterStr(orderShowDTO.getShoppingCartIds()));
            for (ShoppingCart shoppingCart : shoppingCarts) {
                appGoodOrderVOS.add(this.getShowOrderVOShoppingCart(shoppingCart, id));
            }
        }
        showOrderVO.setAppGoodOrderVOS(appGoodOrderVOS);

        //可获得积分
        int integral = appGoodOrderVOS.stream().filter(appGoodOrderVO -> appGoodOrderVO.getIntegral() != null).mapToInt(AppGoodOrderVO::getIntegral).sum();
        showOrderVO.setIntegral(integral);

        showOrderVO.setSumMoney(this.getSumPrice(appGoodOrderVOS));

        return showOrderVO;

    }

    /**
     * 商品详情，直接下单
     *
     * @param orderShowDTO
     */
    private AppGoodOrderVO getShowOrderVOOne(OrderShowDTO orderShowDTO) {
        //查询商品信息
        AppGoodOrderVO appGoodOrder = goodService.getAppGoodOrder(orderShowDTO.getGoodId(), orderShowDTO.getAttrSymbolPath());
        //查询商品价格
        SizeAndRolor one = sizeAndRolorService.getOne(Wrappers.<SizeAndRolor>lambdaQuery()
                .eq(SizeAndRolor::getAttrSymbolPath, orderShowDTO.getAttrSymbolPath())
                .eq(SizeAndRolor::getGoodsId, orderShowDTO.getGoodId()));

        if (EmptyUtil.isNotEmpty(one)) {
            appGoodOrder.setGoodNewPrice(one.getGoodPrice()).setGoodPic(one.getPic()).setIntegral(one.getInventory());
        }
        appGoodOrder.setBuyCount(orderShowDTO.getCount());

        //处理属性配置
//        appGoodOrder.setItemAttrKeys(iItemAttrValService.getItemAttrVals(orderShowDTO.getAttrSymbolPath()));
        if (!StringUtils.isEmpty(orderShowDTO.getDiscountCouponId())) {
            //处理优惠券信息 订单的价格是否满足
            DiscountVO detailById = discountMyService.getDetailById(orderShowDTO.getDiscountCouponId());
            int i = detailById.getFullMoney().compareTo(new BigDecimal(appGoodOrder.getBuyCount()).multiply(appGoodOrder.getGoodNewPrice()));
            if (i == -1) {
                appGoodOrder.setDiscountMyId(detailById.getId());
                detailById.setId(null);
                ToolUtil.copyProperties(detailById, appGoodOrder);
            }
        }


        return appGoodOrder;
    }

    /**
     * 从购物车下单，获取商品信息
     *
     * @param shoppingCart
     */
    private AppGoodOrderVO getShowOrderVOShoppingCart(ShoppingCart shoppingCart, String userId) {
        AppGoodOrderVO appGoodOrderVO = new AppGoodOrderVO();
        //查询商品信息
        AppGoodOrderVO appGoodOrder = goodService.getAppGoodOrder(shoppingCart.getGoodId(), shoppingCart.getAttrSymbolPath());
        ToolUtil.copyProperties(appGoodOrder, appGoodOrderVO);
        //查询商品价格
        /*SizeAndRolor one = sizeAndRolorService.getOne(Wrappers.<SizeAndRolor>lambdaQuery()
                .eq(SizeAndRolor::getAttrSymbolPath, shoppingCart.getAttrSymbolPath())
                .eq(SizeAndRolor::getGoodsId, shoppingCart.getGoodId()));
        if (EmptyUtil.isNotEmpty(one)){
            appGoodOrderVO.setGoodNewPrice(one.getGoodPrice()).setGoodPic(one.getPic()).setIntegral(one.getInventory());
        }*/
        appGoodOrderVO.setBuyCount(shoppingCart.getCount());
        //获取这个人，这个商品 能用的优惠券
        String goodId = shoppingCart.getGoodId();
        BigDecimal multiply = appGoodOrderVO.getGoodNewPrice().multiply(new BigDecimal(appGoodOrderVO.getBuyCount()));
        List<DiscountVO> dealCanUseCoupon1 = discountMyService.getDealCanUseCoupon(userId, goodId, multiply);
        if (EmptyUtil.isNotEmpty(dealCanUseCoupon1)) {
            DiscountVO dealCanUseCoupon = dealCanUseCoupon1.get(0);
            if (EmptyUtil.isNotEmpty(dealCanUseCoupon)) {
                ToolUtil.copyProperties(dealCanUseCoupon, appGoodOrder);
                appGoodOrder.setDiscountMyId(dealCanUseCoupon.getId());
            }
        }

        return appGoodOrderVO;
    }

    /**
     * 计算订单总金额
     *
     * @param appGoodOrderVOS
     */
    private BigDecimal getSumPrice(List<AppGoodOrderVO> appGoodOrderVOS) {
        BigDecimal sumPrice = new BigDecimal(0);
        for (AppGoodOrderVO appGoodOrderVO : appGoodOrderVOS) {
            if (StringUtils.isEmpty(appGoodOrderVO.getDiscountMyId())) {
                sumPrice = sumPrice.add(appGoodOrderVO.getGoodNewPrice().multiply(new BigDecimal(Optional.ofNullable(appGoodOrderVO.getBuyCount()).orElse(1))));
            } else {
                sumPrice = sumPrice.add(appGoodOrderVO.getGoodNewPrice().divide(new BigDecimal(appGoodOrderVO.getBuyCount()))).subtract(appGoodOrderVO.getSubtractMoney());
            }
        }
        return sumPrice;
    }


    /**
     * 微信回调
     *
     * @param request
     * @return
     */
    @Override
    public String wxPayNotify(HttpServletRequest request) {
        String xmlMsg = HttpKit.readData(request);
        log.warn("微信支付回调消息参数=\n" + xmlMsg);
        Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);

        String returnCode = params.get("return_code");
        //获取创建时候的 商户订单号（唯一就行）
        String orderNo = params.get("out_trade_no");

        Map<String, String> xml;

        // 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
        List<Order> orders = this.listByIds(ToolUtil.splitterStr(orderNo));
        String panduan = panduan(orders);
        if (StringUtils.isEmpty(panduan)) return panduan;
        // 注意此处签名方式需与统一下单的签名类型一致
        if (WxPayKit.verifyNotify(params, WeChatConfig.KEY, SignType.HMACSHA256)) {
            if (WxPayKit.codeIsOk(returnCode)) {
                this.settingOrder(orders);
                // 发送通知等
                xml = new HashMap<String, String>(2);
                xml.put("return_code", "200");
                xml.put("return_msg", "回调成功");
                return WxPayKit.toXml(xml);
            }
        }
        return null;
    }

    /**
     * 支付宝回调
     *
     * @param request
     * @return
     */
    @Override
    public String aliPayNotify(HttpServletRequest request) {
        Map<String, String> params = AliPayUtilTool.toMap(request);
        log.warn("支付宝回调消息参数=\n" + JSONUtil.toJsonPrettyStr(params));
        JSONObject object = new JSONObject();
        try {
            //			boolean verifyResult = AlipaySignature.rsaCheckV1(params, "支付公钥", "UTF-8", "RSA2");
            boolean verifyResult = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, AlipayConfig.SIGN_TYPE);
            String orderTradeStatus = params.get("trade_status");//付款状态
            String out_trade_no = params.get("out_trade_no");
            String TRADE_SUCCESS = "TRADE_SUCCESS";
            if (orderTradeStatus.equals(TRADE_SUCCESS)) {
                // TODO 请在这里加上商户的业务逻辑程序代码 异步通知可能出现订单重复通知 需要做去重处理
                List<Order> orders = this.listByIds(ToolUtil.splitterStr(out_trade_no));
                String panduan = panduan(orders);
                if (!StringUtils.isEmpty(panduan)) return panduan;
                //处理订单状态
                for (int i = 0; i < orders.size(); i++) {
                    Order order = orders.get(i);
                    order.setOrderStatus(OrderStatusEnum.PRE_SEND);
                    this.updateById(order);
                }
                object.put("return_code", "200");
                object.put("return_msg", "支付宝notify_url 验证成功 succcess");
                return object.toString();
            } else {
                System.out.println("notify_url 验证失败");
                // TODO
                object.put("return_code", "-1");
                object.put("return_msg", "notify_url 验证失败，请重新下单");
                return object.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            object.put("return_code", "-1");
            object.put("return_msg", "请重新下单");
            return object.toString();
        }
    }


    /***
     * 支付宝、微信回调 处理订单
     */
    private String panduan(List<Order> orders) {
        Set<OrderStatusEnum> collect = orders.parallelStream().map(Order::getOrderStatus).collect(Collectors.toSet());
        if (collect.size() == 1 && !collect.contains(OrderStatusEnum.PRE_PAY)) {
            Map<String, String> xml = new HashMap<>(2);
            xml.put("return_code", "-1");
            xml.put("return_msg", "订单状态失效或已支付，请重新下单");
            return WxPayKit.toXml(xml);
        }
        return null;
    }

    /***
     * 支付宝、微信回调 处理订单
     */
    private void settingOrder(List<Order> orders) {
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            //更新订单信息
            order.setOrderStatus(OrderStatusEnum.PRE_SEND);
            this.updateById(order);
            //移除票劵信息
//        this.removeShopTicket(order);
        }

    }
    /*****************************************************后端模块开始*************************************************************/


    /**
     * 获取商家订单列表
     *//*
    private List PCgetShopOrderList(){

        //查询登录用户的权限
        String userRole = goodService.getRoleUserList(securityUtil.getCurrUser().getId());

        //判断权限是否为空
        if(ToolUtil.isNotEmpty(userRole)){

            return null;
        }else {

            if(userRole.equals(CommonConstant.ADMIN)){
                List<Order> orderList = this.list();





            }



        }









        return  null;
    }*/



    /*****************************************************后端模块結束*************************************************************/


    /*****************************************************分销模块开始*************************************************************/

    @Autowired
    private DictDataService dictDataService;
    @Resource
    private UserRelationshipMapper userRelationshipMapper;


    /**
     * 分销
     * byId  订单详情
     */
    private void distribution(Order byId) {

        BigDecimal totalPrice = new BigDecimal(byId.getTotalPrice());
        //首先查询平台和商家的分账比例（比如1:9）
        DictData dictData = dictDataService.get("270668348755283968");
        String[] value = dictData.getValue().split(":");//商家和平台比例
        //分账不为空
        if (ToolUtil.isNotEmpty(dictData)&&value.length==2) {
            //平台比例
            BigDecimal terraceRatio = new BigDecimal(value[0]);
            //商家比例
            BigDecimal shopRatio = new BigDecimal(value[1]);
            //总比例
            BigDecimal totalProportion = terraceRatio.add(shopRatio);


            //商家的钱
            BigDecimal shopTotalPrice = this.getPrice(totalPrice,totalProportion,shopRatio);
            //平台的钱
            BigDecimal terraceTotalPrice = this.getPrice(totalPrice,totalProportion,terraceRatio);
            //推荐用户的钱
            BigDecimal userRecommendTotalPrice = new BigDecimal(0);
            //普通用户的钱
            BigDecimal userTotalPrice = new BigDecimal(0);

            //查詢是否有邀請人
            UserRelationship userRelationship = userRelationshipMapper.selectOne(Wrappers.<UserRelationship>lambdaQuery()
                    .eq(UserRelationship::getCreateBy,byId.getCreateBy()));
            //判断邀请人是否为空
            if(ToolUtil.isNotEmpty(userRelationship)){

                //推荐人比例
                BigDecimal userRecommendRatio = new BigDecimal(0);
                //消费者比例
                BigDecimal userRatio = new BigDecimal(0);
              String roleName = goodService.getRoleUserList(userRelationship.getParentUserid());//查询邀请人的角色
                if(roleName.equals(CommonConstant.STORE)||roleName.equals(CommonConstant.STORE)){//邀请人是商家或者个人商家
                    DictData shopDictData = dictDataService.get("270760115399823360");
                    String[] shopvalue = shopDictData.getValue().split(":");//二级分账(平台，推荐人(商家)，消费者)
                    if(shopvalue.length==3){
                        //平台比例
                        terraceRatio=new BigDecimal(shopvalue[0]);
                        //商家比例
                        shopRatio=new BigDecimal(shopvalue[1]);
                        //用户比例
                        userRatio=new BigDecimal(shopvalue[2]);
                        //总比例
                        totalProportion= terraceRatio.add(shopRatio.add(userRatio));

                        //商家推荐的钱
                        shopTotalPrice = shopTotalPrice.add(this.getPrice(terraceTotalPrice,totalProportion,shopRatio));
                        //用户返还的钱
                        userTotalPrice=this.getPrice(terraceTotalPrice,totalProportion,userRatio);
                        //平台重新分配的钱
                        terraceTotalPrice=this.getPrice(terraceTotalPrice,totalProportion,terraceRatio);
                    }
                }else if(roleName.equals(CommonConstant.DEFAULT_ROLE)){//邀请人是普通用户
                    DictData userDictData = dictDataService.get("270760077613338624");
                    String[] uservalue = userDictData.getValue().split(":");//二级分账(平台，推荐人(消费者)，消费者)
                    if(uservalue.length==3){
                        //平台比例
                        terraceRatio=new BigDecimal(uservalue[0]);
                        //消费者比例
                        userRecommendRatio=new BigDecimal(uservalue[1]);
                        //用户比例
                        userRatio=new BigDecimal(uservalue[2]);
                        //总比例
                        totalProportion= terraceRatio.add(userRecommendRatio.add(userRatio));

                        //消费者推荐的钱
                        userRecommendTotalPrice=this.getPrice(terraceTotalPrice,totalProportion,userRecommendRatio);
                        //用户返还的钱
                        userTotalPrice=this.getPrice(terraceTotalPrice,totalProportion,userRatio);
                        //平台重新分配的钱
                        terraceTotalPrice=this.getPrice(terraceTotalPrice,totalProportion,terraceRatio);
                    }
                }

                this.transferAccounts(shopTotalPrice,terraceTotalPrice,userRecommendTotalPrice,userTotalPrice,byId.getPayTypeEnum());

                System.out.println(shopTotalPrice+"商家的钱");
                System.out.println(terraceTotalPrice+"平台的钱");
                System.out.println(userRecommendTotalPrice+"推荐用户的钱");
                System.out.println(userTotalPrice+"普通用户的钱");

            }else {//邀请人是空，那就把钱分给平台是商家

                this.transferAccounts(shopTotalPrice,terraceTotalPrice,userRecommendTotalPrice,userTotalPrice,byId.getPayTypeEnum());
                //TODO::
                System.out.println(shopTotalPrice+"商家的钱");
                System.out.println(terraceTotalPrice+"平台的钱");
                System.out.println(userRecommendTotalPrice+"推荐用户的钱");
                System.out.println(userTotalPrice+"普通用户的钱");
            }

        }

    }



    /**
     * 计算金额
     * @param totalPrice 总价格
     * @param totalProportion 总比例
     * @param ratio 按比例计算金额
     * @return
     */
    private BigDecimal getPrice(BigDecimal totalPrice,BigDecimal totalProportion ,BigDecimal ratio){
        BigDecimal money = new BigDecimal(0);
            money = (totalPrice.divide (totalProportion)).multiply(ratio);
        return money;
    }

    /**
     * 订单转账
     * @param shopTotalPrice 商家的钱
     * @param terraceTotalPrice 平台的钱
     * @param userRecommendTotalPrice 推荐用户的钱
     * @param userTotalPrice 普通用户的钱
     * @param payTypeEnum 支付类型    WE_CHAT(1,"微信支付"),ALI_PAY(2,"支付宝支付"), BALANCE(3, "余额支付")
     * @return
     */
    private String transferAccounts(BigDecimal shopTotalPrice,BigDecimal terraceTotalPrice,BigDecimal userRecommendTotalPrice,BigDecimal userTotalPrice,PayTypeEnum payTypeEnum){

        if(payTypeEnum.equals(PayTypeEnum.BALANCE)){
            //TODO::余额转账
        }else if(payTypeEnum.equals(PayTypeEnum.ALI_PAY)){
            //TODO::支付宝转账
        }else if(payTypeEnum.equals(PayTypeEnum.WE_CHAT)){
            //TODO::微信转账
        }
        return null;
    }


/*****************************************************分销模块结束*************************************************************/







}