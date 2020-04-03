package cn.ruanyun.backInterface.modules.business.shoppingCart.service;


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
     * @param shoppingCart
     */
    void insertShoppingCart(ShoppingCart shoppingCart);

    /**
     * 移除购物车
     * @param ids
     */
    void removeShoppingCart(String[] ids);


    /**
     * 编辑购物车
     * @param shoppingCart
     */
    void updateShoppingCart(ShoppingCart shoppingCart);

    /**
     * 获取我的购物车数据
     * @return
     */
    List<ShoppingCartVO> shoppingCartVOList(String ids);

}