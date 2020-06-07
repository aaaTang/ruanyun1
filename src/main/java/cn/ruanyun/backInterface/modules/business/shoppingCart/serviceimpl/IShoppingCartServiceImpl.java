package cn.ruanyun.backInterface.modules.business.shoppingCart.serviceimpl;


import cn.ruanyun.backInterface.common.enums.ShopCartTypeEnum;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.business.good.VO.AppGoodOrderVO;
import cn.ruanyun.backInterface.modules.business.good.mapper.GoodMapper;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import cn.ruanyun.backInterface.modules.business.goodCategory.entity.GoodCategory;
import cn.ruanyun.backInterface.modules.business.goodCategory.mapper.GoodCategoryMapper;
import cn.ruanyun.backInterface.modules.business.goodService.pojo.GoodService;
import cn.ruanyun.backInterface.modules.business.itemAttrKey.pojo.ItemAttrKey;
import cn.ruanyun.backInterface.modules.business.itemAttrKey.service.IItemAttrKeyService;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.pojo.ItemAttrVal;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.service.IItemAttrValService;
import cn.ruanyun.backInterface.modules.business.shoppingCart.VO.ShoppingCartVO;
import cn.ruanyun.backInterface.modules.business.shoppingCart.entity.ShoppingCart;
import cn.ruanyun.backInterface.modules.business.shoppingCart.mapper.ShoppingCartMapper;
import cn.ruanyun.backInterface.modules.business.shoppingCart.service.IShoppingCartService;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.mapper.SizeAndRolorMapper;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.pojo.SizeAndRolor;
import cn.ruanyun.backInterface.modules.business.sizeAndRolor.service.ISizeAndRolorService;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
    private IItemAttrValService iItemAttrValService;

    @Resource
    private GoodCategoryMapper goodCategoryMapper;

    @Resource
    private GoodMapper goodMapper;

    @Resource
    private SizeAndRolorMapper sizeAndRolorMapper;


    /**
     * 加入购物车
     *
     * @param shoppingCart shoppingCart
     */
    @Override
    public void insertShoppingCart(ShoppingCart shoppingCart) {

        ShoppingCart shoppingCartInsert = this.getOne(Wrappers.<ShoppingCart>lambdaQuery()
                .eq(ShoppingCart::getGoodId, shoppingCart.getGoodId())
                .eq(ShoppingCart::getBuyType, shoppingCart.getBuyType())
                //这里的getAttrSymbolPath 传进来的是规格属性的id
                .eq(ShoppingCart::getAttrSymbolPath,Optional.ofNullable(sizeAndRolorMapper.selectById(shoppingCart.getAttrSymbolPath())).map(SizeAndRolor::getAttrSymbolPath).orElse(null))
                .eq(ShoppingCart::getShopCartType, shoppingCart.getShopCartType())
                .eq(ShoppingCart::getCreateBy, securityUtil.getCurrUser().getId()));

        if (ToolUtil.isEmpty(shoppingCartInsert)) {

            shoppingCart.setCreateBy(securityUtil.getCurrUser().getId());
            shoppingCart.setAttrSymbolPath(Optional.ofNullable(sizeAndRolorMapper.selectById(shoppingCart.getAttrSymbolPath())).map(SizeAndRolor::getAttrSymbolPath).orElse(null));
            this.save(shoppingCart);
        }else {

            shoppingCartInsert.setBuyCount(shoppingCartInsert.getBuyCount() + shoppingCart.getBuyCount());
            this.updateById(shoppingCartInsert);
        }

    }

    /**
     * 移除购物车
     *
     * @param ids ids
     */
    @Override
    public void removeShoppingCart(String ids) {

        this.removeByIds(ToolUtil.splitterStr(ids));
    }

    /**
     * 获取我的购物车数据
     *
     * @return return
     */
    @Override
    public Result<DataVo<ShoppingCartVO>> getMyShoppingCart(PageVo pageVo){

        Page<ShoppingCart> shoppingCartPage = this.page(PageUtil.initMpPage(pageVo), Wrappers.<ShoppingCart>lambdaQuery()
        .eq(ShoppingCart::getCreateBy, securityUtil.getCurrUser().getId())
        .orderByDesc(ShoppingCart::getCreateTime));

        if (ToolUtil.isEmpty(shoppingCartPage.getRecords())) {

            return new ResultUtil<DataVo<ShoppingCartVO>>().setErrorMsg(201, "暂无数据！");
        }

        DataVo<ShoppingCartVO> result = new DataVo<>();

        result.setDataResult(shoppingCartPage.getRecords().parallelStream().flatMap(shoppingCart -> {

            ShoppingCartVO shoppingCartVO = new ShoppingCartVO();
            shoppingCartVO.setItemAttrKeys(iItemAttrValService.getItemAttrValVo(shoppingCart.getAttrSymbolPath()));

            shoppingCartVO.setAttrSymbolPathId(Optional.ofNullable(sizeAndRolorMapper.selectOne(new QueryWrapper<SizeAndRolor>().lambda()
                    .eq(SizeAndRolor::getGoodsId,shoppingCart.getGoodId())
                    .eq(SizeAndRolor::getAttrSymbolPath,shoppingCart.getAttrSymbolPath())
            )).map(SizeAndRolor::getId).orElse(null));
            //尾款方式
            shoppingCartVO.setLeaseState(
                   Optional.ofNullable(goodCategoryMapper.selectById(
                           Optional.ofNullable(goodMapper.selectById(shoppingCart.getGoodId())).map(Good::getGoodCategoryId).orElse(null)
                   )).map(GoodCategory::getLeaseState).orElse(null)

            );
            ToolUtil.copyProperties(shoppingCart, shoppingCartVO);

            return Stream.of(shoppingCartVO);
        }).collect(Collectors.toList())).setTotalSize(shoppingCartPage.getTotal())
                .setTotalPage(shoppingCartPage.getPages())
                .setCurrentPageNum(shoppingCartPage.getCurrent());

        return new ResultUtil<DataVo<ShoppingCartVO>>().setData(result, "获取我的购物车数据成功！");
    }

    /**
     * 获取购物车数量
     *
     * @return return
     */
    @Override
    public Integer getGoodsCartNum() {

        return Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<ShoppingCart>lambdaQuery()
        .eq(ShoppingCart::getCreateBy, securityUtil.getCurrUser().getId()))))
        .map(List::size)
        .orElse(0);
    }

    /**
     * 修改购物车数量
     *
     * @param id    购物车id
     * @param count 购物车商品数量
     * @return Object
     */
    @Override
    public Result<Object> changeShopCartNum(String id, Integer count) {

        return Optional.ofNullable(this.getById(id)).map(shoppingCart -> {

            shoppingCart.setBuyCount(count);
            this.updateById(shoppingCart);

            return new ResultUtil<>().setSuccessMsg("修改成功！");
        }).orElse(new ResultUtil<>().setErrorMsg(201, "当前购物车数据不存在！"));
    }


}