package cn.ruanyun.backInterface.modules.business.shoppingCart.serviceimpl;


import cn.ruanyun.backInterface.common.utils.EmptyUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.good.VO.AppGoodOrderVO;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import cn.ruanyun.backInterface.modules.business.itemAttrKey.pojo.ItemAttrKey;
import cn.ruanyun.backInterface.modules.business.itemAttrKey.service.IItemAttrKeyService;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.pojo.ItemAttrVal;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.service.IItemAttrValService;
import cn.ruanyun.backInterface.modules.business.shoppingCart.VO.ShoppingCartVO;
import cn.ruanyun.backInterface.modules.business.shoppingCart.entity.ShoppingCart;
import cn.ruanyun.backInterface.modules.business.shoppingCart.mapper.ShoppingCartMapper;
import cn.ruanyun.backInterface.modules.business.shoppingCart.service.IShoppingCartService;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.pojo.SizeAndRolor;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.service.ISizeAndRolorService;
import com.alibaba.druid.util.StringUtils;
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
    @Autowired
    private IItemAttrValService itemAttrValService;

    /**
     * 加入购物车
     *
     * @param shoppingCart
     */
    @Override
    public void insertShoppingCart(ShoppingCart shoppingCart) {


        if (!StringUtils.isEmpty(shoppingCart.getGoodId())){
            AppGoodOrderVO appGoodOrder = goodService.getAppGoodOrder(shoppingCart.getGoodId(), shoppingCart.getAttrSymbolPath(),null,null);
            shoppingCart.setTotalPrice(new BigDecimal(shoppingCart.getCount()).multiply(appGoodOrder.getGoodNewPrice()));
            shoppingCart.setGoodNewPrice(appGoodOrder.getGoodNewPrice());
            shoppingCart.setBuyState(appGoodOrder.getBuyState());
            shoppingCart.setLeaseState(appGoodOrder.getLeaseState());
        }

        shoppingCart.setCreateBy(securityUtil.getCurrUser().getId());

        CompletableFuture.runAsync(() -> {
            if (ToolUtil.isNotEmpty(getShopCartSame(shoppingCart))) {
                ShoppingCart shoppingCartOld = getShopCartSame(shoppingCart);
                shoppingCartOld.setCount(shoppingCartOld.getCount()+shoppingCart.getCount())
                        .setTotalPrice(shoppingCart.getGoodNewPrice().multiply(BigDecimal.valueOf(shoppingCartOld.getCount()+shoppingCart.getCount())));
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
    public Result<Object> updateShoppingCart(ShoppingCart shoppingCart) {

        shoppingCart.setUpdateBy(securityUtil.getCurrUser().getId());
        CompletableFuture.runAsync(() -> {
            ShoppingCart shoppingCartOld = this.getById(shoppingCart.getId());
            ToolUtil.copyProperties(shoppingCart,shoppingCartOld);
            shoppingCartOld.setTotalPrice(shoppingCartOld.getGoodNewPrice().multiply(BigDecimal.valueOf(shoppingCart.getCount())));
            this.updateById(shoppingCartOld);
        });

      return new ResultUtil<>().setData(this.getById(shoppingCart.getId()),"获取我的购物车数据成功！");
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

                    if (!StringUtils.isEmpty(shoppingCart.getAttrSymbolPath())){
                        //处理属性信息
                        List<String> itemAttrKeys = itemAttrValService.listByIds(ToolUtil.splitterStr(shoppingCart.getAttrSymbolPath())).stream().map(ItemAttrVal::getAttrValue).collect(Collectors.toList());
                        shoppingCartVO.setItemAttrKeys(itemAttrKeys);
                    }

                    Good byId = goodService.getById(shoppingCart.getGoodId());
                    if (EmptyUtil.isNotEmpty(byId)){
                        shoppingCartVO.setName(byId.getGoodName())
                        .setGoodPrice(byId.getGoodNewPrice())
                        .setPic(byId.getGoodPics());
                    }
                    if (!StringUtils.isEmpty(shoppingCart.getAttrSymbolPath())){
                        //处理价格 如果这个属性配置了新的价格，就用新的价格
                        SizeAndRolor one = iSizeAndRolorService.getOne(Wrappers.<SizeAndRolor>lambdaQuery()
                                .eq(SizeAndRolor::getAttrSymbolPath, shoppingCart.getAttrSymbolPath())
                                .eq(SizeAndRolor::getGoodsId, shoppingCart.getGoodId()));
                        if (EmptyUtil.isNotEmpty(one)){
                            ToolUtil.copyProperties(one,shoppingCartVO);
                        }
                    }
                    ToolUtil.copyProperties(shoppingCart,shoppingCartVO);
                    return Stream.of(shoppingCartVO);
                }).collect(Collectors.toList())).orElse(null));

        return shopCartVos.join();
    }

    /**
     * 获取购物车数量
     * @return
     */
    @Override
    public Integer getGoodsCartNum() {
        List<ShoppingCart> shoppingCart = this.list(new QueryWrapper<ShoppingCart>().lambda().eq(ShoppingCart::getCreateBy,securityUtil.getCurrUser().getId()));
        return Optional.ofNullable(shoppingCart.size()).orElse(null);
    }


    @Override
    public void changeCount(String id,Integer count) {
        CompletableFuture.runAsync(() -> {
            ShoppingCart shoppingCartOld = this.getById(id);
            shoppingCartOld.setCount(count);
            shoppingCartOld.setTotalPrice(getUpdatePrice(shoppingCartOld,shoppingCartOld.getCount().toString()));
            this.updateById(shoppingCartOld);
        });
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
                .eq(ShoppingCart::getAttrSymbolPath,shoppingCart.getAttrSymbolPath()));
    }

}