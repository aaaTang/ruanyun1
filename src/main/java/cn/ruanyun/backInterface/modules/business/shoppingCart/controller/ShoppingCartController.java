package cn.ruanyun.backInterface.modules.business.shoppingCart.controller;

import cn.ruanyun.backInterface.common.exception.RuanyunException;
import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.business.shoppingCart.VO.ShoppingCartVO;
import cn.ruanyun.backInterface.modules.business.shoppingCart.entity.ShoppingCart;
import cn.ruanyun.backInterface.modules.business.shoppingCart.service.IShoppingCartService;
import com.alibaba.druid.util.StringUtils;
import com.google.common.collect.Maps;
import io.swagger.annotations.*;
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
@Api(tags = "购物车管理接口")
@RequestMapping("/ruanyun/shoppingCart")
@Transactional
public class ShoppingCartController {

    @Autowired
    private IShoppingCartService iShoppingCartService;


    @PostMapping("/insertShoppingCart")
    @ApiOperation(value = "加入购物车")
    public Result<Object> insertShoppingCart(ShoppingCart shoppingCart) {

        try {

            if (ToolUtil.isEmpty(shoppingCart.getBuyCount())) {

                return new ResultUtil<>().setErrorMsg(207, "请输入购买数量！");
            }
            iShoppingCartService.insertShoppingCart(shoppingCart);
            return new ResultUtil<>().setSuccessMsg("添加购物车成功！");
        }catch (RuanyunException e) {

            throw new RuanyunException(e.getMsg());
        }
    }

    @PostMapping("/removeShoppingCart")
    @ApiOperation(value = "移除购物车")
    public Result<Object> removeShoppingCart(String ids) {

        try {

            iShoppingCartService.removeShoppingCart(ids);
            return new ResultUtil<>().setSuccessMsg("添加购物车成功！");
        }catch (RuanyunException e) {

            throw new RuanyunException(e.getMsg());
        }
    }


    @PostMapping("/getMyShoppingCart")
    @ApiOperation(value = "获取我的购物车数据")
    public Result<DataVo<ShoppingCartVO>> getMyShoppingCart(PageVo pageVo) {

        return iShoppingCartService.getMyShoppingCart(pageVo);
    }


    @PostMapping("/getGoodsCartNum")
    @ApiOperation(value = "获取购物车数量")
    public Result<Object> getGoodsCartNum() {

        return new ResultUtil<>().setData(iShoppingCartService.getGoodsCartNum(), "获取购物车数量成功！");
    }


    @PostMapping("/changeShopCartNum")
    @ApiOperation(value = "修改购物车数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "购物车id", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "count", value = "数量", dataType = "string", paramType = "query")
    })
    public Result<Object> changeShopCartNum(String id, Integer count) {

        return iShoppingCartService.changeShopCartNum(id, count);
    }
}
