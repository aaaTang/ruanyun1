package cn.ruanyun.backInterface.modules.business.good.serviceimpl;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.CheckEnum;
import cn.ruanyun.backInterface.common.enums.FollowTypeEnum;
import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import cn.ruanyun.backInterface.common.enums.SearchTypesEnum;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.RoleMapper;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserRoleMapper;
import cn.ruanyun.backInterface.modules.base.pojo.Role;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.pojo.UserRole;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.business.area.service.IAreaService;
import cn.ruanyun.backInterface.modules.business.comment.service.ICommentService;
import cn.ruanyun.backInterface.modules.business.discountCoupon.VO.DiscountCouponListVO;
import cn.ruanyun.backInterface.modules.business.discountCoupon.service.IDiscountCouponService;
import cn.ruanyun.backInterface.modules.business.followAttention.pojo.FollowAttention;
import cn.ruanyun.backInterface.modules.business.followAttention.service.IFollowAttentionService;
import cn.ruanyun.backInterface.modules.business.good.DTO.GoodDTO;
import cn.ruanyun.backInterface.modules.business.good.VO.*;
import cn.ruanyun.backInterface.modules.business.good.mapper.GoodMapper;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import cn.ruanyun.backInterface.modules.business.goodCategory.entity.GoodCategory;
import cn.ruanyun.backInterface.modules.business.goodCategory.mapper.GoodCategoryMapper;
import cn.ruanyun.backInterface.modules.business.goodCategory.service.IGoodCategoryService;
import cn.ruanyun.backInterface.modules.business.goodService.service.IGoodServiceService;
import cn.ruanyun.backInterface.modules.business.goodsIntroduce.service.IGoodsIntroduceService;
import cn.ruanyun.backInterface.modules.business.goodsPackage.service.IGoodsPackageService;
import cn.ruanyun.backInterface.modules.business.grade.service.IGradeService;
import cn.ruanyun.backInterface.modules.business.itemAttrKey.mapper.ItemAttrKeyMapper;
import cn.ruanyun.backInterface.modules.business.itemAttrKey.pojo.ItemAttrKey;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.service.IItemAttrValService;
import cn.ruanyun.backInterface.modules.business.myFavorite.service.IMyFavoriteService;
import cn.ruanyun.backInterface.modules.business.myFootprint.pojo.MyFootprint;
import cn.ruanyun.backInterface.modules.business.myFootprint.serviceimpl.IMyFootprintServiceImpl;
import cn.ruanyun.backInterface.modules.business.orderDetail.service.IOrderDetailService;
import cn.ruanyun.backInterface.modules.business.recommendedPackage.pojo.RecommendedPackage;
import cn.ruanyun.backInterface.modules.business.recommendedPackage.service.IRecommendedPackageService;
import cn.ruanyun.backInterface.modules.business.searchHistory.pojo.SearchHistory;
import cn.ruanyun.backInterface.modules.business.searchHistory.service.ISearchHistoryService;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.mapper.SizeAndRolorMapper;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.pojo.SizeAndRolor;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.service.ISizeAndRolorService;
import cn.ruanyun.backInterface.modules.business.storeActivity.service.IStoreActivityService;
import cn.ruanyun.backInterface.modules.business.storeFirstRateService.service.IstoreFirstRateServiceService;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Resource;
import javax.swing.*;
import java.math.BigDecimal;
import java.util.*;
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
    @Resource
    private UserMapper userMapper;
    @Autowired
    private IMyFootprintServiceImpl iMyFootprintService;
    @Resource
    private GoodCategoryMapper goodCategoryMapper;
    @Autowired
    private ISearchHistoryService iSearchHistoryService;
    @Autowired
    private IFollowAttentionService iFollowAttentionService;
    @Autowired
    private IMyFavoriteService iMyFavoriteService;
    @Autowired
    private IDiscountCouponService iDiscountCouponService;
    @Autowired
    private IGoodServiceService iGoodServiceService;
    @Autowired
    private IGoodsPackageService iGoodsPackageService;
    @Autowired
    private IItemAttrValService iItemAttrValService;
    @Autowired
    private IOrderDetailService orderDetailService;
    @Autowired
    private ICommentService commentService;
    @Autowired
    private ISizeAndRolorService sizeAndRolorService;
    @Resource
    private SizeAndRolorMapper sizeAndRolorMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private RoleMapper roleMapper;
    @Autowired
    private IGradeService gradeService;
    @Resource
    private GoodMapper goodMapper;
    @Resource
    private IGoodsIntroduceService iGoodsIntroduceService;
    @Autowired
    private IGoodCategoryService goodCategoryService;
    @Autowired
    private IRecommendedPackageService iRecommendedPackageService;

    @Autowired
    private IAreaService areaService;

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
    public Result<Object> removeGood(String  id) {

        return Optional.ofNullable(super.getById(id))
                .map(good1 -> {
                    String result = "";
                    if(good1.getDelFlag().equals(1)){
                        good1.setDelFlag(0);
                        result="解除下架！";
                    }else if (good1.getDelFlag().equals(0)){
                        good1.setDelFlag(CommonConstant.DEL_FLAG);
                        result="下架成功！";
                    }
                    super.updateById(good1);

                    return new ResultUtil<>().setSuccessMsg(result);
                }).orElse(new ResultUtil<>().setErrorMsg(201, "不商品不存在！"));

    }

    /**
     * 封装类，获取商品列表字段
     *
     * @param id
     * @return
     */
    @Override
    public AppGoodListVO getAppGoodListVO(String id ,String goodCategoryId) {

        return Optional.ofNullable(this.getOne(new QueryWrapper<Good>().lambda().eq(Good::getId,id).eq(ToolUtil.isNotEmpty(goodCategoryId),Good::getGoodCategoryId,goodCategoryId)))
                .map(good -> {
                    AppGoodListVO appGoodListVO = new AppGoodListVO();

                    //1.店铺信息
                    Optional.ofNullable(userService.getById(good.getCreateBy()))
                            .ifPresent(user -> ToolUtil.copyProperties(user, appGoodListVO.setUserId(user.getId()))
                            );

                    //2.商品信息
                    ToolUtil.copyProperties(good,appGoodListVO);
                    String[] pic =  good.getGoodPics().split(",");
                        if(ToolUtil.isNotEmpty(pic)){
                            appGoodListVO.setGoodPic(pic[0]);
                        }

                    // TODO: 2020/3/27 其他信息
                    appGoodListVO.setSaleVolume(orderDetailService.getGoodSalesVolume(good.getId()))
                            .setGrade(Double.parseDouble(commentService.getGoodScore(good.getId())))
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
                goods.map(goods1 -> goods1.parallelStream().flatMap(good -> Stream.of(getAppGoodListVO(good.getId(),null)))
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

        if(ToolUtil.isEmpty(goodDTO.getAreaId())){
            // 1.默认条件构造器
            LambdaQueryWrapper<Good> wrappers =  Wrappers.<Good>lambdaQuery()
                    .in(ToolUtil.isNotEmpty(getGoodCategoryIdByParentId(goodDTO.getGoodCategoryId())),Good::getGoodCategoryId, getGoodCategoryIdByParentId(goodDTO.getGoodCategoryId()))
                    .eq(!EmptyUtil.isEmpty(goodDTO.getGoodTypeEnum()),Good::getTypeEnum, goodDTO.getGoodTypeEnum())
                    .eq(!StringUtils.isEmpty(goodDTO.getStoreId()),Good::getCreateBy, goodDTO.getStoreId())
                    .eq(Good::getDelFlag,CommonConstant.STATUS_NORMAL);

            //2.筛选条件
            if (ToolUtil.isNotEmpty(goodDTO
                    .getPriceHigh()) && ToolUtil.isNotEmpty(goodDTO.getPriceLow())) {

                //2.1只查询价格区间的
                wrappers.lt(Good::getGoodNewPrice,goodDTO.getPriceHigh())
                        .gt(Good::getGoodNewPrice,goodDTO.getPriceLow());

            }
            return ToolUtil.setListToNul(this.list(wrappers));

        }else if(ToolUtil.isNotEmpty(goodDTO.getAreaId())){
            //首先获取商家和个人商家
                //1查询角色表是商家和个人商家的所有数据
            List<Role> roles = roleMapper.selectList(Wrappers.<Role>lambdaQuery()
                    .eq(Role::getName,CommonConstant.STORE).or().eq(Role::getName,CommonConstant.PER_STORE));

            List<UserRole> userRoles= new ArrayList<>();
            for (Role role : roles) {
                //2.获取角色权限表的用户id
                List<UserRole> selectList = userRoleMapper.selectList(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getRoleId, role.getId()));
                for (UserRole userRole : selectList) { userRoles.add(userRole);}}

            List<User> userList = new ArrayList<>();
            for (UserRole userRole : userRoles) {
                //查询区域的商家或者店铺
                User u = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getId,userRole.getUserId()).eq(User::getAreaId,goodDTO.getAreaId()));
                if(ToolUtil.isNotEmpty(u)){ userList.add(u);}
            }

            List<Good> goods = new ArrayList<>();
            for (User user : userList) {
                List<Good> g = goodMapper.selectList(new QueryWrapper<Good>().lambda()
                        .in(ToolUtil.isNotEmpty(getGoodCategoryIdByParentId(goodDTO.getGoodCategoryId())),Good::getGoodCategoryId, getGoodCategoryIdByParentId(goodDTO.getGoodCategoryId()))
                        .eq(!EmptyUtil.isEmpty(goodDTO.getGoodTypeEnum()),Good::getTypeEnum, goodDTO.getGoodTypeEnum())
                        .eq(Good::getCreateBy, user.getId())
                        .eq(Good::getDelFlag, CommonConstant.STATUS_NORMAL)
                        .lt(ToolUtil.isNotEmpty(goodDTO.getPriceHigh())&&ToolUtil.isNotEmpty(goodDTO.getPriceLow()),Good::getGoodNewPrice,goodDTO.getPriceHigh())
                        .gt(ToolUtil.isNotEmpty(goodDTO.getPriceHigh())&&ToolUtil.isNotEmpty(goodDTO.getPriceLow()),Good::getGoodNewPrice,goodDTO.getPriceLow()));
                goods.addAll(g);
            }
            return Optional.ofNullable(goods).orElse(null);

        }


        return null;

    }


    /**
     * 查询当前id下面的所有子集分类
     * @param goodCategoryId 父类id
     * @return 分类集合
     */
    public Set<String> getGoodCategoryIdByParentId(String goodCategoryId) {

        Set<String> categoryIds = Sets.newHashSet();

        categoryIds.add(goodCategoryId);

        Optional.ofNullable(ToolUtil.setListToNul(goodCategoryService.list(Wrappers.<GoodCategory>lambdaQuery()
                .eq(GoodCategory::getParentId, goodCategoryId))))
                .ifPresent(goodCategories -> {

                    categoryIds.addAll(goodCategories.parallelStream().map(GoodCategory::getId)
                            .collect(Collectors.toSet()));

                    goodCategories.parallelStream().forEach(goodCategory -> categoryIds.addAll(getGoodCategoryIdByParentId(goodCategory.getId())));

                });
        return categoryIds;
    }


    /**
     * 获取商品详情
     *
     * @param id
     * @return
     */
    @Override
    public AppGoodDetailVO getAppGoodDetail(String id) {

        String principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

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
            //TODO: 店铺名称
            .setShopName(userService.getUserIdByUserName(good.getCreateBy()))
            //TODO: 商品数量
            .setGoodsNum(this.list(Wrappers.<Good>lambdaQuery()
                            .eq(Good::getCreateBy, good.getCreateBy()).eq(Good::getTypeEnum,GoodTypeEnum.GOOD)).size())
            // TODO: 关注店铺人数
            .setFollowAttentionNum(iFollowAttentionService.list(Wrappers.<FollowAttention>lambdaQuery()
                         .eq(FollowAttention::getUserId, good.getCreateBy())).size())
            //TODO: 评论数量
//            .setCommonNum(0)
            ;

            GoodCategory goodCategory = goodCategoryMapper.selectById(good.getGoodCategoryId());

            //TODO: 店铺数据
            goodDetailVO.setShopList(shopList)
                    //TODO: 商品优惠券
                    .setDiscountCouponListVOS(iDiscountCouponService.getDiscountCouponListByGoodsPackageId(id))
                    //TODO: 商品服务类型
                    .setGoodsService(iGoodServiceService.getGoodsServiceList(id))
                    //购买状态 1购买 2租赁
                    .setBuyState(Optional.ofNullable(goodCategory).map(GoodCategory::getBuyState).orElse(null))
                    //租赁状态 1尾款线上支付  2尾款线下支付
                    .setLeaseState(Optional.ofNullable(goodCategory).map(GoodCategory::getLeaseState).orElse(null));


            if(!"anonymousUser".equals(principal)) {

                //是否收藏
                goodDetailVO.setFavorite(iMyFavoriteService.getMyFavorite(id,GoodTypeEnum.GOOD));

                //用户浏览商品足迹
                MyFootprint myFootprint = new MyFootprint();
                myFootprint.setGoodsId(good.getId());
                iMyFootprintService.insertOrderUpdateMyFootprint(myFootprint);
            }

            //规格状态  0空   1有
           List<SizeAndRolor>  sizeAndRolors = Optional.ofNullable(ToolUtil.setListToNul(sizeAndRolorMapper.selectList(new QueryWrapper<SizeAndRolor>().lambda()
                    .eq(SizeAndRolor::getGoodsId,good.getId())))).orElse(null);

            if(ToolUtil.isNotEmpty(sizeAndRolors)){
                goodDetailVO.setSpecificationState(1);
            }else {
                goodDetailVO.setSpecificationState(0);
            }


            //是否是四大金刚  0否   1是
            Optional.ofNullable(Optional.ofNullable(goodCategoryMapper.selectById(goodCategory.getParentId())).orElse(null)).ifPresent(goodCategory1 -> {
               if("四大金刚".equals(goodCategory1.getTitle())){
                   goodDetailVO.setDevarajas(1);
               }else {
                   goodDetailVO.setDevarajas(0);
               }
            });

            return goodDetailVO;
        }).orElse(null);
    }


    /**
     * 获取首页一级分类下的所有商品
     * @return
     */
    @Override
    public List<AppOneClassGoodListVO> getAppOneClassGoodList(String classId,String areaName) {


      List<GoodCategory> goodCategoryList = goodCategoryMapper.selectList(new QueryWrapper<GoodCategory>().lambda().eq(GoodCategory::getStatus,0));

      List<GoodCategory> goodCategories = getByMapCategory(classId,goodCategoryList);

      List<AppOneClassGoodListVO> list = new ArrayList<>();

        for (GoodCategory goodCategory : goodCategories) {

            //查询套餐
            List<RecommendedPackage> recommendedPackageList = iRecommendedPackageService.list();

            for (RecommendedPackage recommendedPackage : recommendedPackageList) {

                Optional.ofNullable(getAppGoodListVO(recommendedPackage.getGoodId(),goodCategory.getId())).ifPresent(appGoodListVO -> {

                    if(ToolUtil.isNotEmpty(areaName)){

                        if (ToolUtil.isNotEmpty(areaService.getIdByAreaName(areaName))) {

                            if (appGoodListVO.getAreaId().equals(areaService.getIdByAreaName(areaName))) {
                                AppOneClassGoodListVO oneClassGoodListVO = new AppOneClassGoodListVO();
                                ToolUtil.copyProperties(appGoodListVO, oneClassGoodListVO);
                                list.add(oneClassGoodListVO);
                            }
                        }
                    }else {
                        AppOneClassGoodListVO oneClassGoodListVO = new AppOneClassGoodListVO();
                        ToolUtil.copyProperties(appGoodListVO,oneClassGoodListVO);
                        list.add(oneClassGoodListVO);
                    }

                });

            }


        }

        return list;
    }

    //递归  cl
    public static List<GoodCategory> getByMapCategory(String id, List<GoodCategory> categoryList) {

        List<GoodCategory> list = new ArrayList<>();

        //相等说明：该分类Pid是所查询分类id,所以是所查询分类的子分类fo

        for (int i = 0; i < categoryList.size(); i++) {

            if (categoryList.get(i).getParentId().equals(id)) {

                list.add(categoryList.get(i));

            }
        }
        //如果分类下没有分类，返回一个空List（递归退出）
        if (list.size() == 0) {
            return new ArrayList<GoodCategory>();
        } else {
            for (int i = 0; i < list.size(); i++) {
                getByMapCategory(list.get(i).getId(), categoryList);
            }
        }
        return list;
    }



    /**
     * App模糊查询商品接口
     * @return
     */
    @Override
    public List AppGoodList(String name,SearchTypesEnum searchTypesEnum) {

        if(ToolUtil.isNotEmpty(name)&&!StringUtils.isEmpty(name)){//搜索记录
            //添加热搜次数
            SearchHistory searchHistory = new SearchHistory();
            searchHistory.setTitle(name);
            iSearchHistoryService.insertOrderUpdateSearchHistory(searchHistory);
        }

       if(searchTypesEnum.equals(SearchTypesEnum.GOODS)){//商品
           //模糊查询商品
           List<Good> goods = this.list(new QueryWrapper<Good>().lambda()
                   .like(ToolUtil.isNotEmpty(name),Good::getGoodName,name).eq(Good::getTypeEnum,GoodTypeEnum.GOOD).eq(Good::getDelFlag,CommonConstant.STATUS_NORMAL));

           List<AppGoodListVO> appGoodListVOList = goods.parallelStream().map(good -> {
               AppGoodListVO appGoodListVO = new AppGoodListVO();

               //1.店铺信息
               Optional.ofNullable(userService.getById(good.getCreateBy()))
                       .ifPresent(user -> ToolUtil.copyProperties(user, appGoodListVO.setUserId(user.getId())));

               //2.商品信息
               ToolUtil.copyProperties(good,appGoodListVO);
               String[] pic =  good.getGoodPics().split(",");
               if(ToolUtil.isNotEmpty(pic)){appGoodListVO.setGoodPic(pic[0]);}

               appGoodListVO.setGrade(Double.parseDouble(gradeService.getShopScore(good.getCreateBy())))
                       .setSaleVolume(orderDetailService.getGoodSalesVolume(good.getId()))
                       .setCommentNum(Optional.ofNullable(commentService.getCommentVOByGoodId(good.getId()))
                               .map(List::size)
                               .orElse(0));

               return appGoodListVO;
           }).collect(Collectors.toList());
           if(ToolUtil.isNotEmpty(appGoodListVOList)){ return appGoodListVOList; }else {return null;}
       }else if(searchTypesEnum.equals(SearchTypesEnum.PACKAGE)){//套餐

         return Optional.ofNullable(iGoodsPackageService.AppGoodsPackageList(null,name)).orElse(null);
       }else if(searchTypesEnum.equals(SearchTypesEnum.SHOP)){//商家

            return this.getShopAndPackage(name);
       }

       return null;
    }

    /**
     * 获取搜索的商家和套餐
     * @return
     */
    @Override
    public List getShopAndPackage(String name){
        //首先获取商家和个人商家
        //1查询角色表是商家和个人商家的所有数据
        List<Role> roles = roleMapper.selectList(Wrappers.<Role>lambdaQuery()
                .eq(Role::getName,CommonConstant.STORE).or().eq(Role::getName,CommonConstant.PER_STORE));

        List<UserRole> userRoles= new ArrayList<>();
        for (Role role : roles) {
            //2.获取角色权限表的用户id
            List<UserRole> selectList = userRoleMapper.selectList(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getRoleId, role.getId()));
            for (UserRole userRole : selectList) { userRoles.add(userRole);}}

        List<ShopAndPackageVO> shopAndPackage = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            //查询区域的商家或者店铺
            User u = userMapper.selectOne(new QueryWrapper<User>().lambda()
                    .eq(User::getId,userRole.getUserId()).like(ToolUtil.isNotEmpty(name),User::getShopName,name));
            if(ToolUtil.isNotEmpty(u)){
                ShopAndPackageVO shopAndPackageVO = new ShopAndPackageVO();//商家基础信息
                ToolUtil.copyProperties(u,shopAndPackageVO);

                shopAndPackageVO.setNickName(u.getShopName())
                        .setGrade(Double.parseDouble(gradeService.getShopScore(userRole.getUserId())))
                        .setCommentNum(Optional.ofNullable(commentService.getCommentVOByGoodId(userRole.getUserId()))
                                .map(List::size)
                                .orElse(0));

                shopAndPackage.add(shopAndPackageVO);
            }

        }
            //商家下的商品和套餐
        for (ShopAndPackageVO shopAndPackageVO : shopAndPackage) {
            List<AppGoodInfoVO> appGoodInfoVOList = new ArrayList<>();
            List<Good> g = goodMapper.selectList(new QueryWrapper<Good>().lambda()
                    .eq(Good::getCreateBy, shopAndPackageVO.getId()).eq(Good::getDelFlag,CommonConstant.STATUS_NORMAL));
            for (Good good : g) {
                AppGoodInfoVO appGoodInfoVO = new AppGoodInfoVO();
                ToolUtil.copyProperties(good,appGoodInfoVO);
                appGoodInfoVOList.add(appGoodInfoVO);
            }
            shopAndPackageVO.setAppGoodInfo(appGoodInfoVOList);
        }
        return Optional.ofNullable(shopAndPackage).orElse(null);
    }



    @Override
    public AppGoodInfoVO getAppGoodInfo(String id) {

        return Optional.ofNullable(super.getById(id))
                .map(good -> {

                    AppGoodInfoVO appGoodInfoVO = new AppGoodInfoVO();
                    ToolUtil.copyProperties(good, appGoodInfoVO);

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
    public AppGoodOrderVO getAppGoodOrder(String id,String attrSymbolPath,Integer buyState) {

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
                    SizeAndRolor one = sizeAndRolorService.getOne(Wrappers.<SizeAndRolor>lambdaQuery().eq(SizeAndRolor::getAttrSymbolPath, attrSymbolPath).eq(SizeAndRolor::getGoodsId,id));

                    if (EmptyUtil.isNotEmpty(one)){
                        appGoodOrderVO.setGoodNewPrice(one.getGoodPrice());
                        appGoodOrderVO.setGoodPic(one.getPic());
                        appGoodOrderVO.setIntegral(one.getInventory());
                        appGoodOrderVO.setGoodDeposit(one.getGoodDeposit());
                        appGoodOrderVO.setGoodDalancePayment(one.getGoodDalancePayment());
                    }

                    if(ToolUtil.isNotEmpty(attrSymbolPath)){
                        //2.商品规格
                        List<String> itemAttrVals = iItemAttrValService.getItemAttrVals(attrSymbolPath);
                        appGoodOrderVO.setItemAttrKeys(itemAttrVals);
                        appGoodOrderVO.setAttrSymbolPath(attrSymbolPath);
                    }

                    appGoodOrderVO.setLeaseState(Optional.ofNullable(goodCategoryMapper.selectById(good.getGoodCategoryId())).map(GoodCategory::getLeaseState).orElse(0));
                    appGoodOrderVO.setBuyState(buyState);
                    return appGoodOrderVO;
                }).orElse(null);
    }

    /************************************************PC端******************************************************/

    /**
     * 获取商家的商品列表
     * @return
     */
    @Override
    public List<PcGoodListVO> PCgoodsList(GoodDTO goodDTO){
        List<Good> list = new ArrayList<>();

        String userId = null;
        if(ToolUtil.isNotEmpty(goodDTO.getStoreId())){
            userId= goodDTO.getStoreId();
        }else {
            userId= securityUtil.getCurrUser().getId();
        }

        //用户权限名
        String userRole = this.getRoleUserList(userId);
        if(ToolUtil.isEmpty(userRole)){
            return null;
        }else

        //当前角色是个人商家或者入驻商家
        if(userRole.equals(CommonConstant.PER_STORE)||userRole.equals(CommonConstant.STORE)){
           list = this.list(new QueryWrapper<Good>().lambda()
                    .eq(Good::getCreateBy, userId)
                    .eq(Good::getTypeEnum, GoodTypeEnum.GOOD)
                    .eq(ToolUtil.isNotEmpty(goodDTO.getGoodCategoryId()), Good::getGoodCategoryId, goodDTO.getGoodCategoryId())
                    .like(ToolUtil.isNotEmpty(goodDTO.getGoodName()),Good::getGoodName,goodDTO.getGoodName())
                   .orderByDesc(Good::getCreateTime)
           );
        }else if (userRole.equals(CommonConstant.ADMIN)){
           list = this.list(new QueryWrapper<Good>().lambda()
                   .eq(Good::getTypeEnum, GoodTypeEnum.GOOD)
                   .eq(ToolUtil.isNotEmpty(goodDTO.getGoodCategoryId()), Good::getGoodCategoryId, goodDTO.getGoodCategoryId())
                   .like(ToolUtil.isNotEmpty(goodDTO.getGoodName()),Good::getGoodName,goodDTO.getGoodName())
                   .orderByDesc(Good::getCreateTime));
        }

       if(ToolUtil.isNotEmpty(list)){
           List<PcGoodListVO> pcGoodList = list.parallelStream().map(pcGoods->{
               PcGoodListVO pc = new PcGoodListVO();
               if(ToolUtil.isNotEmpty(pcGoods.getGoodCategoryId())){
                   //分类名称
                   GoodCategory goodCategory1 = goodCategoryMapper.selectById(pcGoods.getGoodCategoryId());
                   if (EmptyUtil.isNotEmpty(goodCategory1)){
                       pc.setGoodCategoryName( goodCategory1.getTitle());
                       pc.setBuyState(goodCategory1.getBuyState());
                       pc.setLeaseState(goodCategory1.getLeaseState());
                   }
                   ToolUtil.copyProperties(pcGoods , pc);
                   pc.setShopName(Optional.ofNullable(userMapper.selectById(pcGoods.getCreateBy())).map(User::getShopName).orElse("暂无！"));
               }
               pc.setStatus(Optional.ofNullable(userMapper.selectById(pcGoods.getCreateBy())).map(User::getStatus).orElse(0));

               //是否是推荐商品
               pc.setRecommend(0);
               Optional.ofNullable(iRecommendedPackageService.getOne(Wrappers.<RecommendedPackage>lambdaQuery().eq(RecommendedPackage::getGoodId,pc.getId())))
                       .ifPresent(recommendedPackage -> {  pc.setRecommend(1);});

                   return  pc;

           })
             .filter(pcGood-> pcGood.getShopName().contains(ToolUtil.isNotEmpty(goodDTO.getShopName())?goodDTO.getShopName():(ToolUtil.isNotEmpty(pcGood.getShopName())?pcGood.getShopName():"")))
                   .collect(Collectors.toList());

           return pcGoodList;
       }else {
           return null;
       }
    }

    /**
     * 后端获取商品详情
     * @param id
     * @return
     */
    @Override
    public PcGoodListVO PCgoodParticulars(String id) {

        return Optional.ofNullable(this.getById(id))
                .map(good -> {
                    PcGoodListVO pcGoodListVO = new PcGoodListVO();

                    ToolUtil.copyProperties(good,pcGoodListVO);
                    pcGoodListVO.setGoodCategoryName(goodCategoryMapper.selectById(good.getGoodCategoryId()).getTitle());//分类名称
                    pcGoodListVO.setStatus(Optional.ofNullable(userMapper.selectById(good.getCreateBy())).map(User::getStatus).orElse(0));//商家是否冻结
                    return pcGoodListVO;
                })
                .orElse(null);
    }

    @Override
    public BigDecimal getLowPriceByStoreId(String storeId) {


        // TODO: 2020/5/30 0030  婚宴酒店的最低价格


        return Optional.ofNullable(this.list(Wrappers.<Good>lambdaQuery()
        .eq(Good::getCreateBy, storeId)))
        .map(goods -> goods.parallelStream().min(Comparator.comparing(Good::getGoodNewPrice)).map(Good::getGoodNewPrice)
                .orElse(new BigDecimal(0)))
        .orElse(new BigDecimal(0));


    }


    /**
     * PC获取商家的套餐列表
     * @return
     */
    @Override
    public List PCgoodsPackageList(GoodDTO goodDTO) {
        List<Good> list = new ArrayList<>();

        String userId = null;
        if(ToolUtil.isNotEmpty(goodDTO.getStoreId())){
            userId= goodDTO.getStoreId();
        }else {
            userId= securityUtil.getCurrUser().getId();
        }

        String userRole = this.getRoleUserList(userId);
        if(ToolUtil.isEmpty(userRole)){
            return null;
        }else
            //当前角色是个人商家或者入驻商家
            if(userRole.equals(CommonConstant.PER_STORE)||userRole.equals(CommonConstant.STORE)){
                list = this.list(new QueryWrapper<Good>().lambda()
                        .eq(Good::getCreateBy, userId)
                        .eq(Good::getTypeEnum, GoodTypeEnum.GOODSPACKAGE)
                        .like(ToolUtil.isNotEmpty(goodDTO.getGoodName()),Good::getGoodName,goodDTO.getGoodName())
                        .orderByDesc(Good::getCreateTime)
                );
            }else if (userRole.equals(CommonConstant.ADMIN)){
                list = this.list(new QueryWrapper<Good>().lambda()
                        .eq(ToolUtil.isNotEmpty(goodDTO.getId()),Good::getId,goodDTO.getId())
                        .like(ToolUtil.isNotEmpty(goodDTO.getGoodName()),Good::getGoodName,goodDTO.getGoodName())
                        .eq(Good::getTypeEnum, GoodTypeEnum.GOODSPACKAGE)
                        .orderByDesc(Good::getCreateTime));
            }

        if(ToolUtil.isNotEmpty(list)){
            List<PcGoodsPackageListVO> pcGoodsPackageListVO = list.parallelStream().map(pcGoodsPackage->{
                PcGoodsPackageListVO pc = new PcGoodsPackageListVO();
                if(ToolUtil.isNotEmpty(pcGoodsPackage.getGoodCategoryId())){
                    //分类名称
                    String goodCategory = Optional.ofNullable(goodCategoryMapper.selectById(pcGoodsPackage.getGoodCategoryId())).map(GoodCategory::getTitle).orElse("未设置分类！");
                    pc.setGoodCategoryName(goodCategory);
                    ToolUtil.copyProperties(pcGoodsPackage , pc);
                    pc.setShopName(Optional.ofNullable(userMapper.selectById(pcGoodsPackage.getCreateBy())).map(User::getShopName).orElse("暂无"));//店铺名称
                }
                pc.setProductsIntroduction(iGoodsIntroduceService.goodsIntroduceList(null,pcGoodsPackage.getId(),1))//商品介绍
                        .setPurchaseNotes(iGoodsIntroduceService.goodsIntroduceList(null,pcGoodsPackage.getId(),2));//购买须知

                pc.setStatus(Optional.ofNullable(userMapper.selectById(pcGoodsPackage.getCreateBy())).map(User::getStatus).orElse(0));

                //是否是推荐商品
                pc.setRecommend(0);
                Optional.ofNullable(iRecommendedPackageService.getOne(Wrappers.<RecommendedPackage>lambdaQuery().eq(RecommendedPackage::getGoodId,pc.getId())))
                        .ifPresent(recommendedPackage -> {  pc.setRecommend(1);});

                return  pc;
            }).filter(pcGood-> pcGood.getGoodCategoryId().contains(ToolUtil.isNotEmpty(goodDTO.getGoodCategoryId())?goodDTO.getGoodCategoryId():pcGood.getGoodCategoryId()))
                    .filter(pcGood-> pcGood.getShopName().contains(ToolUtil.isNotEmpty(goodDTO.getShopName())?goodDTO.getShopName():pcGood.getShopName()))

                    .collect(Collectors.toList());

            return pcGoodsPackageListVO;
        }else {
            return null;
        }
    }




    @Override
    public  List<AppForSaleGoodsVO> getAppForSaleGoods(String ids) {
        //查询商家创建的商品
        List<Good> good =this.list(new QueryWrapper<Good>().lambda().eq(Good::getCreateBy,ids).eq(Good::getTypeEnum,GoodTypeEnum.GOOD).eq(Good::getDelFlag,CommonConstant.STATUS_NORMAL));

        List<AppForSaleGoodsVO> forSaleGoodsVO = new ArrayList<>();
         for (Good g : good) {
             AppForSaleGoodsVO appForSaleGoodsVO  = new AppForSaleGoodsVO();
             ToolUtil.copyProperties(g,appForSaleGoodsVO);
             appForSaleGoodsVO.setGoodPics(g.getGoodPics().split(",")[0]);
             forSaleGoodsVO.add(appForSaleGoodsVO);
        }
        return forSaleGoodsVO;
    }


    /**
     * 查询角色权限
     * @return
     */
    @Override
    public String getRoleUserList(String userId) {
        UserRole userRole = userRoleMapper.selectOne(new QueryWrapper<UserRole>().lambda()
                .eq(UserRole::getUserId,userId)
        );

       if(ToolUtil.isNotEmpty(userRole)){
           return  Optional.ofNullable(roleMapper.selectOne(new QueryWrapper<Role>().lambda()
                   .eq(ToolUtil.isNotEmpty(userRole),Role::getId,userRole.getRoleId())))
                   .map(Role::getName)
                   .orElse(null);
       }else {
           return null;
       }

    }



}