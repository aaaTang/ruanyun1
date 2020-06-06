package cn.ruanyun.backInterface.modules.business.kotlinDemo.pojo

import cn.ruanyun.backInterface.base.RuanyunBaseEntity
import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum
import com.baomidou.mybatisplus.annotation.TableName
import io.swagger.annotations.ApiModelProperty
import lombok.Data
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "t_kotlin_demo")
@TableName("t_kotlin_demo")
class KotlinDemo : RuanyunBaseEntity() {

    @ApiModelProperty("标题")
    var title : String? = null

    @ApiModelProperty("内容")
    var content : String? = null

}