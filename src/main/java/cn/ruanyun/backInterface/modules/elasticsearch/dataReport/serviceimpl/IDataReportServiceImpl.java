package cn.ruanyun.backInterface.modules.elasticsearch.dataReport.serviceimpl;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.modules.elasticsearch.dataReport.mapper.DataReportMapper;
import cn.ruanyun.backInterface.modules.elasticsearch.dataReport.pojo.DataReport;
import cn.ruanyun.backInterface.modules.elasticsearch.dataReport.service.IDataReportService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;


/**
 * 数据上报接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IDataReportServiceImpl implements IDataReportService {


    @Autowired
    private DataReportMapper dataReportMapper;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    @Override
    public void insertOrUpdateDataReport(DataReport dataReport) {

        elasticsearchTemplate.createIndex(dataReport.getClass());
        //异步插入或者更新数据
        CompletableFuture.runAsync(() -> dataReportMapper.save(dataReport));
    }


    @Override
    public Page<DataReport> getDataReportList(DataReport dataReport, PageVo pageVo) {


        //直接构造查询条件 条件
        QueryBuilder qb0 = QueryBuilders.termQuery("identificationCode",dataReport.getIdentificationCode());

        return dataReportMapper.search(QueryBuilders.boolQuery().must(qb0), PageUtil.initPage(pageVo));
    }
}