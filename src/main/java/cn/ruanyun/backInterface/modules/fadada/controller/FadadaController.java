package cn.ruanyun.backInterface.modules.fadada.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.fadada.dto.*;
import cn.ruanyun.backInterface.modules.fadada.pojo.Fadada;
import cn.ruanyun.backInterface.modules.fadada.service.IfadadaService;
import cn.ruanyun.backInterface.modules.fadada.vo.FadadaVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author z
 * 法大大管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/fadada")
@Api(tags = "发大大管理接口")
@Transactional
public class FadadaController {


    @Autowired
    private IfadadaService fadadaService;


    @PostMapping("/getCompanyVerifyUrl")
    @ApiOperation("获取企业实名认证地址")
    public Result<Object> getCompanyVerifyUrl(CompanyVerifyDto companyVerifyDto) {

        return fadadaService.getCompanyVerifyUrl(companyVerifyDto);
    }

    @PostMapping("/getPersonVerifyUrl")
    @ApiOperation("获取个人实名认证地址")
    public Result<Object> getPersonVerifyUrl(PersonVerifyDto personVerifyDto) {

        return fadadaService.getPersonVerifyUrl(personVerifyDto);
    }

    @PostMapping("/applyCert")
    @ApiOperation("实名证书申请")
    public Result<Object> applyCert() {

        return fadadaService.applyCert();
    }

    @PostMapping("/addSignature")
    @ApiOperation("印章上传")
    public Result<Object> addSignature(SignatureDto signatureDto) throws Exception {

        return fadadaService.addSignature(signatureDto);
    }


    @PostMapping("/uploadDocs")
    @ApiOperation("合同上传")
    public Result<Object> uploadDocs(UploaddocsDto uploaddocsDto) throws Exception {

        return fadadaService.uploadDocs(uploaddocsDto);
    }

    @PostMapping("/extSign")
    @ApiOperation("手动签署")
    public Result<Object> extSign(ExtSignDto extsignDto) {

        return fadadaService.extSign(extsignDto);
    }


    @PostMapping("/viewContract")
    @ApiOperation("查看合同")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "法大大id", dataType = "string", paramType = "query"))
    public Result<Object> viewContract(String id) {

        return fadadaService.viewContract(id);
    }

    @PostMapping("/downLoadContract")
    @ApiOperation("下载合同")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "法大大id", dataType = "string", paramType = "query"))
    public Result<Object> downLoadContract(String id) {

        return fadadaService.downLoadContract(id);
    }

    @PostMapping("/contractFiling")
    @ApiOperation("合同归档")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "法大大id", dataType = "string", paramType = "query"))
    public Result<Object> contractFiling(String id) {

        return fadadaService.contractFiling(id);
    }

    @PostMapping("/getFadadaList")
    @ApiOperation("获取合同列表")
    public Result<DataVo<FadadaVo>> getFadadaList(PageVo pageVo) {

        return fadadaService.getFadadaList(pageVo);
    }

}
