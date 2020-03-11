package cn.ruanyun.backInterface.modules.quartz.service;



import cn.ruanyun.backInterface.base.RuanyunBaseService;
import cn.ruanyun.backInterface.modules.quartz.entity.QuartzJob;

import java.util.List;

/**
 * 定时任务接口
 * @author fei
 */
public interface QuartzJobService extends RuanyunBaseService<QuartzJob,String> {

    /**
     * 通过类名获取
     * @param jobClassName
     * @return
     */
    List<QuartzJob> findByJobClassName(String jobClassName);


    /**
     * 添加定时任务
     * @param jobClassName
     * @param cronExpression
     * @param parameter
     */
    void add(String jobClassName, String cronExpression, String parameter);

}