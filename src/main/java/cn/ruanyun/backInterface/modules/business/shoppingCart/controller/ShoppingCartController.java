package cn.ruanyun.backInterface.modules.business.shoppingCart.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.shoppingCart.entity.ShoppingCart;
import cn.ruanyun.backInterface.modules.business.shoppingCart.service.IShoppingCartService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author fei
 */
@Slf4j
@RestController
@Api(description = "购物车管理接口")
@RequestMapping("/ruanyun/shoppingCart")
@Transactional
public class ShoppingCartController {

    @Autowired
    private IShoppingCartService iShoppingCartService;

    @PostMapping("/insertShoppingCart")
    public Result<Object> insertShoppingCart(ShoppingCart shoppingCart) {

        iShoppingCartService.insertShoppingCart(shoppingCart);
        return new ResultUtil<>().setSuccessMsg("添加购物车成功！");
    }

    /**
     * 移除购物车
     * @param ids
     * @return
     */
    @PostMapping("/removeShoppingCart")
    public Result<Object> removeShoppingCart(String[] ids) {

        iShoppingCartService.removeShoppingCart(ids);
        return new ResultUtil<>().setSuccessMsg("移除购物车成功！");
    }

    /**
     * 更新购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/updateShoppingCart")
    public Result<Object> updateShoppingCart(ShoppingCart shoppingCart) {

        iShoppingCartService.updateShoppingCart(shoppingCart);
        return new ResultUtil<>().setErrorMsg("更新购物车成功！");
    }


    /**
     * 获取我的购物车数据
     * @param pageVo
     * @return
     */
    @PostMapping("/shoppingCartVOList")
    public Result<Object> shoppingCartVOList(PageVo pageVo, String ids) {

        return Optional.ofNullable(iShoppingCartService.shoppingCartVOList(ids))
                .map(shoppingCartVos -> {

                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",shoppingCartVos.size());
                    result.put("data", PageUtil.listToPage(pageVo,shoppingCartVos));

                    return new ResultUtil<>().setData(result,"获取我的购物车数据成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }
   /* *
     * 获取我的购物车数据
     * @param pageVo
     * @return
     * */

    @PostMapping("/changeCount")
    public Result<Object> changeCount(String id,Integer count) {
        iShoppingCartService.changeCount(id,count);
        return new ResultUtil<>().setData(null,"修改成功！");
    }

}
