package cn.ruanyun.backInterface.modules.merchant.trustIdentity.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.merchant.trustIdentity.DTO.TrustIdentityDTO;
import cn.ruanyun.backInterface.modules.merchant.trustIdentity.pojo.TrustIdentity;
import cn.ruanyun.backInterface.modules.merchant.trustIdentity.service.ITrustIdentityService;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

/**
 * @author z
 * 商家信任标识管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/trustIdentity")
@Transactional
public class TrustIdentityController {

    @Autowired
    private ITrustIdentityService iTrustIdentityService;


   /**
     * 更新或者插入数据
     * @param trustIdentity
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateTrustIdentity")
    public Result<Object> insertOrderUpdateTrustIdentity(TrustIdentity trustIdentity){

        try {

            iTrustIdentityService.insertOrderUpdateTrustIdentity(trustIdentity);
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
    @PostMapping(value = "/removeTrustIdentity")
    public Result<Object> removeTrustIdentity(String ids){

        try {

            iTrustIdentityService.removeTrustIdentity(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

    /**
     * 审核申请
     */
    @PostMapping("/checkTrustIdentity")
    public Result<Object> checkTrustIdentity(TrustIdentity trustIdentity){return iTrustIdentityService.checkTrustIdentity(trustIdentity);}



    @PostMapping("/getTrustIdentity")
    @ApiOperation(value = "获取申请信任标识列表")
    public Result<Object> getTrustIdentity(PageVo pageVo, TrustIdentityDTO trustIdentityDTO) {

        return Optional.ofNullable(iTrustIdentityService.getTrustIdentity(trustIdentityDTO))
                .map(trustIdentityVOList -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",trustIdentityVOList.size());
                    result.put("data", PageUtil.listToPage(pageVo,trustIdentityVOList));
                    return new ResultUtil<>().setData(result,"获取申请信任标识列表成功！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }



}
