package cn.ruanyun.backInterface.modules.business.discountMy.serviceimpl;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.DisCouponTypeEnum;
import cn.ruanyun.backInterface.common.utils.EmptyUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.discountCoupon.mapper.DiscountCouponMapper;
import cn.ruanyun.backInterface.modules.business.discountCoupon.pojo.DiscountCoupon;
import cn.ruanyun.backInterface.modules.business.discountCoupon.service.IDiscountCouponService;
import cn.ruanyun.backInterface.modules.business.discountMy.VO.DiscountVO;
import cn.ruanyun.backInterface.modules.business.discountMy.mapper.DiscountMyMapper;
import cn.ruanyun.backInterface.modules.business.discountMy.pojo.DiscountMy;
import cn.ruanyun.backInterface.modules.business.discountMy.service.IDiscountMyService;
import cn.ruanyun.backInterface.modules.business.discountShop.mapper.DiscountShopMapper;
import cn.ruanyun.backInterface.modules.business.discountShop.pojo.DiscountShop;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.xml.transform.sax.SAXResult;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 优惠券接口实现
 * @author wj
 */
@Slf4j
@Service
@Transactional
public class IDiscountMyServiceImpl extends ServiceImpl<DiscountMyMapper, DiscountMy> implements IDiscountMyService {
    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private IDiscountCouponService discountService;
    @Resource
    private DiscountCouponMapper discountCouponMapper;
    @Autowired
    private IGoodService goodService;
    @Resource
    private DiscountShopMapper discountShopMapper;


    private static final Joiner JOINER = Joiner.on(",").skipNulls();

    @Override
    public List<DiscountVO> getMyCoupon(Integer status) {

        List<DiscountVO> discountVOList = new ArrayList<>();

        //我的优惠券
        this.list(Wrappers.<DiscountMy>lambdaQuery()
                .eq(DiscountMy::getCreateBy,securityUtil.getCurrUser().getId())
                .orderByDesc(DiscountMy::getCreateTime)).forEach(discountMy -> {

           DiscountVO discountVO = new DiscountVO();

            DiscountCoupon discountCoupon =  discountCouponMapper.selectById(discountMy.getDiscountCouponId());
           ToolUtil.copyProperties(discountCoupon,discountVO);
           discountVO.setStatus(discountMy.getStatus());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            discountVO.setValidityPeriod(simpleDateFormat.format(discountCoupon.getValidityPeriod()));


                //判断当前时间是否大于订单有效时间
               int time =discountCouponMapper.selectById(discountMy.getDiscountCouponId()).getValidityPeriod().compareTo(new Date());
                    //小于当前时间赋值2
               if(time<=0){discountVO.setStatus(2);}

               if(discountVO.getStatus().equals(status)){
                   discountVOList.add(discountVO);
               }


       });

        return discountVOList;

    }


    /**
     * 处理某个订单下面的商品能够使用的优惠券
     * @param userId
     * @param goodId
     * @param multiply
     * @return
     */
    @Override
    public  List<DiscountVO> getDealCanUseCoupon(String userId, String goodId, BigDecimal multiply) {
        String createBy = goodService.getById(goodId).getCreateBy();
        userId = securityUtil.getCurrUser().getId();
        return Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<DiscountMy>lambdaQuery().eq(DiscountMy::getCreateBy,userId).eq(DiscountMy::getStatus,CommonConstant.STATUS_NORMAL))))
                .map(discountMIES -> {
                    List<DiscountVO> discountVOList = discountMIES.parallelStream().map(discountMy -> {
                        DiscountCoupon byId = discountService.getById(discountMy.getDiscountCouponId());
                        DiscountShop discountShop = discountShopMapper.selectOne(new QueryWrapper<DiscountShop>().lambda()
                                .eq(DiscountShop::getDiscountId,discountMy.getDiscountCouponId())
                                .eq(DiscountShop::getShopId,createBy)
                        );
                        DiscountVO discountVO = new DiscountVO();
                        if (EmptyUtil.isNotEmpty(byId)){

                            //时间戳转换
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");


                            //全场 价格达到标准
                            int i = multiply.compareTo(byId.getFullMoney());
                            if (byId.getDisCouponType().getCode() == DisCouponTypeEnum.ALL_USE.getCode() && i != -1 && byId.getCreateBy().equals(createBy)){

                                ToolUtil.copyProperties(byId,discountVO);
                                discountVO.setDisCouponType(byId.getDisCouponType());
                                discountVO.setValidityPeriod(simpleDateFormat.format(byId.getValidityPeriod()));
                                //指定商品
                            }else if(byId.getDisCouponType().getCode() == DisCouponTypeEnum.ONE_PRODUCT.getCode() && byId.getGoodsPackageId().equals(goodId)  && i != -1){

                                ToolUtil.copyProperties(byId,discountVO);
                                discountVO.setValidityPeriod(simpleDateFormat.format(byId.getValidityPeriod()));

                                //系统优惠券
                            }else if(byId.getDisCouponType().getCode() == DisCouponTypeEnum.ALL_SHOP.getCode()&& ToolUtil.isNotEmpty(discountShop) && i != -1){

                                ToolUtil.copyProperties(byId,discountVO);
                                discountVO.setValidityPeriod(simpleDateFormat.format(byId.getValidityPeriod()));

                            }else {
                                return null;
                            }

                        }
                        return discountVO;
                    }).filter(Objects::nonNull).collect(Collectors.toList());
                    return discountVOList;
                }).orElse(null);

    }



    @Override
    public void changeMyDisCouponStatus(String disCouponId, String userId) {

        Optional.ofNullable(this.getOne(Wrappers.<DiscountMy>lambdaQuery()
        .eq(DiscountMy::getDiscountCouponId, disCouponId)
        .eq(DiscountMy::getCreateBy, userId)))
        .ifPresent(discountMy -> {

            discountMy.setStatus(1);
            this.updateById(discountMy);
        });
    }


    /**
     * 领取优惠券
     * @param couponId
     */
    @Override
    public Result<Object> receiveCoupon(String couponId) {
      return  Optional.ofNullable(this.getOne(Wrappers.<DiscountMy>lambdaQuery().eq(DiscountMy::getDiscountCouponId,couponId)
                .eq(DiscountMy::getCreateBy,securityUtil.getCurrUser().getId())))
                .map(discountMy -> new ResultUtil<>().setErrorMsg(201,"已经领取该优惠券！"))
                .orElseGet(() -> {
                    DiscountMy discountMy = new DiscountMy();
                    discountMy.setDiscountCouponId(couponId);
                    discountMy.setCreateBy(securityUtil.getCurrUser().getId());
                    this.save(discountMy);
                    return new ResultUtil<>().setSuccessMsg("领取优惠券成功！");
                });
    }



}