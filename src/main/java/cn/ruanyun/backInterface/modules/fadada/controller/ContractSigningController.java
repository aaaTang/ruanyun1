package cn.ruanyun.backInterface.modules.fadada.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.fadada.dto.ExtSignDto;
import cn.ruanyun.backInterface.modules.fadada.pojo.ContractSigning;
import cn.ruanyun.backInterface.modules.fadada.service.IContractSigningService;
import cn.ruanyun.backInterface.modules.fadada.vo.ContractSigningVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author z
 * 合同签署管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/contractSigning")
@Transactional
@Api(tags = "合同签署")
public class ContractSigningController {


    @Autowired
    private  IContractSigningService contractSigningService;


    @PostMapping("/getCustomerSigning")
    @ApiOperation("获取客户的审核数据(APP)")
    public Result<ContractSigningVo> getCustomerSigning() {

        return contractSigningService.getCustomerSigning();
    }

    @PostMapping("/extSign")
    @ApiOperation("手动签署合同(APP)")
    public Result<Object> extSign(ExtSignDto extsignDto) {

        return contractSigningService.extSign(extsignDto);
    }

    @PostMapping("/updateExtSign")
    @ApiOperation("修改签署合同(APP)")
    public Result<Object> updateExtSign(ExtSignDto extsignDto) {


        return contractSigningService.updateExtSign(extsignDto);
    }

    @PostMapping("/getContractSigningList")
    @ApiModelProperty("获取审核列表(PC)")
    public Result<DataVo<ContractSigningVo>> getContractSigningList(PageVo pageVo) {

        return contractSigningService.getContractSigningList(pageVo);
    }


    @PostMapping("checkContractSigning")
    @ApiOperation("后台审核手动签署合同(PC)")
    public Result<Object> checkContractSigning(ContractSigning contractSigning) {

        return contractSigningService.checkContractSigning(contractSigning);
    }

    @PostMapping("/contractFiling")
    @ApiOperation(value = "合同归档(PC)")
    public Result<Object> contractFiling(String id) {

        return contractSigningService.contractFiling(id);
    }

    @PostMapping("/viewContract")
    @ApiOperation("查看合同(pc)")
    public Result<Object> viewContract(String id) {

        return contractSigningService.viewContract(id);
    }

    @PostMapping("/downLoadContract")
    @ApiOperation("下载合同(PC)")
    public Result<Object> downLoadContract(String id) {

        return contractSigningService.downLoadContract(id);
    }

}
