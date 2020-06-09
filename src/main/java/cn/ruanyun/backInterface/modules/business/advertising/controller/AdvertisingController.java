package cn.ruanyun.backInterface.modules.business.advertising.controller;

import cn.ruanyun.backInterface.common.enums.AdvertisingTypeEnum;
import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.business.advertising.pojo.Advertising;
import cn.ruanyun.backInterface.modules.business.advertising.service.IAdvertisingService;
import cn.ruanyun.backInterface.modules.business.advertising.vo.AppAdvertisingListVo;
import cn.ruanyun.backInterface.modules.business.advertising.vo.BackAdvertisingListVo;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
 * 广告管理管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/advertising")
@Transactional
public class AdvertisingController {

    @Autowired
    private IAdvertisingService iAdvertisingService;


    @PostMapping("/insertOrderUpdateAdvertising")
    @ApiOperation("添加轮播图广告数据")
    Result<Object> insertOrderUpdateAdvertising(Advertising advertising) {

        return iAdvertisingService.insertOrderUpdateAdvertising(advertising);
    }

    @PostMapping("/removeAdvertising")
    @ApiOperation("移除广告数据")
    @ApiImplicitParams(@ApiImplicitParam(name = "ids", value = "广告id字符串", dataType = "string", paramType = "query"))
    public Result<Object> removeAdvertising(String ids) {

        iAdvertisingService.removeAdvertising(ids);
        return new ResultUtil<>().setSuccessMsg("移除成功！");
    }

    @PostMapping("/getAppAdvertisingList")
    @ApiOperation("获取app广告数据")
    public Result<List<AppAdvertisingListVo>> getAppAdvertisingList(Advertising advertising) {

        return new ResultUtil<List<AppAdvertisingListVo>>().setData(iAdvertisingService.getAppAdvertisingList(advertising)
        , "获取app广告数据成功！");
    }


    @PostMapping("/getBackAdvertisingList")
    @ApiOperation("获取后台管理系统广告数据")
    public Result<DataVo<BackAdvertisingListVo>> getBackAdvertisingList(PageVo pageVo, Advertising advertising) {

        return iAdvertisingService.getBackAdvertisingList(pageVo, advertising);
    }
}
