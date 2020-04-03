package cn.ruanyun.backInterface.modules.business.order.serviceimpl;

import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.enums.PayTypeEnum;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.business.discountCoupon.pojo.DiscountCoupon;
import cn.ruanyun.backInterface.modules.business.discountCoupon.service.IDiscountCouponService;
import cn.ruanyun.backInterface.modules.business.good.VO.AppGoodOrderVO;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import cn.ruanyun.backInterface.modules.business.harvestAddress.entity.HarvestAddress;
import cn.ruanyun.backInterface.modules.business.harvestAddress.service.IHarvestAddressService;
import cn.ruanyun.backInterface.modules.business.order.DTO.OrderDTO;
import cn.ruanyun.backInterface.modules.business.order.DTO.OrderShowDTO;
import cn.ruanyun.backInterface.modules.business.order.VO.MyOrderVO;
import cn.ruanyun.backInterface.modules.business.order.VO.ShowOrderVO;
import cn.ruanyun.backInterface.modules.business.order.mapper.OrderMapper;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;
import cn.ruanyun.backInterface.modules.business.order.service.IOrderService;
import cn.ruanyun.backInterface.modules.business.orderDetail.pojo.OrderDetail;
import cn.ruanyun.backInterface.modules.business.orderDetail.service.IOrderDetailService;
import cn.ruanyun.backInterface.modules.business.shoppingCart.service.IShoppingCartService;
import com.alibaba.fastjson.JSON;
import dm.jdbc.stat.support.json.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;


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
       private IDiscountCouponService discountCouponService;
       @Autowired
       private IGoodService goodService;
       @Autowired
       private IOrderDetailService orderDetailService;
       @Autowired
       private IUserService userService;

       @Override
       public Result<Object> insertOrderUpdateOrder(OrderDTO orderDTO) {
           Order order = new Order();
           if (ToolUtil.isEmpty(order.getCreateBy())) {
               order.setCreateBy(securityUtil.getCurrUser().getId());
           } else {
               order.setUpdateBy(securityUtil.getCurrUser().getId());
           }
           //下单的地址信息
           HarvestAddress harvestAddress = harvestAddressService.getById(orderDTO.getAddressId());
           harvestAddress.setId(null);
           ToolUtil.copyProperties(harvestAddress,order);
           //下单的优惠信息
           if(ToolUtil.isNotEmpty(orderDTO.getDiscountCouponId())){
               DiscountCoupon discountCoupon = discountCouponService.getById(orderDTO.getDiscountCouponId());
               discountCoupon.setId(null);
               ToolUtil.copyProperties(discountCoupon,order);
           }

           //"[{"sizeId":"256005326325682176","buyCount":5,"colorId":"256004810652782592","goodId":"1243581862115827714"},{"sizeId":"256005326325682176","buyCount":5,"colorId":"256004810652782592","goodId":"1243581862115827714"}]"
           JSONArray jsonArray = new JSONArray(orderDTO.getGoods().toString());
           for (int i = 0; i < jsonArray.length(); i++) {
               OrderDetail orderDetail = JSON.parseObject(jsonArray.get(i).toString(), OrderDetail.class);
               orderDetail.setOrderId(order.getId());
               Good byId = goodService.getById(orderDetail.getGoodId());
               byId.setId(null);
               ToolUtil.copyProperties(byId,orderDetail);
               orderDetailService.insertOrderUpdateOrderDetail(orderDetail);
           }
           //直接下单的商品

           Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(order)))
                   .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                   .toFuture().join();

           return new ResultUtil<>().setData(order.getId(),"插入或者更新成功!");
       }

    /**
     * 支付
     *
     * @param id
     * @param payTypeEnum
     */
    @Override
    public Result<Object> payOrder(String id, PayTypeEnum payTypeEnum) {
        Order order = this.getById(id);

        //统计订单总金额


        if (payTypeEnum.getCode() == 1){

        }else if (payTypeEnum.getCode() == 2){

        //余额支付
        }else if (payTypeEnum.getCode() == 3){
            User byId = userService.getById(securityUtil.getCurrUser().getId());
            BigDecimal balance = byId.getBalance();
            BigDecimal fullMoney = order.getFullMoney();
            int i = byId.getBalance().compareTo(order.getFullMoney());
            if(i == -1){
                return new ResultUtil<>().setErrorMsg("余额不足!");
            }
//            生成账单流水
            order.setOrderStatus(OrderStatusEnum.PRE_SEND);
            this.updateById(order);
        }
        return null;
    }

    @Override
      public void removeOrder(String ids) {
          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    @Override
    public List<MyOrderVO> getMyOrderList(Order order, PageVo pageVo) {
        return null;
    }

    @Override
    public void confirmReceipt(String orderId) {

    }

    @Override
    public void sendOrder(String orderId, String logistics) {

    }

    @Override
    public Boolean judgeCouponCanUse(Order order) {
        return null;
    }

    @Override
    public Map<String, BigDecimal> getStoreMoney(String userId) {
        return null;
    }

    @Override
    public void updateClearingOrder(String userId, Integer code) {

    }

    @Override
    public Map<String, Object> payInfo(Order order) {
        return null;
    }

    @Override
    public Object getInfo(String productId) {
        return null;
    }

    @Override
    public void insertOrderByShopping(OrderDTO orderDTO) {

    }

    @Override
    public ShowOrderVO showOrder(OrderShowDTO orderShowDTO) {
        ShowOrderVO showOrderVO = new ShowOrderVO();
        //商品的总金额
        BigDecimal sumMoney = new BigDecimal(0);
        //运费
        BigDecimal freightMoney = new BigDecimal(0);
        //可获得积分
        int integral = 0;
        //查询地址信息
        showOrderVO.setHarvestAddressVO(harvestAddressService.getAddressDetail(orderShowDTO.getAddressId()));
        //优惠券
        DiscountCoupon byId1 = discountCouponService.getById(orderShowDTO.getDiscountCouponId());
        ToolUtil.copyProperties(byId1,showOrderVO);

        //获取商品的信息
        //直接下单
        List<AppGoodOrderVO> appGoodOrderVOS = new ArrayList<>();

        List<Map<String,Object>> goods = new ArrayList<>();

        if (orderShowDTO.getType() == 1){
            AppGoodOrderVO appGoodOrder = goodService.getAppGoodOrder(orderShowDTO.getGoodId(), orderShowDTO.getColorId(), orderShowDTO.getSizeId());
            appGoodOrderVOS.add(appGoodOrder);
            appGoodOrder.setCount(orderShowDTO.getCount());
            BigDecimal divide = appGoodOrder.getGoodNewPrice().divide(new BigDecimal(orderShowDTO.getCount()));
            sumMoney= sumMoney.add(divide);
            goods.add(this.good(appGoodOrder,orderShowDTO.getColorId(),orderShowDTO.getSizeId()));
        //购物车多选下单
        }else {
            appGoodOrderVOS = Optional.ofNullable(shoppingCartService.listByIds(ToolUtil.splitterStr(orderShowDTO.getShoppingCartIds()))).map(shoppingCarts -> {
                List<AppGoodOrderVO> appGoodOrderVOS1 = shoppingCarts.parallelStream().flatMap(shoppingCart -> {
                    AppGoodOrderVO appGoodOrder = goodService.getAppGoodOrder(shoppingCart.getGoodId(), shoppingCart.getColorId(), shoppingCart.getSizeId());
                    appGoodOrder.setCount(Integer.parseInt(shoppingCart.getCount()));
//                    sumMoney = sumMoney.add(shoppingCart.getTotalPrice());
                    goods.add(this.good(appGoodOrder,shoppingCart.getColorId(),shoppingCart.getSizeId()));
                    return Stream.of(appGoodOrder);
                }).collect(Collectors.toList());
                return appGoodOrderVOS1;
            }).orElse(null);
        }
        showOrderVO.setAppGoodOrderVOS(appGoodOrderVOS);


        //可获得积分
        integral = appGoodOrderVOS.stream().filter(appGoodOrderVO -> appGoodOrderVO.getIntegral()!=null).mapToInt(AppGoodOrderVO::getIntegral).sum();

        showOrderVO.setSumMoney(sumMoney);
        showOrderVO.setIntegral(integral);

        //确认下单的时候需要传的商品的数据
        showOrderVO.setGoods(JSON.toJSONString(goods));
        return showOrderVO;

    }

    private Map<String,Object> good(AppGoodOrderVO appGoodOrder,String color,String size){
        Map<String,Object> good = new HashMap<>();
        good.put("goodId",appGoodOrder.getId());
        good.put("buyCount",appGoodOrder.getCount());
        good.put("colorId",color);
        good.put("sizeId",size);
        return good;
    }
}