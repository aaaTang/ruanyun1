package cn.ruanyun.backInterface.modules.auctionCalendar.site.service;

import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.vo.BackSiteListVo;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.vo.SiteDetailTimeVO;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.vo.SiteDetailVo;
import cn.ruanyun.backInterface.modules.auctionCalendar.site.vo.SiteListVo;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
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

    /**
     * 后台查询场所列表
     * @param pageVo 分页
     * @return BackSiteListVo
     */
    Result<DataVo<BackSiteListVo>> getBackSiteListVo(PageVo pageVo);

    /**
     * App按分类获取场地列表
     * @param categoryId 分类id
     * @return
     */
    Result<List<SiteListVo>> AppGetCategorySiteList(String categoryId);


    /**
     * 查询场地某个时间段是否有档期时间
     * @param siteId  场地id
     * @param scheduleTime  时间
     * @return
     */
    List<SiteDetailTimeVO> getSiteDetailTime(String siteId, String scheduleTime);
}