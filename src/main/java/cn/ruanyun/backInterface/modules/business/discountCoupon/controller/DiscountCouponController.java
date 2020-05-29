package cn.ruanyun.backInterface.modules.business.discountCoupon.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.discountCoupon.DTO.DiscountCouponDTO;
import cn.ruanyun.backInterface.modules.business.discountCoupon.pojo.DiscountCoupon;
import cn.ruanyun.backInterface.modules.business.discountCoupon.service.IDiscountCouponService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author fei
 * 优惠券管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/discountCoupon")
@Transactional
public class DiscountCouponController {

    @Autowired
    private IDiscountCouponService iDiscountCouponService;


   /**
     * 更新或者插入数据
     * @param discountCoupon
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateDiscountCoupon")
    public Result<Object> insertOrderUpdateDiscountCoupon(DiscountCoupon discountCoupon){
        try {
            iDiscountCouponService.insertOrderUpdateDiscountCoupon(discountCoupon);
            return new ResultUtil<>().setSuccessMsg("插入或者更新成功!");
        }catch (Exception e) {
            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 移除数据
     * @param ids
     * @return
    */
    @PostMapping(value = "/removeDiscountCoupon")
    public Result<Object> removeDiscountCoupon(String ids){
        try {
            iDiscountCouponService.removeDiscountCoupon(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {
            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

    /**
     * 优惠券详情
     * @param id
     * @return
     */
    @PostMapping("/getDiscountCouponDetail")
    public Result<Object> getDiscountCouponDetail(String id) {
        return new ResultUtil<>().setData(iDiscountCouponService.getById(id),"获取详情成功！");
    }


    /**
     * 按商品获取优惠券
     * @param goodsPackageId
     * @return
     */
    @PostMapping("/getDiscountCouponListByGoodsPackageId")
    public Result<Object> getDiscountCouponListByGoodsPackageId(String goodsPackageId) {
        return Optional.ofNullable(iDiscountCouponService.getDiscountCouponListByGoodsPackageId(goodsPackageId))
                .map(discountCouponList -> new ResultUtil<>().setData(discountCouponList,"获取列表成功！"))
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }


    /**
     * 按商家获取优惠券
     * @param createBy
     * @return
     */
    @PostMapping("/getDiscountCouponListByCreateBy")
    public Result<Object> getDiscountCouponListByCreatedId(String createBy) {
        return Optional.ofNullable(iDiscountCouponService.getDiscountCouponListByCreateBy(createBy))
                .map(discountCouponList -> new ResultUtil<>().setData(discountCouponList,"获取列表成功！"))
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }


    /**
     * 获取小程序用户可以领的优惠券
     */
    @PostMapping("/getDiscountCoupon")
    public Result<Object> getDiscountCoupon() {
        return Optional.ofNullable(iDiscountCouponService.getPlatformDiscountCoupon())
                .map(discountCouponList -> new ResultUtil<>().setData(discountCouponList,"获取列表成功！"))
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }


    /**
     * 获取系统的优惠券
     * @return
     */
    @PostMapping("/getPlatformDiscountCoupon")
    public Result<Object> getPlatformDiscountCoupon() {
        return Optional.ofNullable(iDiscountCouponService.getPlatformDiscountCoupon())
                .map(platformDiscountCoupon -> new ResultUtil<>().setData(platformDiscountCoupon,"获取列表成功！"))
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }



    /************************************************后端管理接口********************************************************/


    /**
     * 后端获取优惠券列表
     * @return
     */
    @PostMapping("/PcGetDiscountCouponList")
    public Result<Object> PcGetDiscountCouponList(PageVo pageVo, DiscountCouponDTO discountCouponDTO) {
        return Optional.ofNullable(iDiscountCouponService.PcGetDiscountCouponList(discountCouponDTO))
                .map(pcDiscountCouponList -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",pcDiscountCouponList.size());
                    result.put("data", PageUtil.listToPage(pageVo,pcDiscountCouponList));
                    return new ResultUtil<>().setData(result,"后端获取优惠券列表成功！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }








}
