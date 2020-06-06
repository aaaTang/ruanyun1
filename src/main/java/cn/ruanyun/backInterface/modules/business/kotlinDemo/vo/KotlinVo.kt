package cn.ruanyun.backInterface.modules.business.kotlinDemo.vo

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.annotations.ApiModelProperty
import org.springframework.format.annotation.DateTimeFormat
import java.util.*

class KotlinVo {

    lateinit var id : String;

    @ApiModelProperty("标题")
    var title : String? = null;

    @ApiModelProperty("内容")
    var content : String? = null;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", hidden = true)
    val createTime: Date? = null
}