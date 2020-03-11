package cn.ruanyun.backInterface.modules.elasticsearch.dataReport.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.elasticsearch.dataReport.pojo.DataReport;
import cn.ruanyun.backInterface.modules.elasticsearch.dataReport.service.IDataReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author fei
 * 数据上报管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/dataReport")
@Transactional
public class DataReportController {

    @Autowired
    private IDataReportService iDataReportService;


    @PostMapping("/insertOrUpdateDataReport")
    public Result<Object> insertOrUpdateDataReport(DataReport dataReport) {

        iDataReportService.insertOrUpdateDataReport(dataReport);
        return new ResultUtil<>().setSuccessMsg("成功！");
    }
}
