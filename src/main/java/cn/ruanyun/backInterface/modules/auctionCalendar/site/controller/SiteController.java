package cn.ruanyun.backInterface.modules.auctionCalendar.site.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.pojo.Site;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.service.ISiteService;
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
public class SiteController {

    @Autowired
    private ISiteService iSiteService;


   /**
     * 更新或者插入数据
     * @param site
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateSite")
    public Result<Object> insertOrderUpdateSite(Site site){

        try {

            iSiteService.insertOrderUpdateSite(site);
            return new ResultUtil<>().setSuccessMsg("插入或者更新成功!");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 移除数据
     * @param ids
     * @return
    */
    @PostMapping(value = "/removeSite")
    public Result<Object> removeSite(String ids){

        try {

            iSiteService.removeSite(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

}
