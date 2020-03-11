package cn.ruanyun.backInterface.modules.quartz.jobs;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


/**
 * 示例带参定时任务
 * @author fei
 */
@Slf4j
public class SampleJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        log.info(String.format("软云测试定时 时间:"+ DateUtil.now()));
    }
}
