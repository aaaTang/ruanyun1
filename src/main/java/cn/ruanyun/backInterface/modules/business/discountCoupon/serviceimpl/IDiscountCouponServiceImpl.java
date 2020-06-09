package cn.ruanyun.backInterface.modules.business.discountCoupon.serviceimpl;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
import cn.ruanyun.backInterface.common.enums.DisCouponTypeEnum;
import cn.ruanyun.backInterface.common.enums.Disabled;
import cn.ruanyun.backInterface.common.enums.UsableRangeTypeEnum;
import cn.ruanyun.backInterface.common.utils.EmptyUtil;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.business.discountCoupon.DTO.DiscountCouponDTO;
import cn.ruanyun.backInterface.modules.business.discountCoupon.VO.AppDiscountCouponListVO;
import cn.ruanyun.backInterface.modules.business.discountCoupon.VO.DiscountCouponListVO;
import cn.ruanyun.backInterface.modules.business.discountCoupon.VO.PcGetDiscountCouponListVO;
import cn.ruanyun.backInterface.modules.business.discountCoupon.VO.PlatformDiscountCouponVO;
import cn.ruanyun.backInterface.modules.business.discountCoupon.mapper.DiscountCouponMapper;
import cn.ruanyun.backInterface.modules.business.discountCoupon.pojo.DiscountCoupon;
import cn.ruanyun.backInterface.modules.business.discountCoupon.service.IDiscountCouponService;
import cn.ruanyun.backInterface.modules.business.discountMy.VO.DiscountVO;
import cn.ruanyun.backInterface.modules.business.discountMy.pojo.DiscountMy;
import cn.ruanyun.backInterface.modules.business.discountMy.service.IDiscountMyService;
import cn.ruanyun.backInterface.modules.business.discountShop.mapper.DiscountShopMapper;
import cn.ruanyun.backInterface.modules.business.discountShop.pojo.DiscountShop;
import cn.ruanyun.backInterface.modules.business.good.mapper.GoodMapper;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import cn.ruanyun.backInterface.modules.business.harvestAddress.VO.HarvestAddressVO;
import cn.ruanyun.backInterface.modules.business.harvestAddress.entity.HarvestAddress;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;

import javax.annotation.Resource;
import javax.swing.text.html.Option;


/**
 * 优惠券接口实现
 *
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IDiscountCouponServiceImpl extends ServiceImpl<DiscountCouponMapper, DiscountCoupon> implements IDiscountCouponService {

    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private IDiscountMyService discountMyService;
    @Autowired
    private IGoodService iGoodService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private GoodMapper goodMapper;
    @Resource
    private DiscountShopMapper discountShopMapper;

    @Override
    public void insertOrderUpdateDiscountCoupon(DiscountCoupon discountCoupon) {
        if (ToolUtil.isEmpty(discountCoupon.getCreateBy())) {
            discountCoupon.setCreateBy(securityUtil.getCurrUser().getId());
        } else {
            discountCoupon.setUpdateBy(securityUtil.getCurrUser().getId());
        }
        Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(discountCoupon)))
                .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                .toFuture().join();
    }

    @Override
    public void removeDiscountCoupon(String ids) {
        CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
    }

    /**
     * 获取优惠券详情
     *
     * @param id
     * @return
     */
    @Override
    public DiscountCoupon getDiscountCouponDetail(String id) {
        return Optional.ofNullable(this.getById(id)).orElse(null);
    }


    /**
     * 按商品获取优惠券
     *
     * @param goodsPackageId
     * @return
     */
    @Override
    public List<DiscountCouponListVO> getDiscountCouponListByGoodsPackageId(String goodsPackageId) {

        String principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        DiscountCoupon discountCoupon = new DiscountCoupon();
        discountCoupon.setGoodsPackageId(goodsPackageId);
        discountCoupon.setDisCouponType(DisCouponTypeEnum.ONE_PRODUCT);
        //获取该商品下所有的优惠券
        List<DiscountCoupon> discountCouponList = this.getDiscountCouponList(discountCoupon);


        List<DiscountCouponListVO> discountVOS = new ArrayList<>();
        discountCouponList.forEach(discountCoupon1 -> {
            DiscountCouponListVO discountCouponListVO = new DiscountCouponListVO();
            ToolUtil.copyProperties(discountCoupon1, discountCouponListVO);

            if(!"anonymousUser".equals(principal)) {

                //是否被领取
                discountCouponListVO.setIsReceive(this.getDetailById(discountCoupon1));
            }

            discountCouponListVO.setValidityPeriod(discountCouponListVO.getValidityPeriod().substring(0, discountCouponListVO.getValidityPeriod().indexOf(" ")));
            discountVOS.add(discountCouponListVO);
        });
        return discountVOS;
    }


    //判断优惠券是否被领取
    public boolean getDetailById(DiscountCoupon discountCoupon) {
        String userId = securityUtil.getCurrUser().getId();

        DiscountMy one = discountMyService.getOne(Wrappers.<DiscountMy>lambdaQuery()
                .eq(DiscountMy::getCreateBy, userId).eq(DiscountMy::getDiscountCouponId, discountCoupon.getId())
        );

        return EmptyUtil.isNotEmpty(one);
    }


    /**
     * 获取优惠券
     *
     * @return
     */
    @Override
    public List<DiscountCoupon> getDiscountCouponList(DiscountCoupon discountCoupon) {

        CompletableFuture<Optional<List<DiscountCoupon>>> optionalCompletableFuture = CompletableFuture.supplyAsync(() ->

                Optional.ofNullable(this.list(Wrappers.<DiscountCoupon>lambdaQuery()
                        .eq(ToolUtil.isNotEmpty(discountCoupon.getCreateBy()), DiscountCoupon::getCreateBy, discountCoupon.getCreateBy())
                        .eq(!StringUtils.isEmpty(discountCoupon.getGoodsPackageId()), DiscountCoupon::getGoodsPackageId, discountCoupon.getGoodsPackageId())
                        .eq(!EmptyUtil.isEmpty(discountCoupon.getDisCouponType()), DiscountCoupon::getGoodsPackageId, discountCoupon.getGoodsPackageId())
                        .eq(DiscountCoupon::getDelFlag, CommonConstant.STATUS_NORMAL)
                        .ge(DiscountCoupon::getValidityPeriod, new Date())
                        .orderByAsc(DiscountCoupon::getCreateTime))));

        return optionalCompletableFuture.join().orElse(null);
    }


    @Override
    public List<DiscountCouponListVO> getDiscountCouponListByCreateBy(String createBy) {


        DiscountCoupon discountCoupon = new DiscountCoupon();
        discountCoupon.setCreateBy(createBy);

        //获取该商家下所有的优惠券
        List<DiscountCoupon> discountCouponList = this.getDiscountCouponList(discountCoupon);

        List<DiscountCouponListVO> couponListVos = new ArrayList<>();

        discountCouponList.forEach(discountCoupon1 -> {
            DiscountCouponListVO discountCouponListVO = new DiscountCouponListVO();
            ToolUtil.copyProperties(discountCoupon1, discountCouponListVO);


            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            discountCouponListVO.setValidityPeriod(simpleDateFormat.format(discountCoupon1.getValidityPeriod()));

            //登录的用户是否领取
            String principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            if(!"anonymousUser".equals(principal)){

                discountCouponListVO.setIsReceive(this.getDetailById(discountCoupon1));
            }

            couponListVos.add(discountCouponListVO);
        });
        return couponListVos;
    }



    @Override
    public List<DiscountVO> getList(String join) {
        return Optional.ofNullable(ToolUtil.setListToNul(this.listByIds(ToolUtil.splitterStr(join)))).map(discountCouponList -> {
            List<DiscountVO> discountCoupons = discountCouponList.parallelStream().map(discountCoupon -> {
                DiscountVO discountCouponListVO = new DiscountVO();
                ToolUtil.copyProperties(discountCoupon, discountCouponListVO);
                return discountCouponListVO;
            }).collect(Collectors.toList());
            return discountCoupons;
        }).orElse(null);
    }


/**********************************************后端管理接口************************************************************/

    /**
     * 后端获取优惠券列表
     *
     * @return
     */
    @Override
    public List PcGetDiscountCouponList(DiscountCouponDTO discountCouponDTO) {

        //1.查询用户角色
        String roleName = iGoodService.getRoleUserList(securityUtil.getCurrUser().getId());

        //判空
        if (ToolUtil.isNotEmpty(roleName)) {

            List<DiscountCoupon> discountCoupons = new ArrayList<>();

            //2.判断角色管理员还是商家或者个人商家
            //管理员
            if (roleName.equals(CommonConstant.ADMIN)) {
                discountCoupons = this.list(new QueryWrapper<DiscountCoupon>().lambda()
                        .eq((ToolUtil.isNotEmpty(discountCouponDTO.getId())), DiscountCoupon::getId, discountCouponDTO.getId())
                        .eq((ToolUtil.isNotEmpty(discountCouponDTO.getDisCouponType())), DiscountCoupon::getDisCouponType, discountCouponDTO.getDisCouponType())
                        .orderByDesc(DiscountCoupon::getCreateTime)
                );

                //商家或者个人商家
            } else if (roleName.equals(CommonConstant.STORE) || roleName.equals(CommonConstant.PER_STORE)) {

                discountCoupons = this.list(new QueryWrapper<DiscountCoupon>().lambda().eq((ToolUtil.isNotEmpty(discountCouponDTO.getId())), DiscountCoupon::getId, discountCouponDTO.getId())
                        .eq((ToolUtil.isNotEmpty(discountCouponDTO.getDisCouponType())), DiscountCoupon::getDisCouponType, discountCouponDTO.getDisCouponType())
                        .eq(DiscountCoupon::getCreateBy, securityUtil.getCurrUser().getId())
                        .orderByDesc(DiscountCoupon::getCreateTime)
                );
            }

            //3.优惠券不为空
            if (ToolUtil.isNotEmpty(discountCoupons)) {

                List<PcGetDiscountCouponListVO> pcGetDiscountList = discountCoupons.parallelStream().map(discountCoupon -> {

                    PcGetDiscountCouponListVO pc = new PcGetDiscountCouponListVO();

                    ToolUtil.copyProperties(discountCoupon, pc);
                    //店铺名称
                    pc.setShopName(Optional.ofNullable(userMapper.selectById(discountCoupon.getCreateBy())).map(User::getShopName).orElse("-"))
                            //商品名称
                            .setGoodsName(Optional.ofNullable(goodMapper.selectById(discountCoupon.getGoodsPackageId())).map(Good::getGoodName).orElse("-"))
                            //创建人名称
                            .setCreateName(Optional.ofNullable(userMapper.selectById(discountCoupon.getCreateBy())).map(User::getNickName).orElse("-"));

                    //优惠券是否过期
                    int time = discountCoupon.getValidityPeriod().compareTo(new Date());

                    if(time>0){
                        pc.setPastDue(BooleanTypeEnum.NO);
                    }else {
                        pc.setPastDue(BooleanTypeEnum.YES);
                    }

                    return pc;
                }).filter(pcVO -> pcVO.getCreateName().contains(ToolUtil.isNotEmpty(discountCouponDTO.getCreateName()) ? discountCouponDTO.getCreateName() : pcVO.getCreateName()))

                        .collect(Collectors.toList());


                return pcGetDiscountList;
            }


        }


        return null;
    }



    /**
     * 获取系统的优惠券
     *
     * @return
     */
    @Override
    public List<PlatformDiscountCouponVO> getPlatformDiscountCoupon() {

        //查询平台发布的优惠券
        return Optional.ofNullable(this.list(new QueryWrapper<DiscountCoupon>().lambda()
                .eq(DiscountCoupon::getDisCouponType, DisCouponTypeEnum.ALL_SHOP)
                .eq(DiscountCoupon::getDelFlag, CommonConstant.STATUS_NORMAL)
                .eq(DiscountCoupon::getUsableRangeTypeEnum, UsableRangeTypeEnum.CLASSIFY)
                .ge(DiscountCoupon::getValidityPeriod, new Date())
                .orderByDesc(DiscountCoupon::getCreateTime)

        )).map(discountCoupons -> {
            List<PlatformDiscountCouponVO> platformDiscountCouponVOS = discountCoupons.parallelStream().flatMap(discountCoupon -> {

                PlatformDiscountCouponVO p = new PlatformDiscountCouponVO();
                ToolUtil.copyProperties(discountCoupon, p);

                //查询商家是否参加这个优惠券活动
                DiscountShop discountShop = discountShopMapper.selectOne(new QueryWrapper<DiscountShop>().lambda()
                        .eq(DiscountShop::getDiscountId, discountCoupon.getId())
                        .eq(DiscountShop::getCreateBy, securityUtil.getCurrUser().getId())
                        .last("limit 1"));

                if (ToolUtil.isNotEmpty(discountShop)) {
                        p.setJoin(CommonConstant.YES);

                } else {

                        p.setJoin(CommonConstant.NO);
                }

                return Stream.of(p);
                //筛选出分类和商家一样的
            }).filter(platformDiscountCouponVO -> platformDiscountCouponVO.getUsableRangeId()
                    .equals(Optional.ofNullable(userMapper.selectById(securityUtil.getCurrUser().getId()))
                            .map(User::getClassId).orElse(null)))

                    .collect(Collectors.toList());
            return platformDiscountCouponVOS;
        }).orElse(null);


    }


    /**
     * App获取平台优惠券
     * @return
     */
    @Override
    public List<AppDiscountCouponListVO> AppDiscountCouponList(DiscountCouponDTO discountCouponDTO) {

        return Optional.ofNullable(ToolUtil.setListToNul(this.list(new QueryWrapper<DiscountCoupon>().lambda()
                .eq(DiscountCoupon::getDisCouponType, DisCouponTypeEnum.ALL_SHOP)
                .eq(DiscountCoupon::getDelFlag, CommonConstant.STATUS_NORMAL)
                .ge(DiscountCoupon::getValidityPeriod, new Date())
                .orderByDesc(DiscountCoupon::getCreateTime))))
        .map(discountCoupons -> discountCoupons.parallelStream().flatMap(discountCoupon -> {
            AppDiscountCouponListVO appDiscountCouponListVO = new AppDiscountCouponListVO();

            ToolUtil.copyProperties(discountCoupon,appDiscountCouponListVO);
            return Stream.of(appDiscountCouponListVO);
        }).collect(Collectors.toList())).orElse(null);
    }


}