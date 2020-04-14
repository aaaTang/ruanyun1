package cn.ruanyun.backInterface.modules.business.goodsPackage.serviceimpl;

import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
import cn.ruanyun.backInterface.common.enums.DisCouponTypeEnum;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.serviceimpl.mybatis.IUserServiceImpl;
import cn.ruanyun.backInterface.modules.base.vo.AppUserVO;
import cn.ruanyun.backInterface.modules.base.vo.BackUserVO;
import cn.ruanyun.backInterface.modules.business.area.mapper.AreaMapper;
import cn.ruanyun.backInterface.modules.business.area.pojo.Area;
import cn.ruanyun.backInterface.modules.business.area.service.IAreaService;
import cn.ruanyun.backInterface.modules.business.bestChoiceShop.mapper.BestShopMapper;
import cn.ruanyun.backInterface.modules.business.discountCoupon.VO.DiscountCouponListVO;
import cn.ruanyun.backInterface.modules.business.discountCoupon.pojo.DiscountCoupon;
import cn.ruanyun.backInterface.modules.business.discountCoupon.service.IDiscountCouponService;
import cn.ruanyun.backInterface.modules.business.discountCoupon.serviceimpl.IDiscountCouponServiceImpl;
import cn.ruanyun.backInterface.modules.business.goodsPackage.DTO.ShopParticularsDTO;
import cn.ruanyun.backInterface.modules.business.goodsPackage.VO.*;
import cn.ruanyun.backInterface.modules.business.goodsPackage.mapper.GoodsPackageMapper;
import cn.ruanyun.backInterface.modules.business.goodsPackage.pojo.GoodsPackage;
import cn.ruanyun.backInterface.modules.business.goodsPackage.service.IGoodsPackageService;
import cn.ruanyun.backInterface.modules.business.myFavorite.mapper.MyFavoriteMapper;
import cn.ruanyun.backInterface.modules.business.myFavorite.service.IMyFavoriteService;
import cn.ruanyun.backInterface.modules.business.storeAudit.mapper.StoreAuditMapper;
import cn.ruanyun.backInterface.modules.business.storeAudit.pojo.StoreAudit;
import cn.ruanyun.backInterface.modules.business.storeAudit.service.IStoreAuditService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcloud.cos.transfer.Copy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Resource;
import javax.persistence.Convert;

import static jdk.nashorn.api.scripting.ScriptUtils.convert;


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
    private IMyFavoriteService iMyFavoriteService;


    @Override
    public void insertOrderUpdateGoodsPackage(GoodsPackage goodsPackage) {
        if (ToolUtil.isEmpty(goodsPackage.getCreateBy())) {
            goodsPackage.setCreateBy(securityUtil.getCurrUser().getId());
        } else {
            goodsPackage.setUpdateBy(securityUtil.getCurrUser().getId());
        }
        Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(goodsPackage)))
                .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                .toFuture().join();
    }

    @Override
    public void removeGoodsPackage(String ids) {
        CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
    }

    /**
     * 后端查询商品全部数据
     */
    public List<GoodsPackage> BackGoodsPackageList() {
        return this.list();
    }


    /**
     * App查询商家商品详情
     *
     * @param id
     * @return
     */
    public Result<Object> GetGoodsPackage(String id) {
        GoodsPackage goodsPackage = this.getById(id);
        GoodsPackageParticularsVO goodsPackageParticularsVO = new GoodsPackageParticularsVO();
        BeanUtils.copyProperties(goodsPackage, goodsPackageParticularsVO);
        goodsPackageParticularsVO.setStoreAuditVO(storeAuditService.getStoreAudisByid(goodsPackage.getCreateBy()));
        List<GoodsPackageListVO> goodsPackageListVOS = this.GetGoodsPackageList(null, null, null, goodsPackage.getCreateBy());
        if (goodsPackageListVOS.size() > 4){
            goodsPackageListVOS = goodsPackageListVOS.subList(0,4);
        }
        goodsPackageParticularsVO.setAppGoodsPackageListVOs(goodsPackageListVOS);
        return new ResultUtil<>().setData(goodsPackageParticularsVO);
    }


    /**
     * App分类商家商品筛选
     */
    public List<GoodsPackageListVO> GetGoodsPackageList(String classId,String areaId,Integer newPrice,String createBy){
        List<GoodsPackage> list= this.list(new QueryWrapper<GoodsPackage>().lambda()
                .eq(EmptyUtil.isNotEmpty(classId),GoodsPackage::getClassId,classId)
                .eq(EmptyUtil.isNotEmpty(areaId),GoodsPackage::getAreaId,areaId)
                .eq(EmptyUtil.isNotEmpty(createBy),GoodsPackage::getCreateBy,createBy)
                .orderByDesc(EmptyUtil.isNotEmpty(newPrice)&&newPrice.equals(1),GoodsPackage::getNewPrice)
                .orderByAsc(EmptyUtil.isNotEmpty(newPrice)&&newPrice.equals(0),GoodsPackage::getNewPrice)
        );

        List<GoodsPackageListVO> goodsPackageListVOS = list.parallelStream().map(goodsPackage -> {
            GoodsPackageListVO goodsPackageListVOList =new GoodsPackageListVO();
            BackUserVO backUserVO = iUserService.getBackUserVO(goodsPackage.getCreateBy());//查询用户信息
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
    public ShopParticularsVO getShopParticulars(String ids){

            User user = iUserService.getById(ids);//获取店铺详情
            ShopParticularsVO  shopParticularsVO = new ShopParticularsVO();
            ToolUtil.copyProperties(user,shopParticularsVO);

        //获取店铺通用优惠券
        List<DiscountCoupon> discountCoupon = iDiscountCouponService.list(new QueryWrapper<DiscountCoupon>().lambda()
        .eq(DiscountCoupon::getDisCouponType, DisCouponTypeEnum.ALL_USE)
                .eq(DiscountCoupon::getStoreAuditOid,ids).eq(DiscountCoupon::getPastDue, BooleanTypeEnum.NO));

        List<DiscountCouponListVO> dvo = new ArrayList<>();
        for (DiscountCoupon dc : discountCoupon) {
            DiscountCouponListVO vo = new DiscountCouponListVO();
            ToolUtil.copyProperties(dc,vo);
            vo.setIsReceive(iDiscountCouponServiceImpl.getDetailById(dc));
            dvo.add(vo);
        }
        shopParticularsVO.setDiscountList(dvo);//优惠券

        //TODO::2020/4/13 店铺评分 未处理

        //TODO::2020/4/13 是否关注店铺
        shopParticularsVO.setFavroite(iMyFavoriteService.getMyFavoriteShop(ids));

        return shopParticularsVO;
    }

    /**
     * 查询商家精选套餐
     */
    public List<AppGoodsPackageListVO> AppGoodsPackageList(String ids){

        List<GoodsPackage>  goodsPackage = this.list(new QueryWrapper<GoodsPackage>().lambda().eq(GoodsPackage::getCreateBy,ids));

        List<AppGoodsPackageListVO> appGoodsPackageList = new ArrayList<>();

        for (GoodsPackage gPackage : goodsPackage) {
            AppGoodsPackageListVO appGoodsVO = new AppGoodsPackageListVO();
            ToolUtil.copyProperties(gPackage,appGoodsVO);
            appGoodsPackageList.add(appGoodsVO);
        }
        return appGoodsPackageList;
    }

    /**
     * 修改店铺详情
     */
    public void UpdateShopParticulars(ShopParticularsDTO shopParticularsDTO){
        igoodsPackageMapper.UpdateShopParticulars(shopParticularsDTO);
    }

    /**
     * 后端获取店铺列表
     */
    @Override
    public List<ShopDatelistVO> getShopDateList(String username, String shopName, Integer storeType) {
            return igoodsPackageMapper.getShopDateList(username,shopName,storeType);
    }



}