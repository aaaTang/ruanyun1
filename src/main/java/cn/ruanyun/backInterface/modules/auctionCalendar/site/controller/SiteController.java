package cn.ruanyun.backInterface.modules.auctionCalendar.site.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.pojo.Site;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.service.ISiteService;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.vo.BackSiteListVo;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.vo.SiteDetailTimeVO;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.vo.SiteDetailVo;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.vo.SiteListVo;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author fei
 * 场地管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/site")
@Transactional
@Api(tags = "场地管理接口")
public class SiteController {

    @Autowired
    private ISiteService iSiteService;

    @PostMapping(value = "/insertOrderUpdateSite")
    @ApiOperation("插入或者场所")
    public Result<Object> insertOrderUpdateSite(Site site){

        try {

            iSiteService.insertOrderUpdateSite(site);
            return new ResultUtil<>().setSuccessMsg("插入或者更新成功!");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

    @PostMapping(value = "/removeSite")
    @ApiOperation("移除场所")
    public Result<Object> removeSite(String ids){

        try {

            iSiteService.removeSite(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    @PostMapping("/getSiteList")
    @ApiOperation("查询该商家下面的场地列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "storeId", value = "商家id", dataType = "string", paramType = "query"))
    public Result<List<SiteListVo>> getSiteList(String storeId) {

        return iSiteService.getSiteList(storeId);
    }


    @PostMapping("/getSiteDetail")
    @ApiOperation("查询场地详情")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "场所id", dataType = "string", paramType = "query"))
    public Result<SiteDetailVo> getSiteDetail(String id) {

        return iSiteService.getSiteDetail(id);
    }

    @PostMapping("/getSiteDetailTime")
    @ApiOperation("查询场地是否有档期时间")
    @ApiImplicitParams({@ApiImplicitParam(name = "scheduleTime", value = "场所档期时间", dataType = "string", paramType = "query")
            ,@ApiImplicitParam(name = "siteId", value = "场所id", dataType = "string", paramType = "query")})
    public Result<Object> getSiteDetailTime(String siteId,String scheduleTime) {

        return new ResultUtil<>().setData(iSiteService.getSiteDetailTime(siteId,scheduleTime),"获取数据成功！");
    }




    @PostMapping("/getBackSiteListVo")
    @ApiOperation("后台查询场所列表")
    public Result<DataVo<BackSiteListVo>> getBackSiteListVo(PageVo pageVo) {

        return iSiteService.getBackSiteListVo(pageVo);
    }


    @PostMapping("/AppGetCategorySiteList")
    @ApiOperation(value = "App按分类获取场地列表")
    public Result<Object> AppGetCategorySiteList(PageVo pageVo, String categoryId) {

        return Optional.ofNullable(iSiteService.AppGetCategorySiteList(categoryId))
                .map(categorySiteList -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",categorySiteList.getResult().size());
                    result.put("data", PageUtil.listToPage(pageVo,categorySiteList.getResult()));
                    return new ResultUtil<>().setData(result,"App按分类获取场地列表！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }




}
