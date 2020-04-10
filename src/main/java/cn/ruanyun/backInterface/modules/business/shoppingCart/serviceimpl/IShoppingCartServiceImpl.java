package cn.ruanyun.backInterface.modules.business.shoppingCart.serviceimpl;


import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import cn.ruanyun.backInterface.modules.business.shoppingCart.VO.ShoppingCartVO;
import cn.ruanyun.backInterface.modules.business.shoppingCart.entity.ShoppingCart;
import cn.ruanyun.backInterface.modules.business.shoppingCart.mapper.ShoppingCartMapper;
import cn.ruanyun.backInterface.modules.business.shoppingCart.service.IShoppingCartService;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.service.ISizeAndRolorService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 购物车接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements IShoppingCartService {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private IGoodService goodService;

    @Autowired
    private ISizeAndRolorService iSizeAndRolorService;

    /**
     * 加入购物车
     *
     * @param shoppingCart
     */
    @Override
    public void insertShoppingCart(ShoppingCart shoppingCart) {

        shoppingCart.setCreateBy(securityUtil.getCurrUser().getId());
        CompletableFuture.runAsync(() -> {
            if (ToolUtil.isNotEmpty(getShopCartSame(shoppingCart))) {
                ShoppingCart shoppingCartOld = getShopCartSame(shoppingCart);
                shoppingCartOld.setCount(shoppingCartOld.getCount()+shoppingCart.getCount())
                        .setTotalPrice(getUpdatePrice(shoppingCartOld,shoppingCartOld.getCount().toString()));
                this.updateById(shoppingCartOld);

            } else {
                this.save(shoppingCart);
            }

        });
    }

    /**
     * 移除购物车
     *
     * @param ids
     */
    @Override
    public void removeShoppingCart(String[] ids) {

        CompletableFuture.runAsync(() -> {
                for(String id : ids){this.removeById(id);}
                });
    }

    /**
     * 编辑购物车
     *
     * @param shoppingCart
     */
    @Override
    public void updateShoppingCart(ShoppingCart shoppingCart) {

        shoppingCart.setUpdateBy(securityUtil.getCurrUser().getId());
        CompletableFuture.runAsync(() -> {
            ShoppingCart shoppingCartOld = this.getById(shoppingCart.getId());
            ToolUtil.copyProperties(shoppingCart,shoppingCartOld);
            shoppingCartOld.setTotalPrice(getUpdatePrice(shoppingCartOld,shoppingCart.getCount().toString()));
            this.updateById(shoppingCartOld);
        });
    }

    /**
     * 获取我的购物车数据
     *
     * @return
     */
    @Override
    public List<ShoppingCartVO> shoppingCartVOList(String ids) {

        String userId = securityUtil.getCurrUser().getId();
        //1.获取原始数据
        CompletableFuture<Optional<List<ShoppingCart>>> shopCarts = CompletableFuture.supplyAsync(() ->
                Optional.ofNullable(getShopCartList(ids,userId)));

        //2.获取封装数据
        CompletableFuture<List<ShoppingCartVO>> shopCartVos = shopCarts.thenApplyAsync(shoppingCarts ->
                shoppingCarts.map(shoppingCarts1 -> shoppingCarts1.parallelStream().flatMap(shoppingCart -> {

                    ShoppingCartVO shoppingCartVO = new ShoppingCartVO();
                    ToolUtil.copyProperties(shoppingCart,shoppingCartVO);

                    shoppingCartVO.setPic(goodService.getPicLimit1(shoppingCart.getGoodId()))
                            .setSizeName(iSizeAndRolorService.getSizeName(shoppingCart.getSizeId()))
                            .setColorName(iSizeAndRolorService.getColorName(shoppingCart.getColorId()))
                            .setName(goodService.getGoodName(shoppingCart.getGoodId()))
                            .setGoodPrice(goodService.getGoodPrice(shoppingCart.getGoodId()))
                            .setInventory(iSizeAndRolorService.getInventory(shoppingCart.getSizeId()));

                    return Stream.of(shoppingCartVO);
                }).collect(Collectors.toList())).orElse(null));

        return shopCartVos.join();
    }

    @Override
    public Integer getGoodsCartNum() {
        List<ShoppingCart> shoppingCart = this.list(new QueryWrapper<ShoppingCart>().lambda().eq(ShoppingCart::getCreateBy,securityUtil.getCurrUser().getId()));
        return Optional.ofNullable(shoppingCart.size()).orElse(null);
    }


    /**
     * 获取基本数据
     * @param ids
     * @param userId
     * @return
     */
    public List<ShoppingCart> getShopCartList(String ids,String userId) {

        if (ToolUtil.isNotEmpty(ids)) {

            return ToolUtil.setListToNul(this.listByIds(ToolUtil.splitterStr(ids)));
        }

        return ToolUtil.setListToNul(this.list(Wrappers.<ShoppingCart>lambdaQuery()
                .eq(ShoppingCart::getCreateBy,userId)
                .orderByDesc(ShoppingCart::getCreateTime)));
    }

    /**
     * 计算价格
     * @param shoppingCart
     * @param count
     * @return
     */
    public  BigDecimal getUpdatePrice(ShoppingCart shoppingCart,String count) {

        return shoppingCart.getTotalPrice().divide(new BigDecimal(shoppingCart.getCount()), RoundingMode.HALF_EVEN)
                .multiply(new BigDecimal(count));
    }


    /**
     * 获取相同的购物车数据
     * @param shoppingCart
     * @return
     */
    public ShoppingCart getShopCartSame(ShoppingCart shoppingCart) {

       return this.getOne(Wrappers.<ShoppingCart>lambdaQuery()
                .eq(ShoppingCart::getCreateBy,shoppingCart.getCreateBy())
                .eq(ShoppingCart::getGoodId,shoppingCart.getGoodId())
                .eq(ShoppingCart::getSizeId,shoppingCart.getSizeId())
                .eq(ShoppingCart::getColorId,shoppingCart.getColorId()));
    }

}