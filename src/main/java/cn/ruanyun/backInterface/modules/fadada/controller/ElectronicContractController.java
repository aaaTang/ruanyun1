package cn.ruanyun.backInterface.modules.fadada.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.fadada.dto.UploaddocsDto;
import cn.ruanyun.backInterface.modules.fadada.pojo.ElectronicContract;
import cn.ruanyun.backInterface.modules.fadada.service.IElectronicContractService;
import cn.ruanyun.backInterface.modules.fadada.vo.ElectronicContractVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author z
 * 电子合同管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/electronicContract")
@Transactional
@Api(tags = "合同")
public class ElectronicContractController {

    @Autowired
    private IElectronicContractService iElectronicContractService;


    @PostMapping("/insertElectronicContract")
    @ApiOperation("添加电子合同(PC)")
    public Result<Object> insertElectronicContract(UploaddocsDto uploaddocsDto) {

        return iElectronicContractService.insertElectronicContract(uploaddocsDto);
    }


    @PostMapping("/removeElectronicContract")
    @ApiOperation("移除电子合同(PC)")
    public Result<Object> removeElectronicContract(String ids) {

        return iElectronicContractService.removeElectronicContract(ids);
    }


    @PostMapping("/getElectronicContractList")
    @ApiOperation("获取电子合同列表(PC)")
    public Result<DataVo<ElectronicContractVo>> getElectronicContractList(PageVo pageVo) {

        return iElectronicContractService.getElectronicContractList(pageVo);
    }


    @PostMapping("/viewContract")
    @ApiOperation("查看合同")
    public Result<Object> viewContract(String id) {

        return iElectronicContractService.viewContract(id);
    }

}
