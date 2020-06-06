package cn.ruanyun.backInterface.modules.business.kotlinDemo.service

import cn.ruanyun.backInterface.modules.business.kotlinDemo.pojo.KotlinDemo
import com.baomidou.mybatisplus.extension.service.IService
import kotlin.streams.toList

interface KotlinDemoService : IService<KotlinDemo> {

    /**
     * 插入数据
     */
    fun saveKotlin(kotlinDemo: KotlinDemo?)

}