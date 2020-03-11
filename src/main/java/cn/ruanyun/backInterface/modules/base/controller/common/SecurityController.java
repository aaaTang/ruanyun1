package cn.ruanyun.backInterface.modules.base.controller.common;

import cn.hutool.http.HttpUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fei
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/common")
@Transactional
public class SecurityController {

    @RequestMapping(value = "/needLogin", method = RequestMethod.GET)
    @ApiOperation(value = "没有登录")
    public Result<Object> needLogin(){

        return new ResultUtil<>().setErrorMsg(401, "您还未登录");
    }

}
