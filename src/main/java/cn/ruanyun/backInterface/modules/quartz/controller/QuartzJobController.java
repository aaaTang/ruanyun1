package cn.ruanyun.backInterface.modules.quartz.controller;



import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.exception.RuanyunException;
import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.quartz.entity.QuartzJob;
import cn.ruanyun.backInterface.modules.quartz.service.QuartzJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author fei
 */
@Slf4j
@RestController
@Api(description = "定时任务管理接口")
@RequestMapping("/ruanyun/quartzJob")
@Transactional
public class QuartzJobController {

    @Autowired
    private QuartzJobService quartzJobService;

    @Autowired
    private Scheduler scheduler;

    @RequestMapping(value = "/getAllByPage", method = RequestMethod.GET)
    @ApiOperation(value = "获取所有定时任务")
    public Result<Page<QuartzJob>> getAll(@ModelAttribute PageVo page){

        Page<QuartzJob> data = quartzJobService.findAll(PageUtil.initPage(page));
        return new ResultUtil<Page<QuartzJob>>().setData(data);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加定时任务")
    public Result<Object> addJob(@ModelAttribute QuartzJob job){

        List<QuartzJob> list = quartzJobService.findByJobClassName(job.getJobClassName());
        if(list!=null&&list.size()>0){
            return new ResultUtil<>().setErrorMsg("该定时任务类名已存在");
        }
        quartzJobService.add(job.getJobClassName(),job.getCronExpression(),job.getParameter());
        quartzJobService.save(job);
        return new ResultUtil<>().setSuccessMsg("创建定时任务成功");
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value = "更新定时任务")
    public Result<Object> editJob(@ModelAttribute QuartzJob job){

        delete(job.getJobClassName());
        quartzJobService.add(job.getJobClassName(),job.getCronExpression(),job.getParameter());
        job.setStatus(CommonConstant.STATUS_NORMAL);
        quartzJobService.update(job);
        return new ResultUtil<>().setSuccessMsg("更新定时任务成功");
    }

    @RequestMapping(value = "/pause", method = RequestMethod.POST)
    @ApiOperation(value = "暂停定时任务")
    public Result<Object> pauseJob(@ModelAttribute QuartzJob job){

        try {
            scheduler.pauseJob(JobKey.jobKey(job.getJobClassName()));
        } catch (SchedulerException e) {
            throw new RuanyunException("暂停定时任务失败");
        }
        job.setStatus(CommonConstant.STATUS_DISABLE);
        quartzJobService.update(job);
        return new ResultUtil<>().setSuccessMsg("暂停定时任务成功");
    }

    @RequestMapping(value = "/resume", method = RequestMethod.POST)
    @ApiOperation(value = "恢复定时任务")
    public Result<Object> resumeJob(@ModelAttribute QuartzJob job){

        try {
            scheduler.resumeJob(JobKey.jobKey(job.getJobClassName()));
        } catch (SchedulerException e) {
            throw new RuanyunException("恢复定时任务失败");
        }
        job.setStatus(CommonConstant.STATUS_NORMAL);
        quartzJobService.update(job);
        return new ResultUtil<>().setSuccessMsg("恢复定时任务成功");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除定时任务")
    public Result<Object> deleteJob(@PathVariable String[] ids){

        for(String id:ids){
            QuartzJob job = quartzJobService.get(id);
            delete(job.getJobClassName());
            quartzJobService.delete(job);
        }
        return new ResultUtil<>().setSuccessMsg("删除定时任务成功");
    }

    /**
     * 删除定时任务
     * @param jobClassName
     */
    public void delete(String jobClassName){

        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName));
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName));
            scheduler.deleteJob(JobKey.jobKey(jobClassName));
        } catch (Exception e) {
            throw new RuanyunException("删除定时任务失败");
        }
    }

}
