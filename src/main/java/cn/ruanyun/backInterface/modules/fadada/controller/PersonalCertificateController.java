package cn.ruanyun.backInterface.modules.fadada.controller;

import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.fadada.dto.PersonVerifyDto;
import cn.ruanyun.backInterface.modules.fadada.pojo.PersonalCertificate;
import cn.ruanyun.backInterface.modules.fadada.service.IPersonalCertificateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author z
 * 个人认证管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/personalCertificate")
@Transactional
@Api(tags = "个人实名认证")
public class PersonalCertificateController {

    @Autowired
    private IPersonalCertificateService iPersonalCertificateService;


    @PostMapping("/commitOrUpdatePersonalCertificate")
    @ApiOperation("提交或者更新个人认证申请(APP)")
    public Result<Object> commitOrUpdatePersonalCertificate(PersonVerifyDto PersonVerifyDto) {

        return iPersonalCertificateService.commitOrUpdatePersonalCertificate(PersonVerifyDto);
    }

    @PostMapping("/getCertificate")
    @ApiOperation("获取个人认证信息(APP)")
    public Result<Object> getCertificate() {

        return iPersonalCertificateService.getCertificate();
    }

    @PostMapping("/findCertInfo")
    @ApiOperation("查询实名认证信息(APP)")
    public  Result<Object> findCertInfo() {

        return iPersonalCertificateService.findCertInfo();
    }
}
