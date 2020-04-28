package cn.ruanyun.backInterface.modules.business.searchHistory.serviceimpl;

import cn.ruanyun.backInterface.modules.business.searchHistory.VO.AppSearchHistorVO;
import cn.ruanyun.backInterface.modules.business.searchHistory.mapper.SearchHistoryMapper;
import cn.ruanyun.backInterface.modules.business.searchHistory.pojo.SearchHistory;
import cn.ruanyun.backInterface.modules.business.searchHistory.service.ISearchHistoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;


/**
 * 搜索历史记录接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class ISearchHistoryServiceImpl extends ServiceImpl<SearchHistoryMapper, SearchHistory> implements ISearchHistoryService {

       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateSearchHistory(SearchHistory searchHistory) {

           SearchHistory history = this.getOne(Wrappers.<SearchHistory>lambdaQuery().eq(SearchHistory::getTitle,searchHistory.getTitle()));
           if(ToolUtil.isNotEmpty(history)){
               history.setCount(history.getCount()+1);
               this.updateById(history);
           }else {
               this.save(searchHistory);
           }



       }

      @Override
      public void removeSearchHistory(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }


    /**
     * 获取App热门搜索
     * @return
     */
    @Override
    public List getAppSearchHistory() {
        List<SearchHistory> searchHistoryList = this.list(new QueryWrapper<SearchHistory>().lambda()
             .orderByDesc(SearchHistory::getCount));
        List<AppSearchHistorVO> searchHistorVOList = new ArrayList<>();
        for (SearchHistory searchHistory : searchHistoryList) {
            AppSearchHistorVO appVO= new AppSearchHistorVO();
            ToolUtil.copyProperties(searchHistory,appVO);
            searchHistorVOList.add(appVO);
        }
       return searchHistorVOList;
    }


}