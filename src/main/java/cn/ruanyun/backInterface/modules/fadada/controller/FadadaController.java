package cn.ruanyun.backInterface.modules.fadada.controller;

import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.fadada.dto.*;
import cn.ruanyun.backInterface.modules.fadada.service.IfadadaService;
import cn.ruanyun.backInterface.modules.fadada.vo.FadadaVo;
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

    @PostMapping("/addSignature")
    @ApiOperation("印章上传(APP)")
    public Result<Object> addSignature(SignatureDto signatureDto) throws Exception {

        return fadadaService.addSignature(signatureDto);
    }

}
