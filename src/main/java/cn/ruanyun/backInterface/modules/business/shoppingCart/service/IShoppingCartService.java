package cn.ruanyun.backInterface.modules.business.shoppingCart.service;


import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.business.shoppingCart.VO.ShoppingCartVO;
import cn.ruanyun.backInterface.modules.business.shoppingCart.entity.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 购物车接口
 * @author fei
 */
public interface IShoppingCartService extends IService<ShoppingCart> {

    /**
     * 加入购物车
     * @param shoppingCart shoppingCart
     */
    void insertShoppingCart(ShoppingCart shoppingCart);

    /**
     * 移除购物车
     * @param ids ids
     */
    void removeShoppingCart(String ids);

    /**
     * 获取我的购物车数据
     * @param pageVo 分页数据
     * @return ShoppingCartVO
     */
    Result<DataVo<ShoppingCartVO>> getMyShoppingCart(PageVo pageVo);

    /**
     * 获取购物车数量
     * @return return
     */
    Integer getGoodsCartNum();

    /**
     * 修改购物车数量
     * @param id 购物车id
     * @param count 购物车商品数量
     * @return Object
     */
    Result<Object> changeShopCartNum(String id, Integer count);

}