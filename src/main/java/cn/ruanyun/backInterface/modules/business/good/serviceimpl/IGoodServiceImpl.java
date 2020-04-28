package cn.ruanyun.backInterface.modules.business.good.serviceimpl;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import cn.ruanyun.backInterface.common.enums.SearchTypesEnum;
import cn.ruanyun.backInterface.common.utils.EmptyUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.RoleMapper;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserRoleMapper;
import cn.ruanyun.backInterface.modules.base.pojo.Role;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.pojo.UserRole;
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
import cn.ruanyun.backInterface.modules.business.goodCategory.entity.GoodCategory;
import cn.ruanyun.backInterface.modules.business.goodCategory.mapper.GoodCategoryMapper;
import cn.ruanyun.backInterface.modules.business.goodService.service.IGoodServiceService;
import cn.ruanyun.backInterface.modules.business.goodsPackage.service.IGoodsPackageService;
import cn.ruanyun.backInterface.modules.business.grade.service.IGradeService;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.service.IItemAttrValService;
import cn.ruanyun.backInterface.modules.business.myFavorite.service.IMyFavoriteService;
import cn.ruanyun.backInterface.modules.business.myFootprint.pojo.MyFootprint;
import cn.ruanyun.backInterface.modules.business.myFootprint.serviceimpl.IMyFootprintServiceImpl;
import cn.ruanyun.backInterface.modules.business.orderDetail.service.IOrderDetailService;
import cn.ruanyun.backInterface.modules.business.searchHistory.pojo.SearchHistory;
import cn.ruanyun.backInterface.modules.business.searchHistory.service.ISearchHistoryService;
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
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleMapper roleMapper;

    @Autowired
    private IGradeService gradeService;

    @Resource
    private GoodMapper goodMapper;





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

        if(ToolUtil.isEmpty(goodDTO.getAreaId())){
            // 1.默认条件构造器
            LambdaQueryWrapper<Good> wrappers =  Wrappers.<Good>lambdaQuery()
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
                        .eq(!StringUtils.isEmpty(goodDTO.getGoodCategoryId()),Good::getGoodCategoryId,goodDTO.getGoodCategoryId())
                        .eq(!EmptyUtil.isEmpty(goodDTO.getGoodTypeEnum()),Good::getTypeEnum, goodDTO.getGoodTypeEnum())
                        .eq(Good::getCreateBy, user.getId())
                        .lt(ToolUtil.isNotEmpty(goodDTO.getPriceHigh())&&ToolUtil.isNotEmpty(goodDTO.getPriceLow()),Good::getGoodNewPrice,goodDTO.getPriceHigh())
                        .gt(ToolUtil.isNotEmpty(goodDTO.getPriceHigh())&&ToolUtil.isNotEmpty(goodDTO.getPriceLow()),Good::getGoodNewPrice,goodDTO.getPriceLow()));
                for (Good good : g) {
                    goods.add(good);
                }
            }
            return Optional.ofNullable(goods).orElse(null);

        }


        return null;

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
            //TODO: 店铺数据
            goodDetailVO.setShopList(shopList)
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


    /**
     * 获取首页一级分类下的所有商品
     * @return
     */
    @Override
    public List<AppOneClassGoodListVO> getAppOneClassGoodList(String classId) {


      List<GoodCategory> goodCategoryList = goodCategoryMapper.selectList(new QueryWrapper<GoodCategory>().lambda()
              .eq(ToolUtil.isNotEmpty(classId),GoodCategory::getParentId,classId)
      );

      List<AppOneClassGoodListVO> list = new ArrayList<>();

        for (GoodCategory goodCategory : goodCategoryList) {
            List<Good> goods = goodMapper.selectList(new QueryWrapper<Good>().lambda().eq(Good::getGoodCategoryId,goodCategory.getId()));
            for (Good good : goods) {
              AppGoodListVO appGoodListVO = getAppGoodListVO(good.getId());
                AppOneClassGoodListVO oneClassGoodListVO = new AppOneClassGoodListVO();
                ToolUtil.copyProperties(appGoodListVO,oneClassGoodListVO);
                list.add(oneClassGoodListVO);
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
                   .like(Good::getGoodName,name));

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
                    .eq(User::getId,userRole.getUserId()).like(ToolUtil.isNotEmpty(name),User::getNickName,name));
            if(ToolUtil.isNotEmpty(u)){
                ShopAndPackageVO shopAndPackageVO = new ShopAndPackageVO();//商家基础信息
                ToolUtil.copyProperties(u,shopAndPackageVO);

                shopAndPackageVO.setGrade(Double.parseDouble(gradeService.getShopScore(userRole.getUserId())))
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
                    .eq(Good::getCreateBy, shopAndPackageVO.getId()));
            for (Good good : g) {
                AppGoodInfoVO appGoodInfoVO = new AppGoodInfoVO();
                ToolUtil.copyProperties(good,appGoodInfoVO);
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
                        appGoodOrderVO.setIntegral(one.getInventory());
                    }

                    //2.商品规格
                    List<String> itemAttrVals = iItemAttrValService.getItemAttrVals(attrSymbolPath);
                    appGoodOrderVO.setItemAttrKeys(itemAttrVals);
                    appGoodOrderVO.setAttrSymbolPath(attrSymbolPath);
                    return appGoodOrderVO;
                }).orElse(null);
    }

    /************************************************PC端******************************************************/

    @Override
    public List<PcGoodListVO> PCgoodsList(){
        List<Good> list = new ArrayList<>();

        String userRole = this.getRoleUserList(securityUtil.getCurrUser().getId());
        if(ToolUtil.isEmpty(userRole)){
            return null;
        }else
        //当前角色是个人商家或者入驻商家
        if(userRole.equals(CommonConstant.PER_STORE)||userRole.equals(CommonConstant.STORE)){
           list = this.list(new QueryWrapper<Good>().lambda()
                    .eq(Good::getCreateBy, securityUtil.getCurrUser().getId())
                   .orderByDesc(Good::getCreateTime)
           );
        }else if (userRole.equals(CommonConstant.ADMIN)){
           list = this.list(new QueryWrapper<Good>().lambda()
                   .orderByDesc(Good::getCreateTime));
        }

       if(ToolUtil.isNotEmpty(list)){
           List<PcGoodListVO> pcGoodList = list.parallelStream().map(pcGoods->{
               PcGoodListVO pc = new PcGoodListVO();
               String goodCategory = goodCategoryMapper.selectById(pcGoods.getGoodCategoryId()).getTitle();
               pc.setGoodCategoryName(goodCategory);

               ToolUtil.copyProperties(pcGoods , pc);
               return  pc;
           }).collect(Collectors.toList());

           return pcGoodList;
       }else {
           return null;
       }

    }

    @Override
    public  List<AppForSaleGoodsVO> getAppForSaleGoods(String ids) {
        //查询商家创建的商品
        List<Good> good =this.list(new QueryWrapper<Good>().lambda().eq(Good::getCreateBy,ids).eq(Good::getTypeEnum,GoodTypeEnum.GOOD));

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