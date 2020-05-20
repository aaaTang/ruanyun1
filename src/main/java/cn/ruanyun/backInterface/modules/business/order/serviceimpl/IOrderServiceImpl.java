package cn.ruanyun.backInterface.modules.business.order.serviceimpl;

import cn.hutool.json.JSONUtil;
import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.*;
import cn.ruanyun.backInterface.common.pay.common.alipay.AliPayUtilTool;
import cn.ruanyun.backInterface.common.pay.common.alipay.AlipayConfig;
import cn.ruanyun.backInterface.common.pay.common.wxpay.WeChatConfig;
import cn.ruanyun.backInterface.common.pay.dto.TransferDto;
import cn.ruanyun.backInterface.common.pay.model.PayModel;
import cn.ruanyun.backInterface.common.pay.service.IPayService;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DictData;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.DictDataService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.business.area.VO.AppAreaListVO;
import cn.ruanyun.backInterface.modules.business.area.pojo.Area;
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
import cn.ruanyun.backInterface.modules.business.order.DTO.OffLineOrderDto;
import cn.ruanyun.backInterface.modules.business.order.DTO.OrderDTO;
import cn.ruanyun.backInterface.modules.business.order.DTO.OrderShowDTO;
import cn.ruanyun.backInterface.modules.business.order.DTO.PcOrderDTO;
import cn.ruanyun.backInterface.modules.business.order.VO.*;
import cn.ruanyun.backInterface.modules.business.order.mapper.OrderMapper;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;
import cn.ruanyun.backInterface.modules.business.order.service.IOrderService;
import cn.ruanyun.backInterface.modules.business.orderDetail.VO.OrderDetailListVO;
import cn.ruanyun.backInterface.modules.business.orderDetail.mapper.OrderDetailMapper;
import cn.ruanyun.backInterface.modules.business.orderDetail.pojo.OrderDetail;
import cn.ruanyun.backInterface.modules.business.orderDetail.service.IOrderDetailService;
import cn.ruanyun.backInterface.modules.business.orderMessage.pojo.OrderMessage;
import cn.ruanyun.backInterface.modules.business.orderMessage.service.IOrderMessageService;
import cn.ruanyun.backInterface.modules.business.profitDetail.pojo.ProfitDetail;
import cn.ruanyun.backInterface.modules.business.profitDetail.service.IProfitDetailService;
import cn.ruanyun.backInterface.modules.business.profitPercent.service.IProfitPercentService;
import cn.ruanyun.backInterface.modules.business.shoppingCart.entity.ShoppingCart;
import cn.ruanyun.backInterface.modules.business.shoppingCart.service.IShoppingCartService;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.mapper.SizeAndRolorMapper;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.pojo.SizeAndRolor;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.service.ISizeAndRolorService;
import cn.ruanyun.backInterface.modules.business.staffManagement.pojo.StaffManagement;
import cn.ruanyun.backInterface.modules.business.staffManagement.service.IStaffManagementService;
import cn.ruanyun.backInterface.modules.business.storeIncome.pojo.StoreIncome;
import cn.ruanyun.backInterface.modules.business.storeIncome.service.IStoreIncomeService;
import cn.ruanyun.backInterface.modules.business.userRelationship.mapper.UserRelationshipMapper;
import cn.ruanyun.backInterface.modules.business.userRelationship.pojo.UserRelationship;
import cn.ruanyun.backInterface.modules.business.userRelationship.service.IUserRelationshipService;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
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
    @Autowired
    private IUserRelationshipService userRelationshipService;
    @Autowired
    private IBalanceService balanceService;
    @Autowired
    private IProfitPercentService profitPercentService;
    @Autowired
    private ISizeAndRolorService iSizeAndRolorService;

    @Autowired
    private IProfitDetailService profitDetailService;

    @Autowired
    private IStoreIncomeService storeIncomeService;

    @Autowired
    private IStaffManagementService staffManagementService;


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
            sumPrice += order.getTotalPrice().doubleValue();

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
                SizeAndRolor sizeAndRolor = sizeAndRolorService.getOneByAttrSymbolPath(orderDetail.getAttrSymbolPath(),orderDetail.getCreateBy());
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
    public Result<Object> payOrder(String ids, PayTypeEnum payTypeEnum, String payPassword) {

        //更改订单支付状态
        Optional.ofNullable(ToolUtil.setListToNul(ToolUtil.splitterStr(ids)))
                .ifPresent(orderIds -> orderIds.parallelStream().forEach(orderId -> {

                    Optional.ofNullable(this.getById(orderId)).ifPresent(order -> {

                        order.setPayTypeEnum(payTypeEnum);
                        this.updateById(order);
                    });
                }));


        //统计订单总金额
        BigDecimal totalPrice;
        List<Order> orders = this.listByIds(ToolUtil.splitterStr(ids));
        if (orders.size() == 0) {

            return new ResultUtil<>().setErrorMsg("该订单不存在!");
        }
        totalPrice = orders.parallelStream().map(Order::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

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

            User byId = userService.getById(securityUtil.getCurrUser().getId());

            if (ToolUtil.isEmpty(byId)) {

                return new ResultUtil<>().setErrorMsg(206, "暂未设置支付密码");
            }
            if (new BCryptPasswordEncoder().matches(payPassword, byId.getPayPassword())) {

                int i = byId.getBalance().compareTo(totalPrice);
                if (i < 0) {

                    return new ResultUtil<>().setErrorMsg(207, "余额不足");
                }
                orders.forEach(order -> {

                    //1.改变订单状态
                    order.setOrderStatus(OrderStatusEnum.PRE_SEND);
                    order.setPayTypeEnum(payTypeEnum);
                    this.updateById(order);

                    //2.减少用户余额,记录明细
                    Optional.ofNullable(userService.getById(order.getCreateBy()))
                            .ifPresent(user -> {

                                user.setBalance(user.getBalance().subtract(order.getTotalPrice()));
                                userService.updateById(user);

                                //生成余额明细
                                Balance balance = new Balance();
                                balance.setTitle("购买商品")
                                        .setAddOrSubtractTypeEnum(AddOrSubtractTypeEnum.SUB)
                                        .setPrice(order.getTotalPrice())
                                        .setCreateBy(order.getCreateBy());
                                balanceService.save(balance);
                            });

                    //3. 处理回调
                    //3.1. 平台按照比例转账到商家支付宝账户

                    TransferDto transfer = new TransferDto();

                    //3.2 商家的支付宝账户
                    transfer.setAliPayAcount(Optional.ofNullable(userService.getById(order.getUserId()))
                            .map(User::getAlipayAccount).orElse(null));

                    //3.3 需要支付给商家的钱款
                    transfer.setAmount(profitPercentService.getProfitPercentLimitOne(ProfitTypeEnum.FIRST)
                            .getStoreProfit().multiply(order.getTotalPrice()));

                    //3.4 转账操作
                    try {
                        String result = payService.aliPayTransfer(transfer);

                        log.info("处理支付宝转账回调信息" + result);

                    } catch (AlipayApiException e) {
                        e.printStackTrace();
                    }
                });
                objectResult = new ResultUtil<>().setData(200, "支付成功!");
            }else {

                return new ResultUtil<>().setErrorMsg(208, "支付密码不一致！");
            }
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
        return null;
    }

    @Override
    public Result<Object> confirmReceive(String orderId) {

        return Optional.ofNullable(this.getById(orderId)).map(order -> {

            //1. 更改订单状态
            order.setOrderStatus(OrderStatusEnum.PRE_COMMENT);
            this.updateById(order);


            //2. 处理分销逻辑
            resolveOrderTwoProfit(orderId);

            return new ResultUtil<>().setSuccessMsg("确认收获成功！");
        }).orElse(new ResultUtil<>().setErrorMsg(201, "该订单不存在！"));
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
        String result = judge(orders);
        if (ToolUtil.isNotEmpty(result)) {

            return result;
        }
        // 注意此处签名方式需与统一下单的签名类型一致
        if (WxPayKit.verifyNotify(params, WeChatConfig.KEY, SignType.HMACSHA256)) {
            if (WxPayKit.codeIsOk(returnCode)) {

                this.settingOrder(orders);

                // 发送通知等
                xml = new HashMap<>(2);
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

            boolean verifyResult = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, AlipayConfig.SIGN_TYPE);

            //付款状态
            String tradeStatus = params.get("trade_status");
            String outTradeNo = params.get("out_trade_no");
            String tradeSuccess = "TRADE_SUCCESS";

            if (tradeStatus.equals(tradeSuccess)) {

                // TODO 请在这里加上商户的业务逻辑程序代码 异步通知可能出现订单重复通知 需要做去重处理
                List<Order> orders = this.listByIds(ToolUtil.splitterStr(outTradeNo));
                String result = judge(orders);
                if (ToolUtil.isNotEmpty(result)) {

                    return result;
                }

                // 处理回调逻辑
                resolveOrderOneProfit(orders);


                object.put("return_code", "200");
                object.put("return_msg", "支付宝notify_url 验证成功 succcess");

            } else {

                System.out.println("notify_url 验证失败");
                // TODO
                object.put("return_code", "-1");
                object.put("return_msg", "notify_url 验证失败，请重新下单");
            }
            return object.toString();

        } catch (Exception e) {

            e.printStackTrace();
            object.put("return_code", "-1");
            object.put("return_msg", "请重新下单");
            return object.toString();
        }
    }

    @Override
    public List<Order> getOrderListByStoreId(String storeId) {

        return Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<Order>lambdaQuery()
                .eq(Order::getUserId, storeId).orderByDesc(Order::getCreateTime))))
                .orElse(null);
    }

    /***
     * 支付宝、微信回调 处理订单
     */
    private String judge(List<Order> orders) {
        Set<OrderStatusEnum> collect = orders.parallelStream().map(Order::getOrderStatus).collect(Collectors.toSet());
        if (collect.size() == 1 && !collect.contains(OrderStatusEnum.PRE_PAY)) {
            Map<String, String> xml = new HashMap<>(2);
            xml.put("return_code", "-1");
            xml.put("return_msg", "订单状态失效或已支付，请重新下单");
            return WxPayKit.toXml(xml);
        }
        return null;
    }


    /**
     * 处理订单回调逻辑
     * @param orders 订单集合
     */
    private void resolveOrderOneProfit(List<Order> orders) {


        orders.parallelStream().forEach(order -> {

            //1. 处理订单状态
            order.setOrderStatus(OrderStatusEnum.PRE_SEND);
            this.updateById(order);

            //2. 平台按照比例转账到商家支付宝账户

            TransferDto transferDto = new TransferDto();

            //2.1 商家的支付宝账户
            transferDto.setAliPayAcount(Optional.ofNullable(userService.getById(order.getUserId()))
            .map(User::getAlipayAccount).orElse(null));

            //2.2 需要支付给商家的钱款
            transferDto.setAmount(profitPercentService.getProfitPercentLimitOne(ProfitTypeEnum.FIRST)
            .getStoreProfit().multiply(order.getTotalPrice()));

            //3. 转账操作
            //3.1 结算金额转到商家余额

            Optional.ofNullable(userService.getById(order.getUserId()))
                    .ifPresent(user -> {

                        //增加余额
                        ThreadPoolUtil.getPool().execute(() -> {

                            user.setBalance(user.getBalance().add(transferDto.getAmount()));
                            userService.updateById(user);
                            log.info("转账成功, 当前转账金额为" + transferDto.getAmount());
                        });

                        //记录明细
                        ThreadPoolUtil.getPool().execute(() -> {

                            Balance balance = new Balance();
                            balance.setAddOrSubtractTypeEnum(AddOrSubtractTypeEnum.ADD)
                                    .setTitle("订单" + order.getOrderNum() + "收入")
                                    .setPrice(transferDto.getAmount())
                                    .setCreateBy(order.getUserId());
                            balanceService.save(balance);

                            log.info("记录商家到账金额明细成功！");
                        });
                    });

            //3.2 记录商家收入管理明细
            // TODO: 2020/5/14 0014  转账成功后才进入到保存收入明细环节,目前暂未做处理
            StoreIncome storeIncome = new StoreIncome();
            storeIncome.setIncomeMoney(profitPercentService.getProfitPercentLimitOne(ProfitTypeEnum.FIRST)
                    .getStoreProfit().multiply(order.getTotalPrice()))
                    .setIncomeType(PayTypeEnum.ALI_PAY)
                    .setOrderId(order.getId())
                    .setCreateBy(order.getUserId());

            storeIncomeService.save(storeIncome);

        });
    }

    /**
     * 处理二级分销
     * @param orderId 订单id
     */
    public void resolveOrderTwoProfit(String orderId) {


        Optional.ofNullable(this.getById(orderId)).ifPresent(order -> {

            //1. 计算当前订单平台预留的资金

            BigDecimal currentProfitAmount = order.getTotalPrice().multiply(profitPercentService
            .getProfitPercentLimitOne(ProfitTypeEnum.FIRST).getPlatformProfit());

            //2. 然后根据比例进行分佣

              //2.1 分佣当前消费者的佣金提成
              Optional.ofNullable(userService.getById(order.getCreateBy())).ifPresent(user -> {

                  //消费者应该分的佣金
                  BigDecimal personProfitMoney = currentProfitAmount.multiply(profitPercentService
                          .getProfitPercentLimitOne(ProfitTypeEnum.TWO).getPersonProfit());

                  ThreadPoolUtil.getPool().execute(() -> {

                      user.setBalance(user.getBalance().add(personProfitMoney));
                      userService.updateById(user);

                      log.info("消费者分佣成功！当前分佣金额" + personProfitMoney);
                  });

                  //2.2 记录余额明细
                  ThreadPoolUtil.getPool().execute(() -> {

                      Balance balance = new Balance();
                      balance.setTitle("佣金到账")
                              .setAddOrSubtractTypeEnum(AddOrSubtractTypeEnum.ADD)
                              .setPrice(personProfitMoney)
                              .setCreateBy(order.getCreateBy());
                      balanceService.save(balance);

                      log.info("记录明细成功！");
                  });

                  //2.3 记录分账明细
                  ThreadPoolUtil.getPool().execute(() -> {

                      ProfitDetail profitDetail = new ProfitDetail();
                      profitDetail.setOrderId(order.getId())
                              .setPayType(PayTypeEnum.BALANCE)
                              .setProfitMoney(personProfitMoney)
                              .setProfitStatus(BooleanTypeEnum.YES)
                              .setCreateBy(order.getCreateBy());
                      profitDetailService.save(profitDetail);

                      log.info("记录资金流向明细成功！");

                  });

              });

            //2.2. 分佣推荐人的佣金
            Optional.ofNullable(userRelationshipService.getRelationUser(order.getCreateBy()))
                    .flatMap(userId -> Optional.ofNullable(userService.getById(userId)))
                    .ifPresent(user -> {

                        //推荐人应该分的佣金
                        BigDecimal personProfitMoney = currentProfitAmount.multiply(profitPercentService
                                .getProfitPercentLimitOne(ProfitTypeEnum.TWO).getRecommendProfit());

                        ThreadPoolUtil.getPool().execute(() -> {

                            user.setBalance(user.getBalance().add(personProfitMoney));
                            userService.updateById(user);

                            log.info("推荐人分佣成功！当前分佣金额" + personProfitMoney);
                        });

                        //2.2 记录余额明细
                        ThreadPoolUtil.getPool().execute(() -> {

                            Balance balance = new Balance();
                            balance.setTitle("佣金到账")
                                    .setAddOrSubtractTypeEnum(AddOrSubtractTypeEnum.ADD)
                                    .setPrice(personProfitMoney)
                                    .setCreateBy(user.getId());
                            balanceService.save(balance);

                            log.info("记录明细成功！");
                        });

                        //2.3 记录分账明细
                        ThreadPoolUtil.getPool().execute(() -> {

                            ProfitDetail profitDetail = new ProfitDetail();
                            profitDetail.setOrderId(order.getId())
                                    .setPayType(PayTypeEnum.BALANCE)
                                    .setProfitMoney(personProfitMoney)
                                    .setProfitStatus(BooleanTypeEnum.YES)
                                    .setCreateBy(user.getId());
                            profitDetailService.save(profitDetail);

                            log.info("记录资金流向明细成功！");

                        });


                    });
        });


    }


    /***
     * 支付宝、微信回调 处理订单
     */
    private void settingOrder(List<Order> orders) {

        orders.parallelStream().forEach(order ->  {

            //更新订单信息
            order.setOrderStatus(OrderStatusEnum.PRE_SEND);
            this.updateById(order);


            // TODO: 2020/5/13 0013 处理分账逻辑

        });

    }


    /*****************************************************后端模块开始*************************************************************/


    /**
     * 获取获取订单列表
     */
    @Override
    public List PCgetShopOrderList(PcOrderDTO pcOrderDTO){

        //查询登录用户的权限
        String userRole = goodService.getRoleUserList(securityUtil.getCurrUser().getId());

        //判断权限是否为空
        if(ToolUtil.isEmpty(userRole)){

            return null;
        }else {

            //查询订单表
            List<Order> orderList = new ArrayList<>();

            //登录角色是管理员
            if(userRole.equals(CommonConstant.ADMIN)){

                //管理员查全部
                orderList = this.list(new QueryWrapper<Order>().lambda()
                        .eq(ToolUtil.isNotEmpty(pcOrderDTO.getId()),Order::getId,pcOrderDTO.getId())
                        .eq(ToolUtil.isNotEmpty(pcOrderDTO.getOrderStatus()),Order::getOrderStatus,pcOrderDTO.getOrderStatus())
                        .eq(ToolUtil.isNotEmpty(pcOrderDTO.getPayTypeEnum()),Order::getPayTypeEnum,pcOrderDTO.getPayTypeEnum())

                );//订单id

                //登录是商家或者个人商家
            }else if(userRole.equals(CommonConstant.STORE)||userRole.equals(CommonConstant.PER_STORE)){

                //查商家自己下的订单
                orderList = this.list(new QueryWrapper<Order>().lambda().eq(Order::getUserId,securityUtil.getCurrUser().getId())
                        .eq(ToolUtil.isNotEmpty(pcOrderDTO.getId()),Order::getId,pcOrderDTO.getId())
                        .eq(ToolUtil.isNotEmpty(pcOrderDTO.getOrderStatus()),Order::getOrderStatus,pcOrderDTO.getOrderStatus())
                        .eq(ToolUtil.isNotEmpty(pcOrderDTO.getPayTypeEnum()),Order::getPayTypeEnum,pcOrderDTO.getPayTypeEnum())
                );//订单id

            }

            //判断订单表不为空
            if(ToolUtil.isNotEmpty(orderList)){

                List<PCgetShopOrderListVO> pCgetShopOrderListVO = orderList.parallelStream()
                        .map(shopOrderList->{

                            PCgetShopOrderListVO shopOrder = new PCgetShopOrderListVO();

                            //订单明细表
                            OrderDetail orderDetai = orderDetailMapper.selectOne(Wrappers.<OrderDetail>lambdaQuery()
                                    .eq(OrderDetail::getOrderId,shopOrderList.getId()));

                            ToolUtil.copyProperties(orderDetai,shopOrder);

                            //订单表
                            ToolUtil.copyProperties(shopOrderList,shopOrder);

                            //店铺名称
                            shopOrder.setShopName(userService.getUserIdByUserName(shopOrderList.getUserId()));

                            //商家类型
                            shopOrder.setShopType(goodService.getRoleUserList(shopOrderList.getUserId()));

                            //商品属性
                            shopOrder.setAttrSymbolPathName(iSizeAndRolorService.attrSymbolPathName(orderDetai.getAttrSymbolPath()));

                            return shopOrder;
                        })//筛选
                        .filter(pcOrderVo->pcOrderVo.getShopName().contains(ToolUtil.isNotEmpty(pcOrderDTO.getShopName())?pcOrderDTO.getShopName():pcOrderVo.getShopName()))
                        .filter(pcOrderVo->pcOrderVo.getGoodName().contains(ToolUtil.isNotEmpty(pcOrderDTO.getGoodName())?pcOrderDTO.getGoodName():pcOrderVo.getGoodName()))
                        .filter(pcOrderVo->pcOrderVo.getShopType().equals(ToolUtil.isNotEmpty(pcOrderDTO.getCommonConstant())?pcOrderDTO.getCommonConstant():pcOrderVo.getShopType()))
                        .collect(Collectors.toList());

                return pCgetShopOrderListVO;
            }else {

                return null;
            }

        }

    }

    @Override
    public Result<Object> insertOffLineOrder(OffLineOrderDto offLineOrderDto) {


        Order order = new Order();

        order.setTypeEnum(OrderTypeEnum.OFFLINE_ORDER)
                .setUserId(Optional.ofNullable(staffManagementService.getOne(Wrappers.<StaffManagement>lambdaQuery()
                .eq(StaffManagement::getStaffId, offLineOrderDto.getStaffId()))).map(StaffManagement::getCreateBy).orElse(null))
                .setCreateBy(securityUtil.getCurrUser().getId());

        ToolUtil.copyProperties(offLineOrderDto, order);

        this.save(order);

        Map<String, Object> map = new ArrayMap<>();
        map.put("id", order.getId());
        map.put("totalPrice", order.getTotalPrice());
        return new ResultUtil<>().setData(map, "插入或者更新成功!");

    }

    @Override
    public BigDecimal getStaffSaleAmount(String staffId) {

        return Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<Order>lambdaQuery()
        .eq(Order::getStaffId, staffId))))
        .map(orders -> orders.parallelStream().map(Order::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add))
        .orElse(new BigDecimal(0));
    }

}