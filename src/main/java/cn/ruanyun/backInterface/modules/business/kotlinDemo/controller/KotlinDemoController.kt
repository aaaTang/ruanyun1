package cn.ruanyun.backInterface.modules.business.kotlinDemo.controller

import cn.ruanyun.backInterface.common.utils.ResultUtil
import cn.ruanyun.backInterface.common.vo.Result
import cn.ruanyun.backInterface.modules.business.kotlinDemo.pojo.KotlinDemo
import cn.ruanyun.backInterface.modules.business.kotlinDemo.service.KotlinDemoService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/kotlin")
@Api(tags = arrayOf("测试kotlin"))
class KotlinDemoController {

    @Autowired
    lateinit var kotlinDemoService: KotlinDemoService

    @PostMapping("/saveKotlin")
    @ApiOperation("插入kotlin")
    fun saveKotlin(kotlinDemo: KotlinDemo) : Result<Any> {

        kotlinDemoService.saveKotlin(kotlinDemo)

        return ResultUtil<Any>().setSuccessMsg("插入测试成功！")
    }

}