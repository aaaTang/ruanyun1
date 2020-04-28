package cn.ruanyun.backInterface.modules.business.searchHistory.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.searchHistory.pojo.SearchHistory;
import cn.ruanyun.backInterface.modules.business.searchHistory.service.ISearchHistoryService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author z
 * 搜索历史记录管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/searchHistory")
@Transactional
public class SearchHistoryController {

    @Autowired
    private ISearchHistoryService iSearchHistoryService;


   /**
     * 更新或者插入数据
     * @param searchHistory
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateSearchHistory")
    public Result<Object> insertOrderUpdateSearchHistory(SearchHistory searchHistory){

        try {
            iSearchHistoryService.insertOrderUpdateSearchHistory(searchHistory);
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
    @PostMapping(value = "/removeSearchHistory")
    public Result<Object> removeSearchHistory(String ids){

        try {

            iSearchHistoryService.removeSearchHistory(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 获取App热门搜索
     * @param pageVo
     * @return
     */
    @PostMapping("/getAppSearchHistory")
    public Result<Object> getAppSearchHistory(PageVo pageVo) {
        return Optional.ofNullable(iSearchHistoryService.getAppSearchHistory())
                .map(searchHistory -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",searchHistory.size());
                    result.put("data", PageUtil.listToPage(pageVo,searchHistory));
                    return new ResultUtil<>().setData(result,"获取App热门搜索数据成功！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }


}
