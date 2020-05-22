package cn.ruanyun.backInterface.modules.business.myFavorite.serviceimpl;


import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.UserService;
import cn.ruanyun.backInterface.modules.business.good.VO.AppGoodListVO;
import cn.ruanyun.backInterface.modules.business.good.mapper.GoodMapper;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import cn.ruanyun.backInterface.modules.business.goodsPackage.mapper.GoodsPackageMapper;
import cn.ruanyun.backInterface.modules.business.goodsPackage.pojo.GoodsPackage;
import cn.ruanyun.backInterface.modules.business.myFavorite.VO.GoodsFavoriteVO;
import cn.ruanyun.backInterface.modules.business.myFavorite.VO.PackageFavotiteVO;
import cn.ruanyun.backInterface.modules.business.myFavorite.entity.MyFavorite;
import cn.ruanyun.backInterface.modules.business.myFavorite.mapper.MyFavoriteMapper;
import cn.ruanyun.backInterface.modules.business.myFavorite.service.IMyFavoriteService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 我的收藏接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IMyFavoriteServiceImpl extends ServiceImpl<MyFavoriteMapper, MyFavorite> implements IMyFavoriteService {

    @Autowired
    private SecurityUtil securityUtil;

    @Resource
    private GoodMapper goodMapper;

    @Resource
    private GoodsPackageMapper goodsPackageMapper;

    @Resource
    private UserMapper userMapper;


    /**
     * 插入我的收藏
     *
     * @param myFavorite
     */
    @Override
    public void insertMyFavorite(MyFavorite myFavorite) {

        myFavorite.setCreateBy(securityUtil.getCurrUser().getId());
            MyFavorite favorite = this.getOne(new QueryWrapper<MyFavorite>().lambda().eq(MyFavorite::getGoodId,myFavorite.getGoodId())
                    .eq(MyFavorite::getGoodTypeEnum,myFavorite.getGoodTypeEnum())
            .eq(MyFavorite::getCreateBy,securityUtil.getCurrUser().getId()));

            if(ToolUtil.isEmpty(favorite)){
                this.save(myFavorite);
            }

    }

    /**
     * 移除我的收藏
     */
    @Override
    public Result<Object> deleteMyFavorite(String goodId, GoodTypeEnum goodTypeEnum) {

        MyFavorite myFavorite = this.getOne(Wrappers.<MyFavorite>lambdaQuery().eq(MyFavorite::getGoodId,goodId)
                .eq(MyFavorite::getCreateBy,securityUtil.getCurrUser().getId())
                .eq(MyFavorite::getGoodTypeEnum,goodTypeEnum));

        if(ToolUtil.isNotEmpty(myFavorite)){
            return new ResultUtil<>().setData(this.removeById(myFavorite.getId()),"取消成功！");
        }else {
            return new ResultUtil<>().setErrorMsg("取消失败！");
        }
    }

    /**
     * 获取我的收藏商品列表
     *
     * @return
     */
    @Override
    public List<GoodsFavoriteVO> getMyGoodsFavoriteList() {

        String currentUser = securityUtil.getCurrUser().getId();
        //1.基础数据
        CompletableFuture<Optional<List<MyFavorite>>> myFavoriteList = CompletableFuture.supplyAsync(() ->
                Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<MyFavorite>lambdaQuery()
                        .eq(MyFavorite::getCreateBy,currentUser)
                        .eq(MyFavorite::getGoodTypeEnum, GoodTypeEnum.GOOD)
                        .orderByDesc(MyFavorite::getCreateTime).eq(MyFavorite::getDelFlag,0)))));

        //2.封装数据
        CompletableFuture<List<GoodsFavoriteVO>> goodVOList = myFavoriteList.thenApplyAsync(myFavorites ->
                myFavorites.map(myFavorites1 -> myFavorites1.parallelStream().flatMap(myFavorite -> {
                    GoodsFavoriteVO goodListVO= new GoodsFavoriteVO();
                        //Good good = goodMapper.selectById(myFavorite.getGoodId());
                    Good good = goodMapper.selectOne(Wrappers.<Good>lambdaQuery().eq(Good::getId,myFavorite.getGoodId()).eq(Good::getDelFlag,0));
                    if(ToolUtil.isNotEmpty(good)){
                        ToolUtil.copyProperties(good,goodListVO);
                        String[] split = good.getGoodPics().split(",");
                        if(ToolUtil.isNotEmpty(split)){
                            goodListVO.setGoodPics(split[0]);
                        }
                        goodListVO.setShopName(Optional.ofNullable(userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getId,good.getCreateBy()))).map(User::getShopName)
                                .orElse(null));
                        return Stream.of(goodListVO);
                    }else {
                        return null;
                    }
                })
                .collect(Collectors.toList())).orElse(null));

        return goodVOList.join();
    }

    /**
     * 获取我的收藏套餐
     * @return
     */
    @Override
    public List<PackageFavotiteVO> getMyGoodsPackageFavoriteList() {
        String currentUser = securityUtil.getCurrUser().getId();
        //1.基础数据
        CompletableFuture<Optional<List<MyFavorite>>> myFavoriteList = CompletableFuture.supplyAsync(() ->
                Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<MyFavorite>lambdaQuery()
                        .eq(MyFavorite::getCreateBy,currentUser)
                        .eq(MyFavorite::getGoodTypeEnum, GoodTypeEnum.GOODSPACKAGE)
                        .orderByDesc(MyFavorite::getCreateTime).eq(MyFavorite::getDelFlag,0)))));

        //2.封装数据
        CompletableFuture<List<PackageFavotiteVO>> goodVOList = myFavoriteList.thenApplyAsync(myFavorites ->
                myFavorites.map(myFavorites1 -> myFavorites1.parallelStream().flatMap(myFavorite -> {
                    PackageFavotiteVO goodListVO= new PackageFavotiteVO();
                    Good goodsPackage = goodMapper.selectById(myFavorite.getGoodId());
                    goodListVO.setId(goodsPackage.getId())
                    .setGoodsName(goodsPackage.getGoodName())//套餐名称
                    .setGoodIds(goodsPackage.getId())//商品的ids
                    .setNewPrice(goodsPackage.getGoodNewPrice())//新价格
                    .setShopName(Optional.ofNullable(userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getId,goodsPackage.getCreateBy()))).map(User::getShopName)
                            .orElse(null))//店铺名称
                    ;
                    String[] split = goodsPackage.getGoodPics().split(",");
                    if(ToolUtil.isNotEmpty(split)){
                        goodListVO.setPics(split[0]);//套餐图片
                    }
                    return Stream.of(goodListVO);
                })
                        .collect(Collectors.toList())).orElse(null));

        return goodVOList.join();
    }

    @Override
    public Integer getMyFavorite(String id,GoodTypeEnum goodTypeEnum) {

        MyFavorite  myFavorite = new MyFavorite();
        if(ToolUtil.isNotEmpty(securityUtil.getCurrUser().getId())){
            myFavorite = this.getOne(new QueryWrapper<MyFavorite>().lambda().eq(MyFavorite::getGoodId,id)
                    .eq(MyFavorite::getGoodTypeEnum,goodTypeEnum)
                    .eq(MyFavorite::getCreateBy,securityUtil.getCurrUser().getId())
            );
        }
        return (myFavorite != null ? 1 : 0);
    }


    /**
     * 获取我的收藏数量
     * @return
     */
    @Override
    public Long getMyFavoriteNum() {
        List<MyFavorite> list = this.list(new QueryWrapper<MyFavorite>().lambda()
//                .eq(MyFavorite::getGoodTypeEnum, GoodTypeEnum.GOOD)
                .eq(MyFavorite::getCreateBy,securityUtil.getCurrUser().getId()));
            Long num = Long.valueOf(list.size());
        return (num != null ? num : 0);
    }




}