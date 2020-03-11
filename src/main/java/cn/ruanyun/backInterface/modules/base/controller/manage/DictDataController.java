package cn.ruanyun.backInterface.modules.base.controller.manage;


import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.Dict;
import cn.ruanyun.backInterface.modules.base.pojo.DictData;
import cn.ruanyun.backInterface.modules.base.service.DictDataService;
import cn.ruanyun.backInterface.modules.base.service.DictService;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author fei
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/dictData")
@CacheConfig(cacheNames = "dictData")
@Transactional
public class DictDataController{

    @Autowired
    private DictService dictService;

    @Autowired
    private DictDataService dictDataService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping(value = "/getByCondition", method = RequestMethod.GET)
    public Result<Page<DictData>> getByCondition(@ModelAttribute DictData dictData,
                                                                                 @ModelAttribute PageVo pageVo){

        Page<DictData> page = dictDataService.findByCondition(dictData, PageUtil.initPage(pageVo));
        return new ResultUtil<Page<DictData>>().setData(page);
    }

    @RequestMapping(value = "/getByType/{type}", method = RequestMethod.GET)
    @Cacheable(key = "#type")
    public Result<Object> getByType(@PathVariable String type){

        Dict dict = dictService.findByType(type);
        if (dict == null) {
            return new ResultUtil<>().setErrorMsg("字典类型Type不存在");
        }
        List<DictData> list = dictDataService.findByDictId(dict.getId());
        return new ResultUtil<>().setData(list);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<Object> add(@ModelAttribute DictData dictData){

        Dict dict = dictService.get(dictData.getDictId());
        if (dict == null) {
            return new ResultUtil<>().setErrorMsg("字典类型id不存在");
        }
        dictDataService.save(dictData);
        // 删除缓存
        redisTemplate.delete("dictData::"+dict.getType());
        return new ResultUtil<>().setSuccessMsg("添加成功");
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Result<Object> edit(@ModelAttribute DictData dictData){

        dictDataService.update(dictData);
        // 删除缓存
        Dict dict = dictService.get(dictData.getDictId());
        redisTemplate.delete("dictData::"+dict.getType());
        return new ResultUtil<>().setSuccessMsg("编辑成功");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    public Result<Object> delByIds(@PathVariable String[] ids){

        for(String id : ids){
            DictData dictData = dictDataService.get(id);
            Dict dict = dictService.get(dictData.getDictId());
            dictDataService.delete(id);
            // 删除缓存
            redisTemplate.delete("dictData::"+dict.getType());
        }
        return new ResultUtil<>().setSuccessMsg("批量通过id删除数据成功");
    }
}
