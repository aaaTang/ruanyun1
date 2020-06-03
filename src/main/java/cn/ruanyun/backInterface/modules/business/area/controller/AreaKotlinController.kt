package cn.ruanyun.backInterface.modules.business.area.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

@RestController
@RequestMapping("/kotlin")
class AreaKotlinController {

    var counter = AtomicLong();

    @GetMapping("/demo/{name}")
    fun greeting(@PathVariable name : String) = name;

}