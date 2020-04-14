package cn.ruanyun.backInterface.modules.business.myFavorite.serviceimpl;


import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.modules.business.good.VO.AppGoodListVO;
import cn.ruanyun.backInterface.modules.business.good.mapper.GoodMapper;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import cn.ruanyun.backInterface.modules.business.myFavorite.VO.GoodsFavoriteVO;
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

    @Autowired
    private IGoodService goodService;

    @Resource
    private MyFavoriteMapper myFavoriteMapper;

    @Resource
    private GoodMapper goodMapper;

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
    public void deleteMyFavorite(String goodsId) {

            String userid =securityUtil.getCurrUser().getId();

         myFavoriteMapper.deleteMyFavorite(goodsId,userid);
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
                        Good good = goodMapper.selectById(myFavorite.getGoodId());
                    ToolUtil.copyProperties(good,goodListVO);
                    String[] split = good.getGoodPics().split(",");
                    if(ToolUtil.isNotEmpty(split)){
                        goodListVO.setGoodPics(split[0]);
                    }
                    return Stream.of(goodListVO);
                })
                .collect(Collectors.toList())).orElse(null));

        return goodVOList.join();
    }


    /**
     * 获取我的收藏数量
     * @return
     */
    @Override
    public Long getMyFavoriteNum() {
        List<MyFavorite> list = this.list(new QueryWrapper<MyFavorite>().lambda()
                .eq(MyFavorite::getGoodTypeEnum, GoodTypeEnum.GOOD)
                .eq(MyFavorite::getCreateBy,securityUtil.getCurrUser().getId()));
            Long num = Long.valueOf(list.size());
        return (num != null ? num : 0);
    }


    /**
     * 查詢我是否关注这个商品
     * @param id
     * @return
     */
    @Override
    public Integer getMyFavoriteGood(String id) {
        MyFavorite myFavorite = this.getOne(Wrappers.<MyFavorite>lambdaQuery()
                .eq(MyFavorite::getGoodId,id).eq(MyFavorite::getCreateBy,securityUtil.getCurrUser().getId())
                .eq(MyFavorite::getGoodTypeEnum, GoodTypeEnum.GOOD)
        );
        return  (myFavorite != null ? 1 : 0);
    }

    /**
     * 查詢我是否关注这个店铺
     * @param id
     * @return
     */
    @Override
    public Integer getMyFavoriteShop(String id) {
        MyFavorite myFavorite = this.getOne(Wrappers.<MyFavorite>lambdaQuery()
                .eq(MyFavorite::getGoodId,id).eq(MyFavorite::getCreateBy,securityUtil.getCurrUser().getId())
                .eq(MyFavorite::getGoodTypeEnum, GoodTypeEnum.SHOP)
        );
        return  (myFavorite != null ? 1 : 0);
    }


}