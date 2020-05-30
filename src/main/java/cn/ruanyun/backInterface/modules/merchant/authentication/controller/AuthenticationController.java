package cn.ruanyun.backInterface.modules.merchant.authentication.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.business.shoppingCart.VO.ShoppingCartVO;
import cn.ruanyun.backInterface.modules.business.storeIncome.vo.StoreIncomeVo;
import cn.ruanyun.backInterface.modules.merchant.authentication.DTO.AuthenticationDTO;
import cn.ruanyun.backInterface.modules.merchant.authentication.pojo.Authentication;
import cn.ruanyun.backInterface.modules.merchant.authentication.service.IAuthenticationService;
import cn.ruanyun.backInterface.modules.merchant.authentication.vo.AuthenticationVo;
import cn.ruanyun.backInterface.modules.merchant.trustIdentity.pojo.TrustIdentity;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author z
 * 商家连锁认证管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/authentication")
@Transactional
public class AuthenticationController {

    @Autowired
    private IAuthenticationService iAuthenticationService;


   /**
     * 更新或者插入数据
     * @param authentication
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateAuthentication")
    public Result<Object> insertOrderUpdateAuthentication(Authentication authentication){

        try {

            iAuthenticationService.insertOrderUpdateAuthentication(authentication);
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
    @PostMapping(value = "/removeAuthentication")
    public Result<Object> removeAuthentication(String ids){

        try {

            iAuthenticationService.removeAuthentication(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }



    /**
     * 审核申请
     */
    @PostMapping("/checkAuthentication")
    public Result<Object> checkAuthentication(Authentication authentication){return iAuthenticationService.checkAuthentication(authentication);}


    @PostMapping("/getAuthentication")
    @ApiOperation(value = "获取申请连锁认证列表")
    public Result<Object> getAuthentication(PageVo pageVo, AuthenticationDTO authenticationDTO) {

        return Optional.ofNullable(iAuthenticationService.getAuthentication(authenticationDTO))
                .map(authenticationvo -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",authenticationvo.size());
                    result.put("data", PageUtil.listToPage(pageVo,authenticationvo));
                    return new ResultUtil<>().setData(result,"获取申请连锁认证列表成功！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }


}
