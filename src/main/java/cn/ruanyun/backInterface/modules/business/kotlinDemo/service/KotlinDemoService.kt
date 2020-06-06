package cn.ruanyun.backInterface.modules.business.kotlinDemo.service

import cn.ruanyun.backInterface.common.vo.PageVo
import cn.ruanyun.backInterface.common.vo.Result
import cn.ruanyun.backInterface.modules.base.pojo.DataVo
import cn.ruanyun.backInterface.modules.business.kotlinDemo.pojo.KotlinDemo
import cn.ruanyun.backInterface.modules.business.kotlinDemo.vo.KotlinVo
import com.baomidou.mybatisplus.extension.service.IService

interface KotlinDemoService : IService<KotlinDemo> {

    /**
     * 插入或者更新数据
     */
    fun saveOrUpdateKotlin(kotlinDemo: KotlinDemo)

    /**
     * 移除kotlin
     */
    fun removeKotlin(ids : String)

    /**
     * 获取插入列表
     *//*
    fun getKotlinList(pageVo: PageVo) : Result<DataVo<KotlinVo>>*/

}