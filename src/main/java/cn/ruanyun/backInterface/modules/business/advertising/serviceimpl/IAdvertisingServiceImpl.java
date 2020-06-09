package cn.ruanyun.backInterface.modules.business.advertising.serviceimpl;

import cn.hutool.core.util.ObjectUtil;
import cn.ruanyun.backInterface.common.enums.AdvertisingJumpTypeEnum;
import cn.ruanyun.backInterface.common.enums.AdvertisingTypeEnum;
import cn.ruanyun.backInterface.common.exception.RuanyunException;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.vo.BackSiteListVo;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.business.advertising.vo.AppAdvertisingListVo;
import cn.ruanyun.backInterface.modules.business.advertising.mapper.AdvertisingMapper;
import cn.ruanyun.backInterface.modules.business.advertising.pojo.Advertising;
import cn.ruanyun.backInterface.modules.business.advertising.service.IAdvertisingService;
import cn.ruanyun.backInterface.modules.business.advertising.vo.BackAdvertisingListVo;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import cn.ruanyun.backInterface.modules.business.goodCategory.service.IGoodCategoryService;
import cn.ruanyun.backInterface.modules.business.goodsPackage.pojo.GoodsPackage;
import cn.ruanyun.backInterface.modules.business.goodsPackage.service.IGoodsPackageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.swing.text.html.Option;


/**
 * 广告管理接口实现
 *
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IAdvertisingServiceImpl extends ServiceImpl<AdvertisingMapper, Advertising> implements IAdvertisingService {


    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private IGoodCategoryService goodCategoryService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IGoodService goodService;

    @Autowired
    private IGoodsPackageService goodsPackageService;


    @Override
    public Result<Object> insertOrderUpdateAdvertising(Advertising advertising) {

        //添加操作
        if (ToolUtil.isEmpty(advertising.getId())) {

            advertising.setCreateBy(securityUtil.getCurrUser().getId());
        }else {

            advertising.setUpdateBy(securityUtil.getCurrUser().getId());
        }

        this.saveOrUpdate(advertising);

        return new ResultUtil<>().setSuccessMsg("添加成功！");

    }

    @Override
    public void removeAdvertising(String ids) {

        CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
    }

    /**
     * App查询广告数据列表
     *
     * @param advertisingTypeEnum 广告类型
     * @return AppAdvertisingListVo
     */
    @Override
    public List<AppAdvertisingListVo> getAppAdvertisingList(AdvertisingTypeEnum advertisingTypeEnum) {

        return Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<Advertising>lambdaQuery()
        .eq(Advertising::getAdvertisingType, advertisingTypeEnum)
        .orderByDesc(Advertising::getCreateTime))))
        .map(advertisings -> advertisings.parallelStream().flatMap(advertising -> {

            AppAdvertisingListVo appAdvertisingListVo = new AppAdvertisingListVo();

            //计算商家模板
            if (ObjectUtil.equal(advertising.getAdvertisingJumpType(), AdvertisingJumpTypeEnum.STORE_HOME)) {

                appAdvertisingListVo.setStoreType(goodCategoryService.judgeStoreType(userService.getById(advertising.getUrl())));
            }

            appAdvertisingListVo.setAdvertisingJumpType(advertising.getAdvertisingJumpType().getCode())
                    .setAdvertisingType(advertising.getAdvertisingType().getCode());

            ToolUtil.copyProperties(advertising, appAdvertisingListVo);

            return Stream.of(appAdvertisingListVo);
        }).collect(Collectors.toList()))
        .orElse(null);
    }

    /**
     * 后端查询广告数据列表
     *
     * @param pageVo      分页
     * @param advertising 广告
     * @return BackAdvertisingListVo
     */
    @Override
    public Result<DataVo<BackAdvertisingListVo>> getBackAdvertisingList(PageVo pageVo, Advertising advertising) {

        Page<Advertising> advertisingPage = this.page(PageUtil.initMpPage(pageVo), Wrappers.<Advertising>lambdaQuery()

         //跳转类型
        .eq(ToolUtil.isNotEmpty(advertising.getAdvertisingJumpType()), Advertising::getAdvertisingJumpType, advertising.getAdvertisingJumpType())

         //广告类型
        .eq(ToolUtil.isNotEmpty(advertising.getAdvertisingType()), Advertising::getAdvertisingType, advertising.getAdvertisingType())

        //时间排序
        .orderByDesc(Advertising::getCreateTime));


        if (ToolUtil.isEmpty(advertisingPage.getRecords())) {

            return new ResultUtil<DataVo<BackAdvertisingListVo>>().setErrorMsg(201, "暂无数据！");
        }

        DataVo<BackAdvertisingListVo> result = new DataVo<>();
        result.setTotalPage(advertisingPage.getTotal())
                .setCurrentPageNum(advertisingPage.getCurrent())
                .setTotalSize(advertisingPage.getSize())
                .setDataResult(advertisingPage.getRecords().parallelStream().flatMap(advertisingReturn -> {

                    BackAdvertisingListVo backAdvertisingListVo = new BackAdvertisingListVo();
                    ToolUtil.copyProperties(advertisingReturn, backAdvertisingListVo);

                    //设置描述
                    if (ObjectUtil.equal(advertisingReturn.getAdvertisingJumpType(), AdvertisingJumpTypeEnum.STORE_HOME)) {

                        backAdvertisingListVo.setJumpDesc(Optional.ofNullable(userService.getById(advertisingReturn.getUrl()))
                        .map(User::getNickName).orElse("-"));
                    }else if (ObjectUtil.equal(advertisingReturn.getAdvertisingJumpType(), AdvertisingJumpTypeEnum.GOOD_PACKAGE_PAGE)) {

                        backAdvertisingListVo.setJumpDesc(Optional.ofNullable(goodsPackageService.getById(advertisingReturn.getUrl()))
                        .map(GoodsPackage::getGoodsName).orElse("-"));
                    }else if (ObjectUtil.equal(advertisingReturn.getAdvertisingJumpType(), AdvertisingJumpTypeEnum.GOOD_PAGE)) {

                        backAdvertisingListVo.setJumpDesc(Optional.ofNullable(goodService.getById(advertisingReturn.getUrl()))
                        .map(Good::getGoodName).orElse("-"));
                    }else {

                        backAdvertisingListVo.setJumpDesc("h5链接");
                    }

                    return Stream.of(backAdvertisingListVo);
                }).collect(Collectors.toList()));

        return new ResultUtil<DataVo<BackAdvertisingListVo>>().setData(result, "后台获取广告列表成功！");
    }


}