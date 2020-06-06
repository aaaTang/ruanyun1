package cn.ruanyun.backInterface.modules.business.kotlinDemo.serviceimpl

import cn.ruanyun.backInterface.common.utils.PageUtil
import cn.ruanyun.backInterface.common.utils.SecurityUtil
import cn.ruanyun.backInterface.common.utils.ToolUtil
import cn.ruanyun.backInterface.common.vo.PageVo
import cn.ruanyun.backInterface.common.vo.Result
import cn.ruanyun.backInterface.modules.base.pojo.DataVo
import cn.ruanyun.backInterface.modules.business.kotlinDemo.mapper.KotlinDemoMapper
import cn.ruanyun.backInterface.modules.business.kotlinDemo.pojo.KotlinDemo
import cn.ruanyun.backInterface.modules.business.kotlinDemo.service.KotlinDemoService
import cn.ruanyun.backInterface.modules.business.kotlinDemo.vo.KotlinVo
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.core.toolkit.Wrappers
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.NullPointerException
import java.util.*

@Service
class KotlinDemoServiceImp : ServiceImpl<KotlinDemoMapper, KotlinDemo>(), KotlinDemoService {


    @Autowired
    lateinit var securityUtil: SecurityUtil


    override fun saveOrUpdateKotlin(kotlinDemo: KotlinDemo) {

        kotlinDemo.createBy = securityUtil.currUser.id;

        this.saveOrUpdate(kotlinDemo)
    }

    override fun removeKotlin(ids: String) {

        this.removeByIds(ToolUtil.splitterStr(ids))
    }

  /*  override fun getKotlinList(pageVo: PageVo): Result<DataVo<KotlinVo>> {


        val page : Page<Any> = PageUtil.initMpPage(pageVo)

        return
    }*/
}


