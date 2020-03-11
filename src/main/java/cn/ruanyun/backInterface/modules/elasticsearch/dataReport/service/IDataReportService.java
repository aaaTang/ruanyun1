package cn.ruanyun.backInterface.modules.elasticsearch.dataReport.service;

import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.modules.elasticsearch.dataReport.pojo.DataReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 数据上报接口
 * @author fei
 */
public interface IDataReportService {


    /**
     * 插入数据上报
     * @param dataReport
     */
    void insertOrUpdateDataReport(DataReport dataReport);


    /**
     * 获取全部数据
     * @param dataReport
     * @return
     */
    Page<DataReport> getDataReportList(DataReport dataReport, PageVo pageVo);





}