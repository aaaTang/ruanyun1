package cn.ruanyun.backInterface.modules.business.searchHistory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.searchHistory.pojo.SearchHistory;

import java.util.List;

/**
 * 搜索历史记录接口
 * @author z
 */
public interface ISearchHistoryService extends IService<SearchHistory> {


      /**
        * 插入或者更新searchHistory
        * @param searchHistory
       */
     void insertOrderUpdateSearchHistory(SearchHistory searchHistory);



      /**
       * 移除searchHistory
       * @param ids
       */
     void removeSearchHistory(String ids);


    /**
     * 获取App热门搜索
     * @return
     */
     List getAppSearchHistory();
}