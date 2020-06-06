package cn.ruanyun.backInterface.modules.business.kotlinDemo.serviceimpl

import cn.ruanyun.backInterface.modules.business.kotlinDemo.mapper.KotlinDemoMapper
import cn.ruanyun.backInterface.modules.business.kotlinDemo.pojo.KotlinDemo
import cn.ruanyun.backInterface.modules.business.kotlinDemo.service.KotlinDemoService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.stereotype.Service

@Service
class KotlinDemoServiceImp : ServiceImpl<KotlinDemoMapper, KotlinDemo>(), KotlinDemoService {


    /**
     * 插入数据
     */
    override fun saveKotlin(kotlinDemo: KotlinDemo?) {

        this.save(kotlinDemo)
    }
}