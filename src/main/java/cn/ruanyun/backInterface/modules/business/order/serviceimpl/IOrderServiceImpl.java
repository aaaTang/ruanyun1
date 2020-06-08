package cn.ruanyun.backInterface.modules.business.order.serviceimpl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import cn.ruanyun.backInterface.common.enums.*;
import cn.ruanyun.backInterface.common.pay.common.alipay.AliPayUtilTool;
import cn.ruanyun.backInterface.common.pay.common.wxpay.WeChatConfig;
import cn.ruanyun.backInterface.common.pay.dto.TransferDto;
import cn.ruanyun.backInterface.common.pay.model.PayModel;
import cn.ruanyun.backInterface.common.pay.service.IPayService;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.VO.CompereAuctionCalendarVO;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.service.ICompereAuctionCalendarService;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.mapper.SiteMapper;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.service.ISiteService;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.vo.SiteDetailTimeVO;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.business.balance.pojo.Balance;
import cn.ruanyun.backInterface.modules.business.balance.service.IBalanceService;
import cn.ruanyun.backInterface.modules.business.comment.DTO.CommentDTO;
import cn.ruanyun.backInterface.modules.business.comment.service.ICommentService;
import cn.ruanyun.backInterface.modules.business.commonParam.service.IcommonParamService;
import cn.ruanyun.backInterface.modules.business.discountCoupon.pojo.DiscountCoupon;
import cn.ruanyun.backInterface.modules.business.discountCoupon.service.IDiscountCouponService;
import cn.ruanyun.backInterface.modules.business.discountMy.service.IDiscountMyService;
import cn.ruanyun.backInterface.modules.business.good.mapper.GoodMapper;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import cn.ruanyun.backInterface.modules.business.goodCategory.mapper.GoodCategoryMapper;
import cn.ruanyun.backInterface.modules.business.goodCategory.service.IGoodCategoryService;
import cn.ruanyun.backInterface.modules.business.goodsPackage.pojo.GoodsPackage;
import cn.ruanyun.backInterface.modules.business.goodsPackage.service.IGoodsPackageService;
import cn.ruanyun.backInterface.modules.business.harvestAddress.service.IHarvestAddressService;
import cn.ruanyun.backInterface.modules.business.order.dto.*;
import cn.ruanyun.backInterface.modules.business.order.mapper.OrderMapper;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;
import cn.ruanyun.backInterface.modules.business.order.service.IOrderService;
import cn.ruanyun.backInterface.modules.business.order.vo.AppMyOrderDetailVo;
import cn.ruanyun.backInterface.modules.business.order.vo.AppMyOrderListVo;
import cn.ruanyun.backInterface.modules.business.order.vo.BackOrderListVO;
import cn.ruanyun.backInterface.modules.business.orderDetail.mapper.OrderDetailMapper;
import cn.ruanyun.backInterface.modules.business.orderDetail.pojo.OrderDetail;
import cn.ruanyun.backInterface.modules.business.orderDetail.service.IOrderDetailService;
import cn.ruanyun.backInterface.modules.business.profitDetail.pojo.ProfitDetail;
import cn.ruanyun.backInterface.modules.business.profitDetail.service.IProfitDetailService;
import cn.ruanyun.backInterface.modules.business.profitPercent.service.IProfitPercentService;
import cn.ruanyun.backInterface.modules.business.shoppingCart.entity.ShoppingCart;
import cn.ruanyun.backInterface.modules.business.shoppingCart.service.IShoppingCartService;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.mapper.SizeAndRolorMapper;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.pojo.SizeAndRolor;
import cn.ruanyun.backInterface.modules.business.staffManagement.pojo.StaffManagement;
import cn.ruanyun.backInterface.modules.business.staffManagement.service.IStaffManagementService;
import cn.ruanyun.backInterface.modules.business.storeIncome.pojo.StoreIncome;
import cn.ruanyun.backInterface.modules.business.storeIncome.service.IStoreIncomeService;
import cn.ruanyun.backInterface.modules.business.userRelationship.service.IUserRelationshipService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.api.client.util.ArrayMap;
import com.google.api.client.util.Lists;
import com.google.api.client.util.Maps;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.WxPayKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
    private IDiscountCouponService discountCouponService;

    @Autowired
    private IGoodsPackageService goodsPackageService;

    @Autowired
    private IPayService payService;

    @Autowired
    private IUserRelationshipService userRelationshipService;

    @Autowired
    private IBalanceService balanceService;

    @Autowired
    private IProfitPercentService profitPercentService;

    @Autowired
    private IProfitDetailService profitDetailService;

    @Autowired
    private IStoreIncomeService storeIncomeService;

    @Autowired
    private IStaffManagementService staffManagementService;

    @Autowired
    private IcommonParamService icommonParamService;

    @Autowired
    private ICommentService iCommentService;

    @Resource
    private GoodCategoryMapper goodCategoryMapper;

    @Resource
    private OrderDetailMapper orderDetailMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private GoodMapper goodMapper;

    @Resource
    private SiteMapper siteMapper;

    @Autowired
    private ISiteService iSiteService;

    @Autowired
    private ICompereAuctionCalendarService iCompereAuctionCalendarService;

    @Resource
    private SizeAndRolorMapper sizeAndRolorMapper;

    @Autowired
    private IGoodCategoryService goodCategoryService;



    @Override
    public Result<Object> insertOrder(OrderDto orderDTO) {

        String currentUserId = securityUtil.getCurrUser().getId();

        //存储orderId
        List<String> orderIds = Lists.newArrayList();

        //1. 解析商品信息字符串
        List<AppOrderGoodInfoDto> appOrderGoodInfo = JSONObject.parseArray(orderDTO.getGoodStr(), AppOrderGoodInfoDto.class);



       return Optional.ofNullable(ToolUtil.setListToNul(appOrderGoodInfo)).map(appOrderGoodInfoDtos -> {

            appOrderGoodInfoDtos.parallelStream().forEach(appOrderGoodInfoDto -> {

                //1. 生成订单
                Order order = new Order();

                //2  生成订单详情
                OrderDetail orderDetail = new OrderDetail();

                //2.2 参数信息
                ToolUtil.copyProperties(appOrderGoodInfoDto, orderDetail);

                order.setCreateBy(currentUserId);

                //购买类型
                order.setBuyType(appOrderGoodInfoDto.getBuyType());

                //1.1 商家id
                if (appOrderGoodInfoDto.getShopCartType().equals(ShopCartTypeEnum.GOOD)) {

                    order.setUserId(Optional.ofNullable(goodService.getById(appOrderGoodInfoDto.getGoodId()))
                    .map(Good::getCreateBy).orElse(null));

                }else if (appOrderGoodInfoDto.getShopCartType().equals(ShopCartTypeEnum.GOOD_PACKAGE)) {

                    order.setUserId(Optional.ofNullable(goodsPackageService.getById(appOrderGoodInfoDto.getGoodId()))
                    .map(GoodsPackage::getCreateBy).orElse(null));
                }

                //1.2 总价格,支付定金价格

                //1.3 判断购买类型
                if (appOrderGoodInfoDto.getBuyType().equals(BuyTypeEnum.FULL_PURCHASE)) {

                    order.setTotalPrice(appOrderGoodInfoDto.getPrice());

                }else if (appOrderGoodInfoDto.getBuyType().equals(BuyTypeEnum.RENT)) {

                    appOrderGoodInfoDto.setPrice(appOrderGoodInfoDto.getGoodDeposit().add(appOrderGoodInfoDto.getGoodBalancePayment()));
                    order.setTotalPrice(appOrderGoodInfoDto.getPrice().multiply(new BigDecimal(appOrderGoodInfoDto.getBuyCount())))
                            .setGoodDeposit(appOrderGoodInfoDto.getGoodDeposit().multiply(new BigDecimal(appOrderGoodInfoDto.getBuyCount())))
                            .setPayGoodBalancePayment((appOrderGoodInfoDto.getPrice().subtract(appOrderGoodInfoDto.getGoodDeposit())).multiply(new BigDecimal(appOrderGoodInfoDto.getBuyCount())))
                            .setRentType(goodCategoryService.getLeaseState(Optional.ofNullable(goodService.getById(appOrderGoodInfoDto.getGoodId()))
                            .map(Good::getGoodCategoryId).orElse(null)))
                            .setBuyType(BuyTypeEnum.RENT);

                    orderDetail.setPrice(appOrderGoodInfoDto.getPrice())
                            .setGoodDeposit(appOrderGoodInfoDto.getGoodDeposit())
                            .setGoodBalancePayment((appOrderGoodInfoDto.getPrice().subtract(appOrderGoodInfoDto.getGoodDeposit())));

                }

                //1.4 优惠券满减
                if (ToolUtil.isNotEmpty(appOrderGoodInfoDto.getDiscountId())) {

                    Optional.ofNullable(discountCouponService.getDiscountCouponDetail(appOrderGoodInfoDto.getDiscountId()))
                            .ifPresent(discountCoupon -> {

                                order.setTotalPrice(order.getTotalPrice().subtract(discountCoupon.getSubtractMoney()))
                                        .setGoodDeposit(order.getGoodDeposit().subtract(discountCoupon.getSubtractMoney()));

                                //设置优惠券已使用
                                discountMyService.changeMyDisCouponStatus(discountCoupon.getId(), currentUserId);
                            });

                }

                //1.4 收货人信息
                Optional.ofNullable(harvestAddressService.getAddressDetail(orderDTO.getAddressId()))
                        .ifPresent(harvestAddressVO -> ToolUtil.copyProperties(harvestAddressVO, order));

                //1.5 订单类型
                if (appOrderGoodInfoDto.getShopCartType().equals(ShopCartTypeEnum.GOOD)) {

                    order.setTypeEnum(OrderTypeEnum.GOOD);
                }else if (appOrderGoodInfoDto.getShopCartType().equals(ShopCartTypeEnum.GOOD_PACKAGE)) {

                    order.setTypeEnum(OrderTypeEnum.GOODS_PACKAGE);
                }

                ToolUtil.copyProperties(orderDTO, order);

                //除去id信息
                order.setId(null);

                if (this.save(order)) {

                    //2.3 优惠券信息
                    Optional.ofNullable(discountCouponService.getDiscountCouponDetail(appOrderGoodInfoDto.getDiscountId()))
                            .ifPresent(discountCoupon -> {

                                orderDetail.setDiscountId(discountCoupon.getId());
                                orderDetail.setSubtractMoney(discountCoupon.getSubtractMoney());
                            });

                    orderDetail.setAttrSymbolPath(Optional.ofNullable(sizeAndRolorMapper.selectById(appOrderGoodInfoDto.getAttrSymbolPath())).map(SizeAndRolor::getAttrSymbolPath).orElse(null));

                    orderDetail.setOrderId(order.getId());

                    orderDetail.setCreateBy(currentUserId);

                    orderDetailService.save(orderDetail);

                    orderIds.add(order.getId());
                }


                //清除购物车
                Optional.ofNullable(shoppingCartService.getOne(Wrappers.<ShoppingCart>lambdaQuery()
                        .eq(ShoppingCart::getGoodId, appOrderGoodInfoDto.getGoodId())
                        .eq(ShoppingCart::getBuyType, appOrderGoodInfoDto.getBuyType())
                        .eq(ShoppingCart::getAttrSymbolPath, Optional.ofNullable(sizeAndRolorMapper.selectById(appOrderGoodInfoDto.getAttrSymbolPath())).map(SizeAndRolor::getAttrSymbolPath).orElse(null))
                        .eq(ShoppingCart::getShopCartType, appOrderGoodInfoDto.getShopCartType())
                        .eq(ShoppingCart::getCreateBy, currentUserId)))
                        .ifPresent(shoppingCart -> shoppingCartService.removeById(shoppingCart.getId()));
            });


            //计算总价格
           BigDecimal buyTotalPrice = appOrderGoodInfoDtos.parallelStream().filter(appOrderGoodInfoDto ->
                   cn.hutool.core.util.ObjectUtil.equal(appOrderGoodInfoDto.getBuyType(), BuyTypeEnum.FULL_PURCHASE))
                   .map(appOrderGoodInfoDto -> {

                       BigDecimal totalPrice = appOrderGoodInfoDto.getPrice().multiply(new BigDecimal(appOrderGoodInfoDto.getBuyCount()));

                       if (ToolUtil.isNotEmpty(appOrderGoodInfoDto.getDiscountId())) {

                           totalPrice.subtract(Optional.ofNullable(discountCouponService.getDiscountCouponDetail(appOrderGoodInfoDto.getDiscountId()))
                           .map(DiscountCoupon::getSubtractMoney).orElse(new BigDecimal(0)));
                       }

                       return totalPrice;
                   })
                   .reduce(BigDecimal.ZERO, BigDecimal::add);

           BigDecimal rentTotalPrice = appOrderGoodInfoDtos.parallelStream().filter(appOrderGoodInfoDto ->
                   cn.hutool.core.util.ObjectUtil.equal(appOrderGoodInfoDto.getBuyType(), BuyTypeEnum.RENT))
                   .map(appOrderGoodInfoDto -> {

                       BigDecimal totalPrice = appOrderGoodInfoDto.getGoodDeposit().multiply(new BigDecimal(appOrderGoodInfoDto.getBuyCount()));

                       if (ToolUtil.isNotEmpty(appOrderGoodInfoDto.getDiscountId())) {

                           totalPrice.subtract(Optional.ofNullable(discountCouponService.getDiscountCouponDetail(appOrderGoodInfoDto.getDiscountId()))
                                   .map(DiscountCoupon::getSubtractMoney).orElse(new BigDecimal(0)));
                       }

                       return totalPrice;
                   })
                   .reduce(BigDecimal.ZERO, BigDecimal::add);


            Map<String, Object> result = Maps.newHashMap();

            result.put("ids", ToolUtil.joinerList(orderIds));

            result.put("totalPrice", buyTotalPrice.add(rentTotalPrice));

            //当前用户余额
            result.put("balance", userService.getById(currentUserId).getBalance());

            return new ResultUtil<>().setData(result, "订单生成成功！");

        }).orElse( new ResultUtil<>().setErrorMsg(208, "订单生成失败！"));

    }


    /**
     * 档期下单
     * @return Object
     */
    @Override
    public Result<Object> inserAuctionCalendartOrder(AuctionCalendartOrderDTO auctionCalendartOrderDTO) {

        //1. 生成订单
        Order order = new Order();

        //2  生成订单详情
        OrderDetail orderDetail = new OrderDetail();

        String currentUserId = securityUtil.getCurrUser().getId();

        order.setCreateBy(currentUserId);

        //存储orderId
        List<String> orderIds = Lists.newArrayList();


        //1.2 总价格,支付定金价格
        //1.3 判断购买类型
            //1.3.1  订单类型是婚宴档期预约订单  并且 购物车商品类型是档期
        if (auctionCalendartOrderDTO.getTypeEnum().equals(OrderTypeEnum.SCHEDULE_ORDER)&&auctionCalendartOrderDTO.getShopCartType().equals(ShopCartTypeEnum.AUCTION_CALENDAR)){

            Order order1 =  orderMapper.selectOne(new QueryWrapper<Order>().lambda()
                    .eq(Order::getSiteId,auctionCalendartOrderDTO.getSiteId())
                    .eq(Order::getScheduleAppointment,auctionCalendartOrderDTO.getScheduleAppointment())
                    .eq(Order::getDayTimeType,auctionCalendartOrderDTO.getDayTimeType())
                    .ge(Order::getOrderStatus,OrderStatusEnum.PRE_SEND)
                    .last("limit 1")
            );
            if(ToolUtil.isNotEmpty(order1)){
              return new ResultUtil<>().setErrorMsg(201,"此场地已经被预约");
            }

            //2.获取档期价格
            List<SiteDetailTimeVO> siteDetailTimeVOS =  iSiteService.getSiteDetailTime(auctionCalendartOrderDTO.getSiteId(),auctionCalendartOrderDTO.getScheduleAppointment());

            for (SiteDetailTimeVO siteDetailTimeVO : siteDetailTimeVOS) {

                if(siteDetailTimeVO.getDayTimeType().equals(auctionCalendartOrderDTO.getDayTimeType())){
                    //2.1婚宴酒店的价格
                    order.setTotalPrice(siteDetailTimeVO.getSitePrice());
                }

            }

            Optional.ofNullable(siteMapper.selectById(auctionCalendartOrderDTO.getSiteId()))
                   .ifPresent(site -> {
                       // 商家id
                       order.setUserId(site.getCreateBy());
                        //场地图片
                       if(ToolUtil.isNotEmpty(site.getSitePics())){
                           orderDetail.setPic(site.getSitePics().split(",")[0]);
                       }
                        //订单明细的商品名称
                       orderDetail.setName(site.getSiteName());
                   });

            order.setTypeEnum(OrderTypeEnum.SCHEDULE_ORDER);

            //场地id
            order.setSiteId(auctionCalendartOrderDTO.getSiteId());



            //1.3.2 订单类型是主持人预约订单 并且 购物车商品类型是商品
        }else if(auctionCalendartOrderDTO.getTypeEnum().equals(OrderTypeEnum.COMPERE_ORDER)&&auctionCalendartOrderDTO.getShopCartType().equals(ShopCartTypeEnum.GOOD)){

           List<Order> orderList= Optional.ofNullable(ToolUtil.setListToNul(
                   orderMapper.selectList(new QueryWrapper<Order>().lambda()
                           .eq(Order::getTypeEnum,OrderTypeEnum.COMPERE_ORDER)
                           .eq(Order::getScheduleAppointment,auctionCalendartOrderDTO.getScheduleAppointment())
                           .eq(Order::getDayTimeType,auctionCalendartOrderDTO.getDayTimeType())
                           .ge(Order::getOrderStatus,OrderStatusEnum.PRE_SEND)
                   )
           )).map(orders -> orders.parallelStream().filter(order1 -> cn.hutool.core.util.ObjectUtil.equal(

                   orderDetailMapper.selectOne(Wrappers.<OrderDetail>lambdaQuery()
                           .eq(OrderDetail::getOrderId,order1.getId())
                           .eq(OrderDetail::getGoodId,auctionCalendartOrderDTO.getGoodId())
                           .eq(OrderDetail::getShopCartType,ShopCartTypeEnum.GOOD)).getGoodId()
                   ,auctionCalendartOrderDTO.getGoodId()
           )).collect(Collectors.toList())).orElse(null);

           if(ToolUtil.isNotEmpty(orderList)){
               return new ResultUtil<>().setErrorMsg(201,"此商品已经被购买！");
           }

            //2.2获取主持人商品价格
            order.setTotalPrice(this.inserGoodOrder(auctionCalendartOrderDTO.getGoodId(),auctionCalendartOrderDTO.getScheduleAppointment(),auctionCalendartOrderDTO.getDayTimeType()));


            List<Order> orderList2= Optional.ofNullable(ToolUtil.setListToNul(
                    orderMapper.selectList(new QueryWrapper<Order>().lambda()
                            .eq(Order::getTypeEnum,OrderTypeEnum.COMPERE_ORDER)
                            .eq(Order::getScheduleAppointment,auctionCalendartOrderDTO.getScheduleAppointment())
                            .eq(Order::getDayTimeType,auctionCalendartOrderDTO.getDayTimeType())
                            .ge(Order::getOrderStatus,OrderStatusEnum.PRE_SEND)
                    )
            )).map(orders -> orders.parallelStream().filter(order1 -> cn.hutool.core.util.ObjectUtil.equal(

                    orderDetailMapper.selectOne(Wrappers.<OrderDetail>lambdaQuery()
                            .eq(OrderDetail::getOrderId,order1.getId())
                            .eq(OrderDetail::getGoodId,auctionCalendartOrderDTO.getGoodId())
                            .eq(OrderDetail::getShopCartType,ShopCartTypeEnum.GOOD_PACKAGE)).getGoodId()
                    ,auctionCalendartOrderDTO.getGoodId()
            )).collect(Collectors.toList())).orElse(null);

            if(ToolUtil.isNotEmpty(orderList2)){
                return new ResultUtil<>().setErrorMsg(201,"此套餐已经被购买！");
            }

            Optional.ofNullable(goodMapper.selectById(auctionCalendartOrderDTO.getGoodId()))
                    .ifPresent(good -> {
                        // 商家id
                        order.setUserId(good.getCreateBy());
                        //订单明细的商品id
                        orderDetail.setGoodId(good.getId());
                        //商品图片
                        if(ToolUtil.isNotEmpty(good.getGoodPics())){
                            orderDetail.setPic(good.getGoodPics().split(",")[0]);
                        }
                        //订单明细的商品名称
                        orderDetail.setName(good.getGoodName());
                    });

                //主持人档期预约订单
            order.setTypeEnum(OrderTypeEnum.COMPERE_ORDER);




            //1.3.2 订单类型是主持人预约订单 并且 购物车商品类型是套餐
        }else if(auctionCalendartOrderDTO.getTypeEnum().equals(OrderTypeEnum.COMPERE_ORDER)&&auctionCalendartOrderDTO.getShopCartType().equals(ShopCartTypeEnum.GOOD_PACKAGE)){

            //2.3获取主持人套餐价格
            order.setTotalPrice(this.inserGoodOrder(auctionCalendartOrderDTO.getGoodId(),auctionCalendartOrderDTO.getScheduleAppointment(),auctionCalendartOrderDTO.getDayTimeType()));

            Optional.ofNullable(goodMapper.selectById(auctionCalendartOrderDTO.getGoodId()))
                    .ifPresent(good -> {
                        // 商家id
                        order.setUserId(good.getCreateBy());
                        //定案商品图片
                        if(ToolUtil.isNotEmpty(good.getGoodPics())){
                            orderDetail.setPic(good.getGoodPics().split(",")[0]);
                        }
                        //订单明细的商品id
                        orderDetail.setGoodId(good.getId());
                        //订单明细的商品名称
                        orderDetail.setName(good.getGoodName());
                    });

            //主持人档期预约订单
            order.setTypeEnum(OrderTypeEnum.COMPERE_ORDER);
        }

        //上午&下午
        order.setScheduleAppointment(auctionCalendartOrderDTO.getScheduleAppointment());
        //预约档期
        order.setDayTimeType(auctionCalendartOrderDTO.getDayTimeType());
        
        order.setBuyType(BuyTypeEnum.FULL_PURCHASE);


        //3 优惠券满减
        if (ToolUtil.isNotEmpty(auctionCalendartOrderDTO.getDiscountId())) {

            Optional.ofNullable(discountCouponService.getDiscountCouponDetail(auctionCalendartOrderDTO.getDiscountId()))
                    .ifPresent(discountCoupon -> {

                        order.setTotalPrice(order.getTotalPrice().subtract(discountCoupon.getSubtractMoney()));

                        //设置优惠券已使用
                        discountMyService.changeMyDisCouponStatus(discountCoupon.getId(), currentUserId);
                    });

        }

        //除去id信息
        order.setId(null);

        if (this.save(order)) {


            //2.3 优惠券信息
            Optional.ofNullable(discountCouponService.getDiscountCouponDetail(auctionCalendartOrderDTO.getDiscountId()))
                    .ifPresent(discountCoupon -> {

                        orderDetail.setDiscountId(discountCoupon.getId());
                        orderDetail.setSubtractMoney(discountCoupon.getSubtractMoney());
                    });

            order.setBuyType(BuyTypeEnum.FULL_PURCHASE);

            orderDetail.setShopCartType(auctionCalendartOrderDTO.getShopCartType());

            orderDetail.setOrderId(order.getId());

            orderDetail.setPrice(order.getTotalPrice());

            orderDetail.setCreateBy(currentUserId);

            orderDetail.setBuyCount(1);

            orderDetail.setShopCartType(auctionCalendartOrderDTO.getShopCartType());

            orderDetailService.save(orderDetail);

            orderIds.add(order.getId());



            Map<String, Object> result = Maps.newHashMap();

            result.put("ids", ToolUtil.joinerList(orderIds));

            result.put("totalPrice", order.getTotalPrice());

            //当前用户余额
            result.put("balance", userService.getById(currentUserId).getBalance());

            return new ResultUtil<>().setData(result, "订单生成成功！");
        }else {

            return new ResultUtil<>().setErrorMsg(201, "订单生成失败！");
        }


    }

    /**
     * 获取主持人的档期价格
     * @param goodIs 商品id
     * @param scheduleAppointment 档期时间
     * @param dayTimeTypeEnum  档期类型
     * @return
     */
    @Override
    public BigDecimal inserGoodOrder(String goodIs,String scheduleAppointment,DayTimeTypeEnum dayTimeTypeEnum){

        //3获取档期是价格
        List<CompereAuctionCalendarVO> compereAuctionCalendarVOS = iCompereAuctionCalendarService.AppGetCompereAuctionCalendar(goodIs,scheduleAppointment);

        for (CompereAuctionCalendarVO compereAuctionCalendarVO : compereAuctionCalendarVOS) {

            if(compereAuctionCalendarVO.getDayTimeType().equals(dayTimeTypeEnum)){
                //3.1注册人商品价格
              return compereAuctionCalendarVO.getSitePrice();
            }
        }

        return null;
    }




    /**
     * 支付
     * @param appPayOrder 支付参数
     * @return Object
     */
    @Override
    public Result<Object> payOrder(AppPayOrderDto appPayOrder) {

       return Optional.ofNullable(ToolUtil.setListToNul(this.listByIds(ToolUtil.splitterStr(appPayOrder.getIds()))))
                .map(orders -> {

                    //1. 支付参数
                    PayModel payModel = new PayModel();
                    payModel.setOrderNums(appPayOrder.getIds());

                    //1.1 计算总价格

                    Map<BuyTypeEnum, List<Order>> groupByType = orders.parallelStream().collect(Collectors.groupingBy(Order::getBuyType));

                    BigDecimal totalPrice = new BigDecimal(0);

                    groupByType.forEach((k, v) -> {

                        if (cn.hutool.core.util.ObjectUtil.equal(BuyTypeEnum.FULL_PURCHASE, k)) {

                            totalPrice.add(v.parallelStream().map(Order::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
                        }else {

                            totalPrice.add(v.parallelStream().map(Order::getGoodDeposit).reduce(BigDecimal.ZERO, BigDecimal::add));
                        }
                    });

                    payModel.setTotalPrice(totalPrice);

                    //2. 处理支付
                    //微信
                      if (appPayOrder.getPayType().equals(PayTypeEnum.WE_CHAT)) {

                        return payService.wxPayMethod(payModel);

                        //支付宝
                    } else if (appPayOrder.getPayType().equals(PayTypeEnum.ALI_PAY)) {

                        return payService.aliPayMethod(payModel);

                        //余额支付
                    } else if (appPayOrder.getPayType().equals(PayTypeEnum.BALANCE)) {

                        User user = userService.getById(securityUtil.getCurrUser().getId());

                        if (ToolUtil.isEmpty(user.getPayPassword())) {

                            return new ResultUtil<>().setErrorMsg(206, "暂未设置支付密码");
                        }
                        if (new BCryptPasswordEncoder().matches(appPayOrder.getPayPassword(), user.getPayPassword())) {

                            if (user.getBalance().compareTo(payModel.getTotalPrice()) < 0) {

                                return new ResultUtil<>().setErrorMsg(207, "余额不足！");
                            }

                            orders.forEach(order -> {

                                //更新订单
                                order.setPayTypeEnum(appPayOrder.getPayType());
                                order.setOrderStatus(OrderStatusEnum.PRE_SEND);
                                this.updateById(order);

                                //2.减少用户余额,记录明细
                                Optional.ofNullable(userService.getById(order.getCreateBy()))
                                        .ifPresent(userCreate -> {

                                            userCreate.setBalance(userCreate.getBalance().subtract(getTotalPriceByOrderType(order)));
                                            userService.updateById(userCreate);

                                            Balance balance = new Balance();
                                            //生成余额明细
                                            balance.setTitle("购买商品")
                                                    .setPrice(getTotalPriceByOrderType(order))
                                                    .setAddOrSubtractTypeEnum(AddOrSubtractTypeEnum.SUB)
                                                    .setOrderId(order.getId())
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
                                        .getStoreProfit().multiply(getTotalPriceByOrderType(order)));

                                //3. 转账操作
                                //3.1 结算金额转到商家余额

                                Optional.ofNullable(userService.getById(order.getUserId()))
                                        .ifPresent(store -> {

                                            //增加余额
                                            ThreadPoolUtil.getPool().execute(() -> {

                                                user.setBalance(store.getBalance().add(transfer.getAmount()));
                                                userService.updateById(store);
                                                log.info("转账成功, 当前转账金额为" + transfer.getAmount());
                                            });

                                            //记录明细
                                            ThreadPoolUtil.getPool().execute(() -> {

                                                Balance balance = new Balance();
                                                balance.setAddOrSubtractTypeEnum(AddOrSubtractTypeEnum.ADD)
                                                        .setTitle("订单" + order.getOrderNum() + "收入")
                                                        .setPrice(transfer.getAmount())
                                                        .setOrderId(order.getId())
                                                        .setCreateBy(order.getUserId());
                                                balanceService.save(balance);

                                                log.info("记录商家到账金额明细成功！");
                                            });
                                        });

                                //3.2 记录商家收入管理明细
                                // TODO: 2020/5/14 0014  转账成功后才进入到保存收入明细环节,目前暂未做处理
                                StoreIncome storeIncome = new StoreIncome();
                                storeIncome.setIncomeMoney(profitPercentService.getProfitPercentLimitOne(ProfitTypeEnum.FIRST)
                                        .getStoreProfit().multiply(getTotalPriceByOrderType(order)))
                                        .setIncomeType(PayTypeEnum.BALANCE)
                                        .setOrderId(order.getId())
                                        .setCreateBy(order.getUserId());
                                if (ToolUtil.isEmpty(order.getGoodDeposit())) {

                                    storeIncome.setStoreIncomeDesc("全款支付");
                                }else {

                                    storeIncome.setStoreIncomeDesc("支付定金");
                                }

                                storeIncomeService.save(storeIncome);

                            });
                            return new ResultUtil<>().setData(200, "支付成功!");
                        }else {

                            return new ResultUtil<>().setErrorMsg(208, "支付密码不一致！");
                        }
                    }

                      return null;
                }).orElse(new ResultUtil<>().setErrorMsg(209, "参数为空！"));
    }


    /**
     * 根据订单类型计算金额
     * @param order order
     * @return BigDecimal
     */
    @Override
    public BigDecimal getTotalPriceByOrderType(Order order) {

        if (BuyTypeEnum.FULL_PURCHASE.equals(order.getBuyType())) {

            return order.getTotalPrice();
        }else {

            return order.getGoodDeposit();
        }
    }



    @Override
    public Result<Object> confirmReceive(String orderId) {

        return Optional.ofNullable(this.getById(orderId)).map(order -> {

            //1. 更改订单状态
            if (order.getBuyType().equals(BuyTypeEnum.FULL_PURCHASE)) {

                order.setOrderStatus(OrderStatusEnum.PRE_COMMENT);
            }else {

                order.setOrderStatus(OrderStatusEnum.SETTLE_ACCOUNTS);
            }

            this.updateById(order);

            //2. 处理分销逻辑
            resolveOrderTwoProfit(orderId);

            return new ResultUtil<>().setSuccessMsg("确认收获成功！");
        }).orElse(new ResultUtil<>().setErrorMsg(201, "该订单不存在！"));
    }

    /**
     * 支付尾款
     *
     * @param orderOperateDto 实体
     * @return Object
     */
    @Override
    public Result<Object> payTheBalance(OrderOperateDto orderOperateDto) {

      return Optional.ofNullable(this.getById(orderOperateDto.getOrderId()))
                .map(order -> {

                    //1. 支付参数
                    PayModel payModel = new PayModel();
                    payModel.setOrderNums(order.getId());
                    payModel.setTotalPrice(order.getPayGoodBalancePayment());

                    //2. 处理支付
                    //微信
                    if (orderOperateDto.getPayType().equals(PayTypeEnum.WE_CHAT)) {

                        return payService.wxPayMethod(payModel);

                        //支付宝
                    } else if (orderOperateDto.getPayType().equals(PayTypeEnum.ALI_PAY)) {

                        return payService.aliPayMethod(payModel);

                        //余额支付
                    } else if (orderOperateDto.getPayType().equals(PayTypeEnum.BALANCE)) {

                        User user = userService.getById(securityUtil.getCurrUser().getId());

                        if (ToolUtil.isEmpty(user.getPayPassword())) {

                            return new ResultUtil<>().setErrorMsg(206, "暂未设置支付密码");
                        }
                        if (new BCryptPasswordEncoder().matches(orderOperateDto.getPayPassword(), user.getPayPassword())) {

                            if (user.getBalance().compareTo(payModel.getTotalPrice()) < 0) {

                                return new ResultUtil<>().setErrorMsg(207, "余额不足！");
                            }

                            //处理支付尾款逻辑
                            payTheBalanceCallBack(orderOperateDto);

                            return new ResultUtil<>().setData(200, "支付成功!");
                        }else {

                            return new ResultUtil<>().setErrorMsg(208, "支付密码不一致！");
                        }
                    }

                    return new ResultUtil<>().setSuccessMsg("支付尾款成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "当前订单不存在！"));
    }

    @Override
    public void payTheBalanceCallBack(OrderOperateDto orderOperateDto) {


        Optional.ofNullable(this.getById(orderOperateDto.getOrderId())).ifPresent(order -> {

            //1. 更新订单
            order.setRentPayType(orderOperateDto.getPayType())
                    .setRentType(RentTypeEnum.ONLINE_PAY)
                    .setOrderStatus(OrderStatusEnum.SETTLE_RECEIPT);
            this.updateById(order);

            //2. 如果是余额支付减少用户余额
            if (PayTypeEnum.BALANCE.equals(orderOperateDto.getPayType())) {

                Optional.ofNullable(userService.getById(order.getCreateBy()))
                        .ifPresent(userCreate -> {
                            userCreate.setBalance(userCreate.getBalance().subtract(order.getPayGoodBalancePayment()));
                            userService.updateById(userCreate);
                        });
            }

            //3. 记录明细
            Balance balance = new Balance();
            balance.setTitle("支付尾款")
                    .setPrice(order.getPayGoodBalancePayment())
                    .setAddOrSubtractTypeEnum(AddOrSubtractTypeEnum.SUB)
                    .setOrderId(order.getId())
                    .setCreateBy(order.getCreateBy());
            balanceService.save(balance);


            //4. 分佣给商家
            BigDecimal payStore = profitPercentService.getProfitPercentLimitOne(ProfitTypeEnum.FIRST)
                    .getStoreProfit().multiply(order.getPayGoodBalancePayment());

            Optional.ofNullable(userService.getById(order.getUserId()))
                    .ifPresent(user -> {

                        //增加余额
                        ThreadPoolUtil.getPool().execute(() -> {

                            user.setBalance(user.getBalance().add(payStore));
                            userService.updateById(user);
                            log.info("转账成功, 当前转账金额为" + payStore);
                        });

                        //记录明细
                        ThreadPoolUtil.getPool().execute(() -> {

                            Balance payStoreBanlance = new Balance();
                            payStoreBanlance.setAddOrSubtractTypeEnum(AddOrSubtractTypeEnum.ADD)
                                    .setTitle("订单" + order.getOrderNum() + "收入")
                                    .setPrice(payStore)
                                    .setOrderId(order.getId())
                                    .setCreateBy(user.getId());
                            balanceService.save(payStoreBanlance);
                            log.info("记录商家到账金额明细成功！");
                        });

                        //记录商家收入
                        ThreadPoolUtil.getPool().execute(() -> {

                            StoreIncome storeIncome = new StoreIncome();
                            storeIncome.setIncomeMoney(payStore)
                                    .setIncomeType(orderOperateDto.getPayType())
                                    .setOrderId(order.getId())
                                    .setCreateBy(user.getId());

                            storeIncome.setStoreIncomeDesc("支付尾款");

                            storeIncomeService.save(storeIncome);
                            log.info("记录商家收入成功！");
                        });

                    });

            //5. 分佣给平台,个人，推荐人

            //5.1 计算当前订单平台预留的资金
            BigDecimal currentProfitAmount = order.getPayGoodBalancePayment().multiply(profitPercentService
                    .getProfitPercentLimitOne(ProfitTypeEnum.FIRST).getPlatformProfit());

            //5.2 分佣当前消费者的佣金提成
            Optional.ofNullable(userService.getById(order.getCreateBy())).ifPresent(user -> {

                BigDecimal personProfitMoney = currentProfitAmount.multiply(profitPercentService
                        .getProfitPercentLimitOne(ProfitTypeEnum.TWO).getPersonProfit());

                ThreadPoolUtil.getPool().execute(() -> {

                    user.setBalance(user.getBalance().add(personProfitMoney));
                    userService.updateById(user);

                    log.info("消费者分佣成功！当前分佣金额" + personProfitMoney);
                });

                //2.2 记录余额明细
                ThreadPoolUtil.getPool().execute(() -> {

                    Balance customBalance = new Balance();
                    customBalance.setTitle("佣金到账")
                            .setAddOrSubtractTypeEnum(AddOrSubtractTypeEnum.ADD)
                            .setPrice(personProfitMoney)
                            .setOrderId(order.getId())
                            .setCreateBy(order.getCreateBy());
                    balanceService.save(customBalance);

                    log.info("记录消费者分佣明细成功！");
                });

                //2.3 记录分账明细
                ThreadPoolUtil.getPool().execute(() -> {

                    ProfitDetail profitDetail = new ProfitDetail();
                    profitDetail.setOrderId(order.getId())
                            .setPayType(orderOperateDto.getPayType())
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

                            Balance referenceBalance = new Balance();
                            referenceBalance.setTitle("佣金到账")
                                    .setAddOrSubtractTypeEnum(AddOrSubtractTypeEnum.ADD)
                                    .setPrice(personProfitMoney)
                                    .setOrderId(order.getId())
                                    .setCreateBy(user.getId());
                            balanceService.save(referenceBalance);

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

    /**
     * 确认尾款
     *
     * @param orderOperateDto 实体
     * @return Object
     */
    @Override
    public Result<Object> confirmTheBalance(OrderOperateDto orderOperateDto) {

        return Optional.ofNullable(this.getById(orderOperateDto.getOrderId()))
                .map(order -> {

                    order.setOrderStatus(OrderStatusEnum.PRE_COMMENT);

                    this.updateById(order);

                    return new ResultUtil<>().setSuccessMsg("确认尾款成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "该订单不存在！"));

    }

    /**
     * 去评价订单
     *
     * @param commentDTO 实体
     * @return Object
     */
    @Override
    public Result<Object> toEvaluate(CommentDTO commentDTO) {

        return iCommentService.insertOrderUpdateComment(commentDTO);
    }

    /**
     * 取消订单
     *
     * @param orderOperateDto 实体
     * @return Object
     */
    @Override
    public Result<Object> cancelOrder(OrderOperateDto orderOperateDto) {

        return Optional.ofNullable(this.getById(orderOperateDto.getOrderId()))
                .map(order -> {

                    order.setOrderStatus(OrderStatusEnum.CANCEL_ORDER);

                    this.updateById(order);

                    return new ResultUtil<>().setSuccessMsg("取消订单成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "该订单不存在！"));

    }


    @Override
    public Result<DataVo<AppMyOrderListVo>> getMyOrderList(PageVo pageVo, OrderStatusEnum orderStatus) {

        IPage<Order> orderPage = this.page(PageUtil.initMpPage(pageVo), Wrappers.<Order>lambdaQuery()
        .eq(ToolUtil.isNotEmpty(orderStatus), Order::getOrderStatus, orderStatus)
        .eq(Order::getCreateBy, securityUtil.getCurrUser().getId())
        .orderByDesc(Order::getCreateTime));

        if (ToolUtil.isEmpty(orderPage.getRecords())) {

            return new ResultUtil<DataVo<AppMyOrderListVo>>().setErrorMsg(201, "暂无订单数据！");
        }

        DataVo<AppMyOrderListVo> result = new DataVo<>();

        result.setDataResult(orderPage.getRecords().parallelStream().flatMap(order -> {

            AppMyOrderListVo appMyOrderListVo = new AppMyOrderListVo();
            appMyOrderListVo.setOrderDetailVo(orderDetailService.getOrderDetailByOrderId(order.getId()))
                    .setOrderStatusCode(order.getOrderStatus().getCode());
            ToolUtil.copyProperties(order, appMyOrderListVo);

            return Stream.of(appMyOrderListVo);
        }).collect(Collectors.toList())).setTotalPage(orderPage.getPages())
                .setCurrentPageNum(orderPage.getCurrent())
                .setTotalSize(orderPage.getTotal());

        return new ResultUtil<DataVo<AppMyOrderListVo>>().setData(result, "获取我的订单数据成功！");

    }

    /**
     * 获取我的订单详情
     *
     * @param id 订单id
     * @return AppMyOrderDetailVo
     */
    @Override
    public Result<AppMyOrderDetailVo> getMyOrderDetail(String id) {

        return Optional.ofNullable(this.getById(id)).map(order -> {

            AppMyOrderDetailVo appMyOrderDetail = new AppMyOrderDetailVo();
            appMyOrderDetail.setOrderDetailVo(orderDetailService.getOrderDetailByOrderId(order.getId()));
            ToolUtil.copyProperties(order, appMyOrderDetail);
            //尾款方式
            appMyOrderDetail.setOrderStatusCode(order.getOrderStatus().getCode())
                    .setLeaseState(getLeaseState(order));

            return new ResultUtil<AppMyOrderDetailVo>().setData(appMyOrderDetail, "获取我的订单详情成功！");
        }).orElse(new ResultUtil<AppMyOrderDetailVo>().setErrorMsg(201, "当前订单不存在！"));
    }


    public Integer getLeaseState(Order order) {

       RentTypeEnum rentTypeEnum = goodCategoryService.getLeaseState(Optional.ofNullable(orderDetailService.getOrderDetailByOrderId(order.getId()))
                .flatMap(orderDetailVo -> Optional.ofNullable(goodService.getById(orderDetailVo.getGoodId())))
                .map(Good::getGoodCategoryId).orElse(null));

       if (ToolUtil.isEmpty(rentTypeEnum)) {

           return null;
       }else {

           return rentTypeEnum.getCode();
       }
    }


    /**
     * 微信回调
     *
     * @param request request
     * @return String
     */
    @Override
    public String wxPayNotify(HttpServletRequest request) {

        String xmlMsg = HttpKit.readData(request);
        log.warn("微信支付回调消息参数=\n" + xmlMsg);

        Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);
        String returnCode = params.get("return_code");
        String orderNo = params.get("out_trade_no");


        //更新订单状态
        List<Order> orders = this.listByIds(ToolUtil.splitterStr(orderNo));

        orders.parallelStream().forEach(order -> {

            if (ToolUtil.isEmpty(order.getPayTypeEnum())) {

                order.setPayTypeEnum(PayTypeEnum.WE_CHAT);
            }else {

                order.setRentPayType(PayTypeEnum.WE_CHAT);
            }
        });

        // 注意此处签名方式需与统一下单的签名类型一致
        if (WxPayKit.verifyNotify(params, WeChatConfig.KEY, SignType.HMACSHA256)) {
            if (WxPayKit.codeIsOk(returnCode)) {

                // 处理正常支付回调逻辑
                if (ToolUtil.isEmpty(orders.parallelStream().filter(order -> ToolUtil.isNotEmpty(order.getRentPayType()))
                        .collect(Collectors.toList()))) {

                    resolveOrderOneProfit(orders);
                }else {

                    //处理支付尾款支付逻辑
                    OrderOperateDto orderOperateDto = new OrderOperateDto();
                    orderOperateDto.setOrderId(orders.parallelStream().map(Order::getId).collect(Collectors.joining()))
                            .setPayType(PayTypeEnum.WE_CHAT);
                    payTheBalanceCallBack(orderOperateDto);
                }

                // 发送通知等
                Map<String, String> xml = new HashMap<>(2);
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
     * @param request request
     * @return String
     */
    @Override
    public String aliPayNotify(HttpServletRequest request) {

        Map<String, String> params = AliPayUtilTool.toMap(request);
        log.warn("支付宝回调消息参数=\n" + JSONUtil.toJsonPrettyStr(params));

        JSONObject object = new JSONObject();
        try {

            //付款状态
            String tradeStatus = params.get("trade_status");
            String outTradeNo = params.get("out_trade_no");
            String tradeSuccess = "TRADE_SUCCESS";

            if (tradeStatus.equals(tradeSuccess)) {

                //更改支付状态
                List<Order> orders = this.listByIds(ToolUtil.splitterStr(outTradeNo));

                orders.parallelStream().forEach(order -> {

                    if (ToolUtil.isEmpty(order.getPayTypeEnum())) {

                        order.setPayTypeEnum(PayTypeEnum.ALI_PAY);
                    }else {

                        order.setRentPayType(PayTypeEnum.ALI_PAY);
                    }
                });

                // 处理正常支付回调逻辑
                if (ToolUtil.isEmpty(orders.parallelStream().filter(order -> ToolUtil.isNotEmpty(order.getRentPayType()))
                .collect(Collectors.toList()))) {

                    resolveOrderOneProfit(orders);
                }else {

                    //处理支付尾款支付逻辑
                    OrderOperateDto orderOperateDto = new OrderOperateDto();
                    orderOperateDto.setOrderId(orders.parallelStream().map(Order::getId).collect(Collectors.joining()))
                            .setPayType(PayTypeEnum.ALI_PAY);
                    payTheBalanceCallBack(orderOperateDto);
                }

                object.put("return_code", "200");
                object.put("return_msg", "支付宝notify_url 验证成功 succcess");

            } else {

                System.out.println("notify_url 验证失败");
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
    public Result<Object> sendGood(OrderOperateDto orderOperateDto) {

        return Optional.ofNullable(this.getById(orderOperateDto.getOrderId()))
                .map(order -> {

                    order.setExpressCode(orderOperateDto.getExpressCode())
                            .setOrderStatus(OrderStatusEnum.DELIVER_SEND);

                    this.updateById(order);

                    return new ResultUtil<>().setSuccessMsg("发货订单成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "该订单不存在！"));
    }

    @Override
    public Result<DataVo<BackOrderListVO>> getBackOrderList(BackOrderListDto backOrderListDto, PageVo pageVo) {


        //根据筛选条件构建查询分页数据
        Page<Order> orderPage = this.page(PageUtil.initMpPage(pageVo), Wrappers.<Order>lambdaQuery()

         //根据手机号模糊查询
        .like(ToolUtil.isNotEmpty(backOrderListDto.getPhone()), Order::getPhone, backOrderListDto.getPhone())

         //根据订单状态查询
        .eq(ToolUtil.isNotEmpty(backOrderListDto.getOrderStatus()), Order::getOrderStatus, backOrderListDto.getOrderStatus())

         //根据购买状态
        .eq(ToolUtil.isNotEmpty(backOrderListDto.getBuyState()), Order::getBuyType, backOrderListDto.getBuyState())

         //大于支付开始时间
        .ge(ToolUtil.isNotEmpty(backOrderListDto.getPayTimeBeginTime()), Order::getCreateTime, backOrderListDto.getPayTimeBeginTime())

         //小于支付结束时间
        .le(ToolUtil.isNotEmpty(backOrderListDto.getPayTimeEndTime()), Order::getCreateTime, backOrderListDto.getPayTimeEndTime())

         //管理员和商家筛选查询
        .eq(ToolUtil.isNotEmpty(backOrderListDto.getStoreId()), Order::getUserId, backOrderListDto.getStoreId())

         //默认按照订单创建时间排序
        .orderByDesc(Order::getCreateTime));


        //封装数据

        //1. 判断数据是否为空
        if (ToolUtil.isEmpty(orderPage.getRecords())) {

            return new ResultUtil<DataVo<BackOrderListVO>>().setErrorMsg(201, "暂无数据！");
        }


        //2. 封装数据
        DataVo<BackOrderListVO> result = new DataVo<>();

        result.setDataResult(orderPage.getRecords().parallelStream().flatMap(order -> {

            BackOrderListVO backOrderListVO = new BackOrderListVO();

            //2.1 所属店铺
            backOrderListVO.setStoreName(Optional.ofNullable(userService.getById(order.getUserId()))
            .map(User::getNickName).orElse("-"));

            // TODO: 2020/5/28 运费金额
            backOrderListVO.setFreightPrice(new BigDecimal(0));

            ToolUtil.copyProperties(order, backOrderListVO);

            return Stream.of(backOrderListVO);
        }).collect(Collectors.toList())).setTotalSize(orderPage.getSize())
                .setCurrentPageNum(orderPage.getCurrent())
                .setTotalPage(orderPage.getTotal());

        //3. 筛选店铺
        if (ToolUtil.isNotEmpty(backOrderListDto.getStoreName())) {

            result.setDataResult(result.getDataResult().parallelStream().filter(backOrderListVO ->
                    backOrderListVO.getStoreName().contains(backOrderListDto.getStoreName()))
            .collect(Collectors.toList())).setTotalSize((long) result.getDataResult().size());
        }

        return new ResultUtil<DataVo<BackOrderListVO>>().setData(result, "获取订单列表数据成功！");
    }


    /**
     * 处理订单回调逻辑
     * @param orders 订单集合
     */
    private void resolveOrderOneProfit(List<Order> orders) {


        orders.parallelStream().forEach(order -> {

            //1. 处理订单状态

            if (order.getOrderStatus().equals(OrderStatusEnum.PRE_PAY)) {

                order.setOrderStatus(OrderStatusEnum.PRE_SEND);

            }else if (order.getOrderStatus().equals(OrderStatusEnum.SETTLE_ACCOUNTS)) {

                order.setOrderStatus(OrderStatusEnum.SETTLE_RECEIPT);
            }

            this.updateById(order);

            //2. 平台按照比例转账到商家支付宝账户

            TransferDto transferDto = new TransferDto();

            //2.1 商家的支付宝账户
            transferDto.setAliPayAcount(Optional.ofNullable(userService.getById(order.getUserId()))
            .map(User::getAlipayAccount).orElse(null));

            //2.2 需要支付给商家的钱款
            transferDto.setAmount(profitPercentService.getProfitPercentLimitOne(ProfitTypeEnum.FIRST)
            .getStoreProfit().multiply(getTotalPriceByOrderType(order)));

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
                                    .setOrderId(order.getId())
                                    .setCreateBy(order.getUserId());
                            balanceService.save(balance);

                            log.info("记录商家到账金额明细成功！");
                        });
                    });

            //3.2 记录商家收入管理明细
            // TODO: 2020/5/14 0014  转账成功后才进入到保存收入明细环节,目前暂未做处理
            StoreIncome storeIncome = new StoreIncome();
            storeIncome.setIncomeMoney(profitPercentService.getProfitPercentLimitOne(ProfitTypeEnum.FIRST)
                    .getStoreProfit().multiply(getTotalPriceByOrderType(order)))
                    .setIncomeType(PayTypeEnum.ALI_PAY)
                    .setOrderId(order.getId())
                    .setCreateBy(order.getUserId());

            if (ToolUtil.isEmpty(order.getGoodDeposit())) {

                storeIncome.setStoreIncomeDesc("全款支付");
            }else {

                storeIncome.setStoreIncomeDesc("支付定金");
            }

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
                              .setOrderId(orderId)
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
                                    .setOrderId(orderId)
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


    @Override
    public Result<Object> insertOffLineOrder(OffLineOrderDto offLineOrderDto) {


        Order order = new Order();

        order.setTypeEnum(OrderTypeEnum.OFFLINE_ORDER)
                .setUserId(Optional.ofNullable(staffManagementService.getOne(Wrappers.<StaffManagement>lambdaQuery()
                .eq(StaffManagement::getStaffId, offLineOrderDto.getStaffId()))).map(StaffManagement::getCreateBy).orElse(null))
                .setGoodDesc(offLineOrderDto.getGoodDesc())
                .setGoodDeposit(offLineOrderDto.getGoodDeposit())
                .setRentType(offLineOrderDto.getRentTypeEnum())
                .setPayGoodBalancePayment(offLineOrderDto.getTotalPrice().subtract(offLineOrderDto.getGoodDeposit()))
                .setCreateBy(securityUtil.getCurrUser().getId());

        ToolUtil.copyProperties(offLineOrderDto, order);

        this.save(order);

        Map<String, Object> map = new ArrayMap<>();
        map.put("id", order.getId());
        map.put("totalPrice", order.getTotalPrice());
        return new ResultUtil<>().setData(map, "插入或者更新成功!");

    }

    @Override
    public Result<Object> insertOffLinePayTheBalanceOrder(String staffId, String orderNum) {

        //1. 判断当前订单是否在本店操作
       return Optional.ofNullable(this.getOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNum, orderNum)))
                .map(order -> {

                    if (order.getUserId().equals(staffId) || order.getUserId().equals(userService
                    .getById(staffId).getCreateBy())) {

                        return new ResultUtil<>().setData(order.getId(), "获取订单id成功！");
                    }else {

                        return new ResultUtil<>().setErrorMsg(205, "只有当前门店才能操作！");
                    }
                }).orElse(new ResultUtil<>().setErrorMsg(206, "当前订单不存在！"));
    }


    @Override
    public BigDecimal getStaffSaleAmount(String staffId) {

        return Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<Order>lambdaQuery()
        .eq(Order::getStaffId, staffId))))
        .map(orders -> orders.parallelStream().map(Order::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add))
        .orElse(new BigDecimal(0));
    }


    @Override
    public Boolean judgeOrderFreeze(String orderId) {

        return Optional.ofNullable(this.getById(orderId))
                .map(order -> DateUtil.offsetDay(order.getCreateTime(),
                        icommonParamService.getCommonParamVo()
                .getFreezeOrderTime()).getTime() >= DateUtil.date().getTime())
                .orElse(false);
    }

    @Override
    public List<Order> getOrderListByStoreId(String storeId) {

        return Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<Order>lambdaQuery()
        .eq(Order::getUserId, storeId)
        .orderByDesc(Order::getCreateTime))))
        .orElse(null);
    }


}