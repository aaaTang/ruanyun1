package cn.ruanyun.backInterface.modules.business.myFavorite.controller;

import cn.ruanyun.backInterface.common.enums.GoodTypeEnum;
import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.myFavorite.entity.MyFavorite;
import cn.ruanyun.backInterface.modules.business.myFavorite.service.IMyFavoriteService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

/**
 * @author fei
 */
@Slf4j
@RestController
@Api(description = "我的收藏管理接口")
@RequestMapping("/ruanyun/myFavorite")
@Transactional
public class MyFavoriteController {

    @Autowired
    private IMyFavoriteService iMyFavoriteService;

    /**
     * 添加我的收藏
     * @param myFavorite
     * @return
     */
    @PostMapping("/insertMyFavorite")
    public Result<Object> insertMyFavorite(MyFavorite myFavorite) {

        iMyFavoriteService.insertMyFavorite(myFavorite);
        return new ResultUtil<>().setSuccessMsg("添加我的收藏成功！");
    }


    /**
     * 删除我的收藏
     * @return
     */
    @PostMapping("/deleteMyFavorite")
    public Result<Object> deleteMyFavorite(String goodId, GoodTypeEnum goodTypeEnum) {

        iMyFavoriteService.deleteMyFavorite(goodId,goodTypeEnum);
        return new ResultUtil<>().setSuccessMsg("删除成功！");
    }


    /**
     * 获取我的收藏商品
     * @param pageVo
     * @return
     */
    @PostMapping("/getMyGoodsFavoriteList")
    public Result<Object> getMyGoodsFavoriteList(PageVo pageVo) {

        return Optional.ofNullable(iMyFavoriteService.getMyGoodsFavoriteList())
                .map(goodListVOS -> {

                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",goodListVOS.size());
                    result.put("data", PageUtil.listToPage(pageVo,goodListVOS));

                    return new ResultUtil<>().setData(result,"获取我的收藏列表成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }



    /**
     * 获取我的收藏套餐
     * @param pageVo
     * @return
     */
    @PostMapping("/getMyGoodsPackageFavoriteList")
    public Result<Object> getMyGoodsPackageFavoriteList(PageVo pageVo) {

        return Optional.ofNullable(iMyFavoriteService.getMyGoodsPackageFavoriteList())
                .map(goodsPackageVOS -> {

                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",goodsPackageVOS.size());
                    result.put("data", PageUtil.listToPage(pageVo,goodsPackageVOS));

                    return new ResultUtil<>().setData(result,"获取我的收藏套餐列表成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }

}
