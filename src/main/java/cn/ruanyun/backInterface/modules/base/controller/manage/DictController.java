package cn.ruanyun.backInterface.modules.base.controller.manage;


import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.entity.Dict;
import cn.ruanyun.backInterface.modules.base.service.DictDataService;
import cn.ruanyun.backInterface.modules.base.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author fei
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/dict")
@Transactional
public class DictController{

    @Autowired
    private DictService dictService;

    @Autowired
    private DictDataService dictDataService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public Result<List<Dict>> getAll(){

        List<Dict> list = dictService.findAllOrderBySortOrder();
        return new ResultUtil<List<Dict>>().setData(list);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<Object> add(@ModelAttribute Dict dict){

        if(dictService.findByType(dict.getType())!=null){
            return new ResultUtil<>().setErrorMsg("字典类型Type已存在");
        }
        dictService.save(dict);
        return new ResultUtil<>().setSuccessMsg("添加成功");
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Result<Object> edit(@ModelAttribute Dict dict){

        Dict old = dictService.get(dict.getId());
        // 若type修改判断唯一
        if(!old.getType().equals(dict.getType())&&dictService.findByType(dict.getType())!=null){
            return new ResultUtil<>().setErrorMsg("字典类型Type已存在");
        }
        dictService.update(dict);
        return new ResultUtil<>().setSuccessMsg("编辑成功");
    }

    @RequestMapping(value = "/delByIds/{id}", method = RequestMethod.DELETE)
    public Result<Object> delAllByIds(@PathVariable String id){


        Dict dict = dictService.get(id);
        dictService.delete(id);
        dictDataService.deleteByDictId(id);
        // 删除缓存
        redisTemplate.delete("dictData::"+dict.getType());
        return new ResultUtil<>().setSuccessMsg("删除成功");
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Result<List<Dict>> searchPermissionList(@RequestParam String key){

        List<Dict> list = dictService.findByTitleOrTypeLike(key);
        return new ResultUtil<List<Dict>>().setData(list);
    }
}
