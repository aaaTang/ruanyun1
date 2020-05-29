package cn.ruanyun.backInterface.modules.auctionCalendar.site.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.pojo.Site;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.service.ISiteService;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.vo.SiteDetailVo;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.vo.SiteListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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


}
