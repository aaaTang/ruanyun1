package cn.ruanyun.backInterface.modules.auctionCalendar.site.service;

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
     * @param site
     */
    void insertOrderUpdateSite(Site site);



    /**
     * 移除site
     * @param ids
     */
    void removeSite(String ids);

}