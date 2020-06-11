package cn.ruanyun.backInterface.modules.fadada.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.fadada.dto.CompanyVerifyDto;
import cn.ruanyun.backInterface.modules.fadada.pojo.EnterpriseCertification;
import cn.ruanyun.backInterface.modules.fadada.service.IEnterpriseCertificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author z
 * 企业认证管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/enterpriseCertification")
@Transactional
@Api(tags = "企业实名认证")
public class EnterpriseCertificationController {

    @Autowired
    private IEnterpriseCertificationService iEnterpriseCertificationService;


    @PostMapping("/commitOrUpdateEnterpriseCertification")
    @ApiOperation("提交企业认证(APP)")
    public Result<Object> commitOrUpdateEnterpriseCertification(CompanyVerifyDto companyVerifyDto) {

        return iEnterpriseCertificationService.commitOrUpdateEnterpriseCertification(companyVerifyDto);
    }
}
