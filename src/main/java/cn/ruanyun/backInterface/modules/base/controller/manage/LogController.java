package cn.ruanyun.backInterface.modules.base.controller.manage;


import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.common.vo.SearchVo;
import cn.ruanyun.backInterface.modules.base.pojo.Log;
import cn.ruanyun.backInterface.modules.base.pojo.elasticsearch.EsLog;
import cn.ruanyun.backInterface.modules.base.service.LogService;
import cn.ruanyun.backInterface.modules.base.service.elasticsearch.EsLogService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


/**
 * @author fei
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/log")
@Transactional
public class LogController{

    @Value("${ruanyun.logRecord.es}")
    private Boolean esRecord;

    @Autowired
    private EsLogService esLogService;

    @Autowired
    private LogService logService;

    @RequestMapping(value = "/getAllByPage", method = RequestMethod.GET)
    public Result<Object> getAllByPage(@RequestParam(required = false) Integer type,
                                       @RequestParam String key,
                                       @ModelAttribute SearchVo searchVo,
                                       @ModelAttribute PageVo pageVo){

        if(esRecord){
            Page<EsLog> es = esLogService.findByConfition(type, key, searchVo, PageUtil.initPage(pageVo));
            return new ResultUtil<>().setData(es);
        }else{
            Page<Log> log = logService.findByConfition(type, key, searchVo, PageUtil.initPage(pageVo));
            return new ResultUtil<>().setData(log);
        }
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量删除")
    public Result<Object> delByIds(@PathVariable String[] ids){

        for(String id : ids){
            if(esRecord){
                esLogService.deleteLog(id);
            }else{
                logService.delete(id);
            }
        }
        return new ResultUtil<Object>().setSuccessMsg("删除成功");
    }

    @RequestMapping(value = "/delAll", method = RequestMethod.DELETE)
    @ApiOperation(value = "全部删除")
    public Result<Object> delAll(){

        if(esRecord){
            esLogService.deleteAll();
        }else{
            logService.deleteAll();
        }
        return new ResultUtil<Object>().setSuccessMsg("删除成功");
    }
}
