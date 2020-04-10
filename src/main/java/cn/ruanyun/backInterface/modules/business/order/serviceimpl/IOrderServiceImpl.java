package cn.ruanyun.backInterface.modules.business.order.serviceimpl;

import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.business.balance.pojo.Balance;
import cn.ruanyun.backInterface.modules.business.balance.service.IBalanceService;
import cn.ruanyun.backInterface.modules.business.discountCoupon.pojo.DiscountCoupon;
import cn.ruanyun.backInterface.modules.business.discountCoupon.service.IDiscountCouponService;
import cn.ruanyun.backInterface.modules.business.discountMy.VO.DiscountVO;
import cn.ruanyun.backInterface.modules.business.discountMy.pojo.DiscountMy;
import cn.ruanyun.backInterface.modules.business.discountMy.service.IDiscountMyService;
import cn.ruanyun.backInterface.modules.business.good.VO.AppGoodOrderVO;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import cn.ruanyun.backInterface.modules.business.goodsPackage.pojo.GoodsPackage;
import cn.ruanyun.backInterface.modules.business.goodsPackage.service.IGoodsPackageService;
import cn.ruanyun.backInterface.modules.business.harvestAddress.entity.HarvestAddress;
import cn.ruanyun.backInterface.modules.business.harvestAddress.service.IHarvestAddressService;
import cn.ruanyun.backInterface.modules.business.order.DTO.OrderDTO;
import cn.ruanyun.backInterface.modules.business.order.DTO.OrderShowDTO;
import cn.ruanyun.backInterface.modules.business.order.VO.GoodsPackageOrderVO;
import cn.ruanyun.backInterface.modules.business.order.VO.MyOrderVO;
import cn.ruanyun.backInterface.modules.business.order.VO.ShowOrderVO;
import cn.ruanyun.backInterface.modules.business.order.mapper.OrderMapper;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;
import cn.ruanyun.backInterface.modules.business.order.service.IOrderService;
import cn.ruanyun.backInterface.modules.business.orderDetail.pojo.OrderDetail;
import cn.ruanyun.backInterface.modules.business.orderDetail.service.IOrderDetailService;
import cn.ruanyun.backInterface.modules.business.shoppingCart.entity.ShoppingCart;
import cn.ruanyun.backInterface.modules.business.shoppingCart.service.IShoppingCartService;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.pojo.SizeAndRolor;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.service.ISizeAndRolorService;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.api.client.util.ArrayMap;
import dm.jdbc.stat.support.json.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


/**
 * 订单接口实现
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
           for (String userId: strings) {
               Order order = new Order();
               order.setUserId(userId);
               double totalPrice = 0d;
               //下单的地址信息
               HarvestAddress harvestAddress = harvestAddressService.getById(orderDTO.getAddressId());
               harvestAddress.setId(null);
               order.setAddressId(orderDTO.getAddressId());
               ToolUtil.copyProperties(harvestAddress,order);
               //订单下面的商品
               List<OrderDetail> orderDetails = goodMap.get(userId);
               for (OrderDetail orderDetail : orderDetails) {
                   orderDetail.setOrderId(order.getId());
                   orderDetail.setCreateBy(null);
                   orderDetailService.insertOrderUpdateOrderDetail(orderDetail);
                   totalPrice = totalPrice + (orderDetail.getBuyCount() * orderDetail.getGoodNewPrice().doubleValue());
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
               ids += ","+order.getId();
               sumPrice += order.getTotalPrice();
           }
           Map<String,Object> map = new ArrayMap<>();
           map.put("id",ids.toString().substring(1));
           map.put("balance", userService.getAccountBalance());
           map.put("totalPrice", sumPrice);
           return new ResultUtil<>().setData(map,"插入或者更新成功!");
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
            byId.setId(null);
            ToolUtil.copyProperties(byId, orderDetail);

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
        List<String> idList = Arrays.asList(ids.split(","));
        List<Order> orders = this.listByIds(idList);
        if (orders.size() == 0){
            return new ResultUtil<>().setErrorMsg("该订单不存在!");
        }
        totalPrice = new BigDecimal(orders.stream().collect(Collectors.summingDouble(Order::getTotalPrice)));

        if (payTypeEnum.getCode() == 1){

        }else if (payTypeEnum.getCode() == 2){

        //余额支付
        }else if (payTypeEnum.getCode() == 3){
            User byId = userService.getById(securityUtil.getCurrUser().getId());
            int i = byId.getBalance().compareTo(totalPrice);
            if(i == -1){
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
        }
        return new ResultUtil<>().setData(200,"支付成功!");
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

    @Override
    public ShowOrderVO showOrder(OrderShowDTO orderShowDTO) {
        String id = securityUtil.getCurrUser().getId();
        ShowOrderVO showOrderVO = new ShowOrderVO();

        //查询地址信息
        showOrderVO.setHarvestAddressVO(harvestAddressService.getAddressDetail(orderShowDTO.getAddressId()));
        //获取商品的信息
        //直接下单
        List<AppGoodOrderVO> appGoodOrderVOS = new ArrayList<>();
        if (orderShowDTO.getType() == 1){
            appGoodOrderVOS.add(this.getShowOrderVOOne(orderShowDTO));
        //购物车多选下单
        }else if(orderShowDTO.getType() == 2){
            appGoodOrderVOS = Optional.ofNullable(shoppingCartService.listByIds(ToolUtil.splitterStr(orderShowDTO.getShoppingCartIds()))).map(shoppingCarts -> {
                List<AppGoodOrderVO> appGoodOrderVOS1 = shoppingCarts.parallelStream().flatMap(shoppingCart -> {
                    AppGoodOrderVO appGoodOrder = this.getShowOrderVOShoppingCart(shoppingCart);
                    return Stream.of(appGoodOrder);
                }).collect(Collectors.toList());
                return appGoodOrderVOS1;
            }).orElse(null);
            //直接该买套餐商品
        }else if(orderShowDTO.getType() == 3){


            appGoodOrderVOS = Optional.ofNullable(shoppingCartService.listByIds(ToolUtil.splitterStr(orderShowDTO.getShoppingCartIds()))).map(shoppingCarts -> {
                List<AppGoodOrderVO> appGoodOrderVOS1 = shoppingCarts.parallelStream().flatMap(shoppingCart -> {
                    AppGoodOrderVO appGoodOrder = this.getShowOrderVOShoppingCart(shoppingCart);
                    return Stream.of(appGoodOrder);
                }).collect(Collectors.toList());
                return appGoodOrderVOS1;
            }).orElse(null);
        }
        showOrderVO.setAppGoodOrderVOS(appGoodOrderVOS);

        //可获得积分
        int integral = appGoodOrderVOS.stream().filter(appGoodOrderVO -> appGoodOrderVO.getIntegral()!=null).mapToInt(AppGoodOrderVO::getIntegral).sum();
        showOrderVO.setIntegral(integral);

        showOrderVO.setSumMoney(this.getSumPrice(appGoodOrderVOS));

        return showOrderVO;

    }

    /**
     * 商品详情，直接下单
     * @param orderShowDTO
     */
    private AppGoodOrderVO getShowOrderVOOne(OrderShowDTO orderShowDTO){
        AppGoodOrderVO appGoodOrderVO = new AppGoodOrderVO();
        //查询商品信息
        AppGoodOrderVO appGoodOrder = goodService.getAppGoodOrder(orderShowDTO.getGoodId(), orderShowDTO.getColorId(), orderShowDTO.getSizeId());
        //查询商品价格
        SizeAndRolor one = sizeAndRolorService.getOne(Wrappers.<SizeAndRolor>lambdaQuery()
                .eq(SizeAndRolor::getId, orderShowDTO.getSizeId())
                .eq(SizeAndRolor::getParentId, orderShowDTO.getSizeId())
                .eq(SizeAndRolor::getIsParent, 1));
        ToolUtil.copyProperties(appGoodOrder,appGoodOrderVO);
        if (EmptyUtil.isNotEmpty(one)){
            appGoodOrderVO.setGoodNewPrice(one.getGoodsPrice());
        }
        appGoodOrderVO.setCount(orderShowDTO.getCount());

        //处理优惠券信息 订单的价格是否满足
        DiscountVO detailById = discountMyService.getDetailById(orderShowDTO.getDiscountCouponId());
        int i = detailById.getFullMoney().compareTo(new BigDecimal(appGoodOrderVO.getCount()).multiply(appGoodOrderVO.getGoodNewPrice()));
        if (i == -1){
            appGoodOrderVO.setDiscountMyId(detailById.getId());
            detailById.setId(null);
            ToolUtil.copyProperties(detailById,appGoodOrderVO);
        }
        return appGoodOrderVO;
    }

    /**
     * 从购物车下单，获取商品信息
     * @param shoppingCart
     */
    private AppGoodOrderVO getShowOrderVOShoppingCart(ShoppingCart shoppingCart){
        AppGoodOrderVO appGoodOrderVO = new AppGoodOrderVO();
        //查询商品信息
        AppGoodOrderVO appGoodOrder = goodService.getAppGoodOrder(shoppingCart.getGoodId(), shoppingCart.getColorId(), shoppingCart.getSizeId());
        ToolUtil.copyProperties(appGoodOrder,appGoodOrderVO);
        //查询商品价格
        SizeAndRolor one = sizeAndRolorService.getOne(Wrappers.<SizeAndRolor>lambdaQuery()
                .eq(SizeAndRolor::getId, shoppingCart.getSizeId())
                .eq(SizeAndRolor::getParentId, shoppingCart.getColorId())
                .eq(SizeAndRolor::getIsParent, 0));
        if (EmptyUtil.isNotEmpty(one)){
            appGoodOrderVO.setGoodNewPrice(one.getGoodsPrice());
        }
        appGoodOrderVO.setCount(shoppingCart.getCount());
        //获取这个人，这个商品 能用的优惠券
        String id = securityUtil.getCurrUser().getId();
        String goodId = shoppingCart.getGoodId();
        BigDecimal multiply = appGoodOrderVO.getGoodNewPrice().multiply(new BigDecimal(appGoodOrderVO.getCount()));
        DiscountVO dealCanUseCoupon = discountMyService.getDealCanUseCoupon(id,goodId, multiply);

        if (EmptyUtil.isNotEmpty(dealCanUseCoupon)){
            ToolUtil.copyProperties(dealCanUseCoupon,appGoodOrder);
            appGoodOrder.setDiscountMyId(dealCanUseCoupon.getId());
        }


        return appGoodOrderVO;
    }

    /**
     * 计算订单总金额
     * @param appGoodOrderVOS
     */
    private BigDecimal getSumPrice(List<AppGoodOrderVO> appGoodOrderVOS){
        BigDecimal sumPrice = new BigDecimal(0);
        for (AppGoodOrderVO appGoodOrderVO:appGoodOrderVOS) {
            if (StringUtils.isEmpty(appGoodOrderVO.getDiscountMyId())){
                sumPrice.add(appGoodOrderVO.getGoodNewPrice().multiply(new BigDecimal(appGoodOrderVO.getCount())));
            }else {
                sumPrice.add(appGoodOrderVO.getGoodNewPrice().divide(new BigDecimal(appGoodOrderVO.getCount()))).subtract(appGoodOrderVO.getSubtractMoney());
            }
        }
        return sumPrice;
    }

}