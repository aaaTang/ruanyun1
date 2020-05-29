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
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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


    private static final Joiner JOINER = Joiner.on(",").skipNulls();

    @Override
    public List<DiscountVO> getMyCoupon(Integer status) {


        return Optional.ofNullable(ToolUtil.setListToNul(
                this.list(Wrappers.<DiscountMy>lambdaQuery()
                         .eq(DiscountMy::getCreateBy,securityUtil.getCurrUser().getId())
                .eq(ToolUtil.isNotEmpty(status) && status != 2, DiscountMy::getStatus,status)
                .eq(ToolUtil.isNotEmpty(status) && status == 2, DiscountMy::getStatus, CommonConstant.STATUS_NORMAL)
                .le(ToolUtil.isNotEmpty(status) && status == 2, DiscountMy::getDiscountCouponTime,new Date())

                        .orderByDesc(DiscountMy::getCreateTime)))
        ).map(list ->{
            List<DiscountVO> discountVOS = list.parallelStream().flatMap(discountMy -> {
                DiscountVO byId = new DiscountVO();
                ToolUtil.copyProperties(discountCouponMapper.selectById(discountMy.getDiscountCouponId()) ,byId);
                byId.setStatus(discountMy.getStatus());


                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                byId.setValidityPeriod(simpleDateFormat.format(discountMy.getDiscountCouponTime()));

                return Stream.of(byId);
            }).collect(Collectors.toList());
            return discountVOS;
        }).orElse(null);
    }


    public DiscountVO getDetailById(String id){
        return Optional.ofNullable(this.getOne(Wrappers.<DiscountMy>lambdaQuery().eq(DiscountMy::getDiscountCouponId,id)))
                .map(discountMy -> {
                    DiscountVO discountVO = new DiscountVO();
                    DiscountCoupon byId = discountService.getById(discountMy.getDiscountCouponId());
                    ToolUtil.copyProperties(byId,discountVO);
                    discountVO.setDisCouponType(byId.getDisCouponType());
                    discountVO.setStatus(discountMy.getStatus());
                    return discountVO;
                }).orElse(null);
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
                        DiscountVO discountVO = new DiscountVO();
                        if (EmptyUtil.isNotEmpty(byId)){
                            //全场 价格达到标准
                            int i = multiply.compareTo(byId.getFullMoney());
                            if (byId.getDisCouponType().getCode() == DisCouponTypeEnum.ALL_USE.getCode() && i != -1 && byId.getCreateBy().equals(createBy)){
                                ToolUtil.copyProperties(byId,discountVO);
                                discountVO.setDisCouponType(byId.getDisCouponType());
                                discountVO.setValidityPeriod(discountVO.getValidityPeriod().substring(0,discountVO.getValidityPeriod().indexOf(" ")));
                                //指定商品
                            }else if(byId.getDisCouponType().getCode() == DisCouponTypeEnum.ONE_PRODUCT.getCode() && byId.getGoodsPackageId().equals(goodId)  && i != -1){
                                ToolUtil.copyProperties(byId,discountVO);
                                discountVO.setValidityPeriod(discountVO.getValidityPeriod().substring(0,discountVO.getValidityPeriod().indexOf(" ")));
                            }else {
                                return null;
                            }
                        }
                        return discountVO;
                    }).collect(Collectors.toList());
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
                    discountMy.setDiscountCouponTime(Optional.ofNullable(discountCouponMapper.selectById(couponId)).map(DiscountCoupon::getValidityPeriod).orElse(new Date()));
                    this.save(discountMy);
                    return new ResultUtil<>().setSuccessMsg("领取优惠券成功！");
                });
    }

    @Override
    public List<DiscountVO> getCanUseCoupon( String productId) {
        /*return Optional.ofNullable(getCanUseCouponId(securityUtil.getCurrUser().getId(),productId)).map(strings -> discountService.getList(JOINER.join(strings)))
                .orElse(null);*/
       return  null;
    }

    public List<String> getCanUseCouponId(String userId, String productId){
       //1.先找出自己的优惠券
        List<DiscountMy> myCoupons = this.list(Wrappers.<DiscountMy>lambdaQuery()
                .eq(DiscountMy::getCreateBy,userId));
        //2.过滤出能用的优惠券
        ;
     /*   return Optional.ofNullable(ToolUtil.setListToNul(myCoupons)).map(myCouponsList -> myCouponsList.parallelStream()
                .takeWhile(myCoupon -> discountService.getOne(Wrappers.<DiscountCoupon>lambdaQuery()
                        .eq(DiscountCoupon::getId,myCoupons.get(1).getDiscountCouponId())).equals(Disabled.Yes.getCode()))
                .takeWhile(myCoupon -> judgeCouponInProduct(productId,myCoupon.())).flatMap(myCoupon -> Stream.of(myCoupon.getCode()))
                .collect(Collectors.toList())).orElse(null);*/
     return  null;
    }

    /**
     * 判断此优惠券是否可用
     * @param productId
     * @param couponId
     * @return
     */
    public Boolean judgeCouponInProduct(String productId, String couponId){
      return Optional.ofNullable(discountService.getOne(Wrappers.<DiscountCoupon>lambdaQuery()
                .eq(DiscountCoupon::getId,productId)))
                .map(productCoupon -> true).orElse(false);
    }

}