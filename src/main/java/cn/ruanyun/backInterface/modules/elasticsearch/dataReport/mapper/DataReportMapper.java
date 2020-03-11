package cn.ruanyun.backInterface.modules.elasticsearch.dataReport.mapper;

import cn.ruanyun.backInterface.modules.elasticsearch.dataReport.pojo.DataReport;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 数据上报数据处理层
 * @author fei
 */
public interface DataReportMapper extends ElasticsearchRepository<DataReport, String> {

}