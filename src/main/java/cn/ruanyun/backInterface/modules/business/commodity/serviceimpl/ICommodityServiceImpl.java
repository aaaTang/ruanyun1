package cn.ruanyun.backInterface.modules.business.commodity.serviceimpl;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.Specifications.SpecificationsVO.SpecificationsListVO;
import cn.ruanyun.backInterface.modules.business.Specifications.mapper.SpecificationsMapper;
import cn.ruanyun.backInterface.modules.business.Specifications.pojo.Specifications;
import cn.ruanyun.backInterface.modules.business.commodity.VO.CommodityDetailsVO;
import cn.ruanyun.backInterface.modules.business.commodity.VO.CommodityListVO;
import cn.ruanyun.backInterface.modules.business.commodity.mapper.CommodityMapper;
import cn.ruanyun.backInterface.modules.business.commodity.pojo.Commodity;
import cn.ruanyun.backInterface.modules.business.commodity.service.ICommodityService;
import cn.ruanyun.backInterface.modules.business.discountCoupon.VO.DiscountCouponListVO;
import cn.ruanyun.backInterface.modules.business.discountCoupon.mapper.DiscountCouponMapper;
import cn.ruanyun.backInterface.modules.business.discountCoupon.pojo.DiscountCoupon;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;

import javax.annotation.Resource;


/**
 * 商品管理接口实现
 * @author zhu
 */
@Slf4j
@Service
@Transactional
public class ICommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements ICommodityService {


       @Autowired
       private SecurityUtil securityUtil;

       @Resource
       private CommodityMapper commodityMapper;

       @Resource
       private DiscountCouponMapper discountCouponMapper;

       @Resource
       private SpecificationsMapper specificationsMapper;

       @Override
       public void insertOrderUpdateCommodity(Commodity commodity) {

           if (ToolUtil.isEmpty(commodity.getCreateBy())) {

                       commodity.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       commodity.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(commodity)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeCommodity(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    /**
     * 获取获取商品详情成功
     * @param ids
     * @return
     */
      @Override
      public Result<Object> AppCommodityDetails(String ids){

          Commodity commodity = this.getById(ids);
          CommodityDetailsVO commodityDetailsVO = new CommodityDetailsVO();
          BeanUtils.copyProperties(commodity,commodityDetailsVO);
          return new ResultUtil<>().setData(commodityDetailsVO);
      }

    /**
     * 获取商品列表
     */
    @Override
    public List<CommodityListVO> CommodityList(Commodity commodity) {
            List<Commodity> list= this.list(new QueryWrapper<Commodity>().lambda()
                    .like(ToolUtil.isNotEmpty(commodity.getCommodityName()),Commodity::getCommodityName,commodity.getCommodityName())
                    .eq(ToolUtil.isNotEmpty(commodity.getClassificationId()),Commodity::getClassificationId,commodity.getClassificationId())
            );//获取全部商品

            List<CommodityListVO> commodityList =list.parallelStream().map(cd -> {
                CommodityListVO commodityListVO = new CommodityListVO();


                //查詢商品对应的优惠卷
                List<DiscountCoupon> discountCoupons =
                        discountCouponMapper.selectList(new QueryWrapper<DiscountCoupon>().lambda()
                                .eq(DiscountCoupon::getGoodsPackageId,cd.getId()));

                List<DiscountCouponListVO> discountCouponListVOS = new ArrayList<>();
                discountCoupons.forEach(discountCoupon -> {
                    DiscountCouponListVO discountCouponListVO = new DiscountCouponListVO();
                    ToolUtil.copyProperties(discountCoupon,discountCouponListVO);
                    discountCouponListVOS.add(discountCouponListVO);
                });

                commodityListVO.setDiscountCouponList(ToolUtil.setListToNul(discountCouponListVOS));

                //查詢商品对应的规格
                List<Specifications> specifications =
                        specificationsMapper.selectList(new QueryWrapper<Specifications>().lambda().eq(Specifications::getCommodityId,cd.getId()));

                List<SpecificationsListVO> specificationsListVO =new ArrayList<>();
                specifications.forEach(sf -> {
                    SpecificationsListVO specificationsList = new SpecificationsListVO();
                    ToolUtil.copyProperties(sf,specificationsList);
                    specificationsListVO.add(specificationsList);
                });

                commodityListVO.setSpecifications(ToolUtil.setListToNul(specificationsListVO));


                ToolUtil.copyProperties(cd,commodityListVO);
                return commodityListVO;
            }).collect(Collectors.toList());

        return commodityList;

    }


}