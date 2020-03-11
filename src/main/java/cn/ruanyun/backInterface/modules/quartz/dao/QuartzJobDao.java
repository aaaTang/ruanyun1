package cn.ruanyun.backInterface.modules.quartz.dao;


import cn.ruanyun.backInterface.base.RuanyunBaseDao;
import cn.ruanyun.backInterface.modules.quartz.entity.QuartzJob;

import java.util.List;

/**
 * 定时任务数据处理层
 * @author fei
 */
public interface QuartzJobDao extends RuanyunBaseDao<QuartzJob,String> {

    /**
     * 通过类名获取
     * @param jobClassName
     * @return
     */
    List<QuartzJob> findByJobClassName(String jobClassName);
}