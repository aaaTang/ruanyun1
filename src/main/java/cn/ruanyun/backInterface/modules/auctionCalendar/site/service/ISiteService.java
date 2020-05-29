package cn.ruanyun.backInterface.modules.auctionCalendar.site.service;

import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.vo.SiteDetailVo;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.vo.SiteListVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.pojo.Site;

import java.util.List;

/**
 * 场地接口
 * @author fei
 */
public interface ISiteService extends IService<Site> {


    /**
     * 插入或者更新site
     * @param site site
     */
    void insertOrderUpdateSite(Site site);


    /**
     * 移除site
     * @param ids ids
     */
    void removeSite(String ids);


    /**
     * 查询该商家下面的场地列表
     * @param storeId 商家id
     * @return SiteListVo
     */
    Result<List<SiteListVo>> getSiteList(String storeId);


    /**
     * 查询场地详情
     * @param id id
     * @return SiteDetailVo
     */
    Result<SiteDetailVo> getSiteDetail(String id);

}