package cn.ruanyun.backInterface.modules.business.goodsPackage.serviceimpl;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.*;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.serviceimpl.mybatis.IUserServiceImpl;
import cn.ruanyun.backInterface.modules.base.vo.BackUserVO;
import cn.ruanyun.backInterface.modules.business.bookingOrder.service.IBookingOrderService;
import cn.ruanyun.backInterface.modules.business.comment.service.ICommentService;
import cn.ruanyun.backInterface.modules.business.discountCoupon.VO.DiscountCouponListVO;
import cn.ruanyun.backInterface.modules.business.discountCoupon.pojo.DiscountCoupon;
import cn.ruanyun.backInterface.modules.business.discountCoupon.service.IDiscountCouponService;
import cn.ruanyun.backInterface.modules.business.discountCoupon.serviceimpl.IDiscountCouponServiceImpl;
import cn.ruanyun.backInterface.modules.business.followAttention.service.IFollowAttentionService;
import cn.ruanyun.backInterface.modules.business.good.mapper.GoodMapper;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.good.serviceimpl.IGoodServiceImpl;
import cn.ruanyun.backInterface.modules.business.goodCategory.entity.GoodCategory;
import cn.ruanyun.backInterface.modules.business.goodCategory.mapper.GoodCategoryMapper;
import cn.ruanyun.backInterface.modules.business.goodsIntroduce.service.IGoodsIntroduceService;
import cn.ruanyun.backInterface.modules.business.goodsPackage.DTO.ShopParticularsDTO;
import cn.ruanyun.backInterface.modules.business.goodsPackage.VO.*;
import cn.ruanyun.backInterface.modules.business.goodsPackage.mapper.GoodsPackageMapper;
import cn.ruanyun.backInterface.modules.business.goodsPackage.pojo.GoodsPackage;
import cn.ruanyun.backInterface.modules.business.goodsPackage.service.IGoodsPackageService;
import cn.ruanyun.backInterface.modules.business.grade.service.IGradeService;
import cn.ruanyun.backInterface.modules.business.myFavorite.service.IMyFavoriteService;
import cn.ruanyun.backInterface.modules.business.storeActivity.service.IStoreActivityService;
import cn.ruanyun.backInterface.modules.business.storeAudit.service.IStoreAuditService;
import cn.ruanyun.backInterface.modules.business.storeFirstRateService.service.IstoreFirstRateServiceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * 商品套餐接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IGoodsPackageServiceImpl extends ServiceImpl<GoodsPackageMapper, GoodsPackage> implements IGoodsPackageService {


    @Autowired
    private SecurityUtil securityUtil;
    @Resource
    private UserMapper userMapper;
    @Resource
    private IUserServiceImpl iUserService;
    @Resource
    private GoodsPackageMapper igoodsPackageMapper;
    @Autowired
    private IStoreAuditService storeAuditService;
    @Resource
    private IDiscountCouponService iDiscountCouponService;
    @Resource
    private IDiscountCouponServiceImpl iDiscountCouponServiceImpl;
    @Resource
    private IGoodServiceImpl iGoodService;
    @Resource
    private IFollowAttentionService followAttentionService;
    @Resource
    private IBookingOrderService iBookingOrderService;
    @Resource
    private GoodMapper goodMapper;
    @Autowired
    private IGradeService gradeService;
    @Autowired
    private IGoodsIntroduceService iGoodsIntroduceService;
    @Autowired
    private IMyFavoriteService iMyFavoriteService;
    @Resource
    private GoodCategoryMapper goodCategoryMapper;
    @Autowired
    private IstoreFirstRateServiceService storeFirstRateServiceService;
    @Autowired
    private IStoreActivityService iStoreActivityService;
    @Autowired
    private ICommentService commentService;

    /**
     * App查询商家套餐详情
     *
     * @param ids
     * @return
     */
    @Override
    public Result<Object> GetGoodsPackage(String ids) {

        Good goodsPackage = goodMapper.selectById(ids);

        String principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        GoodsPackageParticularsVO goodsPackageParticularsVO = new GoodsPackageParticularsVO();
           if(ToolUtil.isNotEmpty(goodsPackage)){
               ToolUtil.copyProperties(goodsPackage,goodsPackageParticularsVO);

               GoodCategory goodCategory = goodCategoryMapper.selectById(goodsPackage.getGoodCategoryId());
               //套餐图片
               goodsPackageParticularsVO.setPics(goodsPackage.getGoodPics())
               //商品介绍
               .setProductsIntroduction(iGoodsIntroduceService.goodsIntroduceList(null,goodsPackage.getId(),1))
               //购买须知
               .setPurchaseNotes(iGoodsIntroduceService.goodsIntroduceList(null,goodsPackage.getId(),2))
                //商铺信息
               .setStoreAuditVO(storeAuditService.getStoreAudisByid(goodsPackage.getCreateBy()))
               //购买状态 1购买 2租赁
               .setBuyState(Optional.ofNullable(goodCategory).map(GoodCategory::getBuyState).orElse(null))
               //租赁状态 1尾款线上支付  2尾款线下支付
               .setLeaseState(Optional.ofNullable(goodCategory).map(GoodCategory::getLeaseState).orElse(null));

               //是否是四大金刚  0否   1是
               Optional.ofNullable(Optional.ofNullable(goodCategoryMapper.selectById(goodCategory.getParentId())).orElse(null)).ifPresent(goodCategory1 -> {
                   if(goodCategory1.getTitle().equals("四大金刚")){
                       goodsPackageParticularsVO.setDevarajas(1);
                   }else {
                       goodsPackageParticularsVO.setDevarajas(0);
                   }
               });
           }

        if(!"anonymousUser".equals(principal)) {

            //是否收藏套餐
            goodsPackageParticularsVO.setMyFavorite(iMyFavoriteService.getMyFavorite(goodsPackage.getId(),GoodTypeEnum.GOODSPACKAGE));

            goodsPackageParticularsVO.setWhetherBookingOrder(iBookingOrderService.getWhetherBookingOrder(goodsPackage.getCreateBy(), securityUtil.getCurrUser().getId()));
        }

        if(ToolUtil.isNotEmpty(goodsPackageParticularsVO.getId())){
            return new ResultUtil<>().setData(goodsPackageParticularsVO);
        }else {
            return null;
        }

    }


    /**
     * App分类商家商品筛选
     */
    @Override
    public List<GoodsPackageListVO> GetGoodsPackageList(String classId, String areaId, Integer newPrice, String createBy){
        List<GoodsPackage> list= this.list(new QueryWrapper<GoodsPackage>().lambda()
                .eq(EmptyUtil.isNotEmpty(classId),GoodsPackage::getClassId,classId)
                .eq(EmptyUtil.isNotEmpty(areaId),GoodsPackage::getAreaId,areaId)
                .eq(EmptyUtil.isNotEmpty(createBy),GoodsPackage::getCreateBy,createBy)
                .eq(GoodsPackage::getDelFlag,CommonConstant.STATUS_NORMAL)
                .orderByDesc(EmptyUtil.isNotEmpty(newPrice)&&newPrice.equals(1),GoodsPackage::getNewPrice)
                .orderByAsc(EmptyUtil.isNotEmpty(newPrice)&&newPrice.equals(0),GoodsPackage::getNewPrice)
        );

        List<GoodsPackageListVO> goodsPackageListVOS = list.parallelStream().map(goodsPackage -> {
            GoodsPackageListVO goodsPackageListVOList =new GoodsPackageListVO();
            BackUserVO backUserVO = iUserService.getBackUserVO(goodsPackage.getCreateBy(), null);//查询用户信息
            goodsPackageListVOList.setUserid(goodsPackage.getCreateBy())
                    .setUserName(backUserVO.getUsername())
                    .setUserPic(backUserVO.getAvatar());
            ToolUtil.copyProperties(goodsPackage, goodsPackageListVOList);
            return goodsPackageListVOList;
        }).collect(Collectors.toList());
        return goodsPackageListVOS;
    }


    /**
     * 获取App店铺详情数据成功
     */
    @Override
    public ShopParticularsVO getShopParticulars(String ids, String longitude, String latitude){


        String principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        //获取店铺详情
        User user = iUserService.getById(ids);

        ShopParticularsVO shopParticularsVO = new ShopParticularsVO();

        if (ToolUtil.isNotEmpty(user)) {

            ToolUtil.copyProperties(user, shopParticularsVO);

            //获取店铺通用优惠券
            List<DiscountCouponListVO> couponListByCreateBy = iDiscountCouponService.getDiscountCouponListByCreateBy(ids);

            //优惠券
            shopParticularsVO.setDiscountList(couponListByCreateBy);

            //优质服务
            shopParticularsVO.setFirstRateService(
                    storeFirstRateServiceService.getStoreFirstRateServiceName(user.getId(),CheckEnum.CHECK_SUCCESS));

            //门店活动
            shopParticularsVO.setActivity(iStoreActivityService.getStoreActivity(ids,null));


            //店铺评分
            shopParticularsVO.setScore(gradeService.getShopScore(ids));


            if(!"anonymousUser".equals(principal)){

                //是否预约店铺
                shopParticularsVO.setWhetherBookingOrder(iBookingOrderService.getWhetherBookingOrder(ids, securityUtil.getCurrUser().getId()));

                //是否关注店铺
                shopParticularsVO.setFavroite(followAttentionService.getMyFollowAttentionShop(ids, FollowTypeEnum.Follow_SHOP));

            }

            //距离
            shopParticularsVO.setDistance(LocationUtils.getDistance(longitude, latitude, user.getLongitude(), user.getLatitude()) + "");

            return shopParticularsVO;
        } else {
            return null;
        }


    }

    /**
     * 查询商家精选套餐
     */
    @Override
    public List<AppGoodsPackageListVO> AppGoodsPackageList(String ids,String name){

        List<Good>  goodsPackage = goodMapper.selectList(new QueryWrapper<Good>().lambda()
                .eq(ToolUtil.isNotEmpty(ids),Good::getCreateBy,ids)
                .like(ToolUtil.isNotEmpty(name),Good::getGoodName,name)
                .eq(Good::getTypeEnum,GoodTypeEnum.GOODSPACKAGE)
                .eq(Good::getDelFlag, CommonConstant.STATUS_NORMAL));

        List<AppGoodsPackageListVO> appGoodsPackageList = new ArrayList<>();

        for (Good gPackage : goodsPackage) {
            AppGoodsPackageListVO appGoodsVO = new AppGoodsPackageListVO();
            appGoodsVO.setId(gPackage.getId()).setNickName(Optional.ofNullable(userMapper.selectById(gPackage.getCreateBy())).map(User::getNickName).orElse(null))
            .setGoodsName(gPackage.getGoodName()).setNewPrice(gPackage.getGoodNewPrice()).setOldPrice(gPackage.getGoodOldPrice());
            String[] pic  = gPackage.getGoodPics().split(",");
            if(ToolUtil.isNotEmpty(pic)){
                appGoodsVO.setPics(pic[0]);
            }
            appGoodsPackageList.add(appGoodsVO);
        }
        return appGoodsPackageList;
    }

    @Override
    public List AppGoodsRecommendPackageList(String ids,String goodName) {

        List<Good>  goodsPackage = goodMapper.selectList(new QueryWrapper<Good>().lambda()
                .eq(ToolUtil.isNotEmpty(ids),Good::getCreateBy,ids)
                .eq(ToolUtil.isNotEmpty(ids),Good::getGoodName,goodName)
                .eq(Good::getTypeEnum,GoodTypeEnum.GOODSPACKAGE)
                .eq(Good::getDelFlag,CommonConstant.STATUS_NORMAL)
                .orderByDesc(Good::getCreateTime)
        );

        List<AppGoodsPackageListVO> appGoodsPackageList = new ArrayList<>();

        for (Good gPackage : goodsPackage) {
            AppGoodsPackageListVO appGoodsVO = new AppGoodsPackageListVO();
            appGoodsVO.setId(gPackage.getId()).setNickName(Optional.ofNullable(userMapper.selectById(gPackage.getCreateBy())).map(User::getNickName).orElse(null))
                    .setShopId(gPackage.getCreateBy())
                    .setGoodsName(gPackage.getGoodName()).setNewPrice(gPackage.getGoodNewPrice()).setOldPrice(gPackage.getGoodOldPrice());
            String[] pic  = gPackage.getGoodPics().split(",");
            if(ToolUtil.isNotEmpty(pic)){
                appGoodsVO.setPics(pic[0]);
            }
            appGoodsPackageList.add(appGoodsVO);
        }
        Collections.shuffle(appGoodsPackageList);
        return appGoodsPackageList;
    }

    /**
     * 修改店铺详情
     */
    @Override
    public void UpdateShopParticulars(ShopParticularsDTO shopParticularsDTO){
        igoodsPackageMapper.UpdateShopParticulars(shopParticularsDTO);
    }

    /**
     * 后端获取店铺列表
     */
    @Override
    public List<ShopDatelistVO> getShopDateList(String username, String shopName, Integer storeType) {
//            return igoodsPackageMapper.getShopDateList(username,shopName,storeType);r
        return null;
    }

    /**
     * 获取App店铺详情参数
     */
    @Override
    public ShopParticularsParameterVO getShopParticularsParameter(String ids) {

        User user = iUserService.getById(ids);
        if(ToolUtil.isNotEmpty(user)){
            ShopParticularsParameterVO shopParticularsParameterVO  = new ShopParticularsParameterVO();
            shopParticularsParameterVO.setId(user.getId())
                    .setAvatar(user.getAvatar())
                    .setShopName(user.getShopName())
                    .setIndividualResume(user.getIndividualResume())
                    //获取商品数量
                    .setGoodsNum(iGoodService.getAppForSaleGoods(ids).size())
                    //获取店铺关注数量
                    .setFollowAttentionNum(followAttentionService.getMefansNum(ids))
                    //TODO::评论数量
                     .setEvaluateNum(commentService.getCommentByStore(user.getId()))
                    //当前登录用户是否关注这个店铺
                    .setMyFollowAttention(followAttentionService.getMyFollowAttentionShop(ids,FollowTypeEnum.Follow_SHOP))
                    ;
                if(ToolUtil.isNotEmpty(user.getPic())){
                    String[] pic = user.getPic().split(",");
                    shopParticularsParameterVO.setPic(pic[0]);
                }
            return shopParticularsParameterVO;
        }else {
            return null;
        }

    }


}