package cn.ruanyun.backInterface.modules.business.good.serviceimpl;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import cn.ruanyun.backInterface.common.utils.EmptyUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.business.comment.service.ICommentService;
import cn.ruanyun.backInterface.modules.business.discountCoupon.service.IDiscountCouponService;
import cn.ruanyun.backInterface.modules.business.followAttention.pojo.FollowAttention;
import cn.ruanyun.backInterface.modules.business.followAttention.service.IFollowAttentionService;
import cn.ruanyun.backInterface.modules.business.good.DTO.GoodDTO;
import cn.ruanyun.backInterface.modules.business.good.VO.*;
import cn.ruanyun.backInterface.modules.business.good.mapper.GoodMapper;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import cn.ruanyun.backInterface.modules.business.goodCategory.mapper.GoodCategoryMapper;
import cn.ruanyun.backInterface.modules.business.goodService.service.IGoodServiceService;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.service.IItemAttrValService;
import cn.ruanyun.backInterface.modules.business.myFavorite.service.IMyFavoriteService;
import cn.ruanyun.backInterface.modules.business.myFootprint.pojo.MyFootprint;
import cn.ruanyun.backInterface.modules.business.myFootprint.serviceimpl.IMyFootprintServiceImpl;
import cn.ruanyun.backInterface.modules.business.orderDetail.service.IOrderDetailService;
import cn.ruanyun.backInterface.modules.business.shoppingCart.service.IShoppingCartService;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.pojo.SizeAndRolor;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.service.ISizeAndRolorService;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 商品接口实现
 * @author fei
 */
@Slf4j
@Service
public class IGoodServiceImpl extends ServiceImpl<GoodMapper, Good> implements IGoodService {


    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private IUserService userService;

    @Autowired
    private IMyFootprintServiceImpl iMyFootprintService;

    @Resource
    private GoodCategoryMapper goodCategoryMapper;

    @Autowired
    private IShoppingCartService iShoppingCartService;

    @Autowired
    private IFollowAttentionService iFollowAttentionService;

    @Autowired
    private IMyFavoriteService iMyFavoriteService;

    @Autowired
    private IDiscountCouponService iDiscountCouponService;

    @Autowired
    private IGoodServiceService iGoodServiceService;

    @Autowired
    private IItemAttrValService iItemAttrValService;

    @Autowired
    private IOrderDetailService orderDetailService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private ISizeAndRolorService sizeAndRolorService;


    @Override
    public void insertOrderUpdateGood(Good good) {

        if (ToolUtil.isEmpty(good.getCreateBy())) {

            good.setCreateBy(securityUtil.getCurrUser().getId());
        }else {

            good.setUpdateBy(securityUtil.getCurrUser().getId());
        }


        Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(good)))
                .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                .toFuture().join();
    }

    @Override
    public void removeGood(String ids) {

        CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
    }

    /**
     * 封装类，获取商品列表字段
     *
     * @param id
     * @return
     */
    @Override
    public AppGoodListVO getAppGoodListVO(String id) {

        return Optional.ofNullable(this.getById(id))
                .map(good -> {
                    AppGoodListVO appGoodListVO = new AppGoodListVO();

                    //1.店铺信息
                    Optional.ofNullable(userService.getById(good.getCreateBy()))
                            .ifPresent(user -> ToolUtil.copyProperties(user, appGoodListVO));

                    //2.商品信息
                    ToolUtil.copyProperties(good,appGoodListVO);

                    appGoodListVO.setGoodPic(Optional.ofNullable(ToolUtil.setListToNul(ToolUtil.splitterStr(good.getGoodPics())))
                            .map(pics -> pics.get(0))
                            .orElse("暂无"));

                    // TODO: 2020/3/27 其他信息
                    appGoodListVO.setSaleVolume(orderDetailService.getGoodSalesVolume(good.getId()))
                            .setCommentNum(Optional.ofNullable(commentService.getCommentVOByGoodId(good.getId()))
                            .map(List::size)
                            .orElse(0));

                    return appGoodListVO;

                }).orElse(null);
    }



    /**
     * 获取商品列表
     *
     * @param goodDTO
     * @return
     */
    @Override
    public List<AppGoodListVO> getAppGoodList(GoodDTO goodDTO) {

        //1先查询封装之前的类
        CompletableFuture<Optional<List<Good>>> goodsList = CompletableFuture.supplyAsync(() ->
                Optional.ofNullable(getGoodList(goodDTO)));

        //2.查询封装之后的类
        CompletableFuture<Optional<List<AppGoodListVO>>> goodsVOList = goodsList.thenApplyAsync(goods ->
                goods.map(goods1 -> goods1.parallelStream().flatMap(good -> Stream.of(getAppGoodListVO(good.getId())))
                        .collect(Collectors.toList())));

        //3.筛选条件
        CompletableFuture<List<AppGoodListVO>> goodsVOFilterList = goodsVOList.thenApplyAsync(goodListVOS ->
                goodListVOS.map(goodListVOS1 -> {
                    //查询筛选条件
                    return Optional.ofNullable(goodDTO.getFilterCondition()).map(code -> {

                        //1.销量升降序
                        if (CommonConstant.SALES_VOLUME_ASC.equals(code)) {

                            return goodListVOS1.parallelStream().sorted(Comparator.comparing(AppGoodListVO::getSaleVolume))
                                    .collect(Collectors.toList());
                        }else if (CommonConstant.SALES_VOLUME_DESC.equals(code)) {

                            return goodListVOS1.parallelStream().sorted(Comparator.comparing(AppGoodListVO::getSaleVolume)
                                    .reversed())
                                    .collect(Collectors.toList());
                        }else if (CommonConstant.PRICE_ASC.equals(code)) {
                            //价格升降序
                            return goodListVOS1.parallelStream().sorted(Comparator.comparing(AppGoodListVO::getGoodNewPrice))
                                    .collect(Collectors.toList());
                        }else if (CommonConstant.PRICE_DESC.equals(code)) {

                            return goodListVOS1.parallelStream().sorted(Comparator.comparing(AppGoodListVO::getGoodNewPrice)
                                    .reversed())
                                    .collect(Collectors.toList());
                        }else if (CommonConstant.COMMENTS_NUM_ASC.equals(code)) {

                            //3.评论数升降序
                            return null;

                        }else {

                            return null;
                        }
                    }).orElse(goodListVOS1);

                }).orElse(null));

        return goodsVOFilterList.join();
    }


    /**
     * 获取基本数据列表
     * @param goodDTO
     * @return
     */
    public List<Good> getGoodList(GoodDTO goodDTO) {

        // 1.默认条件构造器
        LambdaQueryWrapper<Good> wrappers = Wrappers.<Good>lambdaQuery()
                .eq(!StringUtils.isEmpty(goodDTO.getGoodCategoryId()),Good::getGoodCategoryId,goodDTO.getGoodCategoryId())
                .eq(!EmptyUtil.isEmpty(goodDTO.getGoodTypeEnum()),Good::getTypeEnum, goodDTO.getGoodTypeEnum())
                .eq(!StringUtils.isEmpty(goodDTO.getStoreId()),Good::getCreateBy, goodDTO.getStoreId());

        //2.筛选条件
        if (ToolUtil.isNotEmpty(goodDTO
                .getPriceHigh()) && ToolUtil.isNotEmpty(goodDTO.getPriceLow())) {

            //2.1只查询价格区间的
            wrappers.lt(Good::getGoodNewPrice,goodDTO.getPriceHigh())
                    .gt(Good::getGoodNewPrice,goodDTO.getPriceLow());

        }
        return ToolUtil.setListToNul(this.list(wrappers));

    }


    /**
     * 获取商品详情
     *
     * @param id
     * @return
     */
    @Override
    public AppGoodDetailVO getAppGoodDetail(String id) {

        return Optional.ofNullable(this.getById(id)).map(good -> {

            AppGoodDetailVO goodDetailVO = new AppGoodDetailVO();
            ToolUtil.copyProperties(good,goodDetailVO);

            goodDetailVO.setGoodDetails(Optional.ofNullable(
                    good.getGoodDetails()).orElse(null));

            //TODO: 店铺数据
            AppShopVO shopList = new AppShopVO();
            // TODO: 店铺图片
            shopList.setShopPic(userService.getUserIdByUserPic(good.getCreateBy()))
            // TODO: 店铺id
            .setShopId(Optional.ofNullable(good.getCreateBy())
                            .orElse(null))
            // TODO: 店铺名称
            .setShopName(userService.getUserIdByUserName(good.getCreateBy()))
            //TODO: 商品数量
            .setGoodsNum(this.list(Wrappers.<Good>lambdaQuery()
                            .eq(Good::getCreateBy, good.getCreateBy())).size())
            // TODO: 关注店铺人数
            .setFollowAttentionNum(iFollowAttentionService.list(Wrappers.<FollowAttention>lambdaQuery()
                         .eq(FollowAttention::getUserId, good.getCreateBy())).size())
            //TODO: 评论数量
//            .setCommonNum(0)
            ;

            //TODO: 用户的购物车数量
            goodDetailVO.setGoodsCartNum(iShoppingCartService.getGoodsCartNum())
                    //TODO: 店铺数据
                    .setShopList(shopList)
                    //TODO: 是否收藏0否 1收藏
                    .setFavorite(iMyFavoriteService.getMyFavorite(id,GoodTypeEnum.GOOD))
                    //TODO: 商品优惠券
                    .setDiscountCouponListVOS(iDiscountCouponService.getDiscountCouponListByGoodsPackageId(id))
                    //TODO: 商品服务类型
                    .setGoodsService(iGoodServiceService.getGoodsServiceList(id));

            //用户浏览商品足迹
            MyFootprint myFootprint = new MyFootprint();
            myFootprint.setGoodsId(good.getId());
            iMyFootprintService.insertOrderUpdateMyFootprint(myFootprint);

            return goodDetailVO;
        }).orElse(null);
    }

    /******************************************首页一级分类下的商品列表---开始**************************************************/

    /**
     * 获取一级分类下的所有商品和套餐以及按地区查询
     * @return
     */
    @Override
    public List<AppGoodAndPackageListVO> getAppGoodAndPackageList(GoodDTO goodDTO) {
        return null;
    }

    /******************************************首页一级分类下的商品列表---结束**************************************************/

    @Override
    public AppGoodInfoVO getAppGoodInfo(String id) {


        return Optional.ofNullable(super.getById(id))
                .map(good -> {

                    AppGoodInfoVO appGoodInfoVO = new AppGoodInfoVO();
                    ToolUtil.copyProperties(good, appGoodInfoVO);

//                    //1.商品图片
//                    appGoodInfoVO.setGoodPic(Optional.ofNullable(ToolUtil.setListToNul(ToolUtil.splitterStr(good.getGoodPics())))
//                            .map(pics -> pics.get(0))
//                            .orElse("暂无"));
//
//                    //2.商品颜色尺寸
//                    appGoodInfoVO.setColors(colorService.getColorInfoVO(good.getColorIds()))
//                            .setSizes(sizeService.getSizeVoByIds(good.getSizeIds()));

                    return appGoodInfoVO;
                }).orElse(null);
    }


    @Override
    public String getPicLimit1(String id) {

        return Optional.ofNullable(this.getById(id)).map(good -> ToolUtil.splitterStr(good.getGoodPics()).get(0))
                .orElse(null);
    }


    /**
     * 获取商品名称
     * @param id
     * @return
     */
    @Override
    public String getGoodName(String id) {

        return Optional.ofNullable(this.getById(id)).map(Good::getGoodName)
                .orElse("组合商品");
    }

    /**
     * 获取商品积分
     * @param id
     * @return
     */
    @Override
    public Integer getGoodIntegral(String id) {

        return Optional.ofNullable(this.getById(id)).map(Good::getIntegral)
                .orElse(0);
    }

    /**
     * 获取商品单价
     * @param id
     * @return
     */
    @Override
    public BigDecimal getGoodPrice(String id) {

        return Optional.ofNullable(this.getById(id)).map(Good::getGoodNewPrice)
                .orElse(BigDecimal.valueOf(0));
    }

    /**
     * 获取商品库存
     * @param id
     * @return
     */
    @Override
    public Integer getInventory(String id) {

//        return Optional.ofNullable(this.getById(id)).map(Good::getInventory)
//                .orElse(0);
        return null;
    }

    /**
     * 获取商品购买信息
     *
     * @param id
     * @return
     */
    @Override
    public AppGoodOrderVO getAppGoodOrder(String id,String attrSymbolPath) {

        return Optional.ofNullable(super.getById(id))
                .map(good -> {
                    AppGoodOrderVO appGoodOrderVO = new AppGoodOrderVO();
                    ToolUtil.copyProperties(good, appGoodOrderVO);
                    appGoodOrderVO.setGoodId(good.getId());
                    //1.商品图片
                    appGoodOrderVO.setGoodPic(Optional.ofNullable(ToolUtil.setListToNul(ToolUtil.splitterStr(good.getGoodPics())))
                            .map(pics -> pics.get(0))
                            .orElse("暂无"));

                    //处理商品价格
                    SizeAndRolor one = sizeAndRolorService.getOne(Wrappers.<SizeAndRolor>lambdaQuery().eq(SizeAndRolor::getAttrSymbolPath, attrSymbolPath));
                    if (EmptyUtil.isNotEmpty(one)){
                        appGoodOrderVO.setGoodNewPrice(one.getGoodPrice());
                        appGoodOrderVO.setGoodPic(one.getPic());
                    }

                    //2.商品颜色尺寸
                    List<String> itemAttrVals = iItemAttrValService.getItemAttrVals(attrSymbolPath);
                    appGoodOrderVO.setItemAttrKeys(itemAttrVals);
                    appGoodOrderVO.setAttrSymbolPath(attrSymbolPath);
                    return appGoodOrderVO;
                }).orElse(null);
    }

    /************************************************PC端******************************************************/

    @Override
    public List<PcGoodListVO> PCgoodsList(){
        List<Good> list = this.list(new QueryWrapper<Good>().lambda()
                .eq(Good::getCreateBy, securityUtil.getCurrUser().getId()));
        List<PcGoodListVO> pcGoodList = list.parallelStream().map(pcGoods->{
            PcGoodListVO pc = new PcGoodListVO();
            String goodCategory = goodCategoryMapper.selectById(pcGoods.getGoodCategoryId()).getTitle();
            pc.setGoodCategoryName(goodCategory);

            ToolUtil.copyProperties(pcGoods , pc);
            return  pc;
        }).collect(Collectors.toList());

        return pcGoodList;
    }

    @Override
    public  List<AppForSaleGoodsVO> getAppForSaleGoods(String ids) {
        //查询商家创建的商品
        List<Good> good =this.list(new QueryWrapper<Good>().lambda().eq(Good::getCreateBy,ids));

        List<AppForSaleGoodsVO> forSaleGoodsVO = new ArrayList<>();
         for (Good g : good) {
             AppForSaleGoodsVO appForSaleGoodsVO  = new AppForSaleGoodsVO();
             ToolUtil.copyProperties(g,appForSaleGoodsVO);
             appForSaleGoodsVO.setGoodPics(g.getGoodPics().split(",")[0]);
             forSaleGoodsVO.add(appForSaleGoodsVO);
        }
        return forSaleGoodsVO;
    }




}