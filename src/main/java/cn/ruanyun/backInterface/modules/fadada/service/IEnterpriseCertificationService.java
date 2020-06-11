package cn.ruanyun.backInterface.modules.fadada.service;

import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.fadada.dto.CompanyVerifyDto;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.fadada.pojo.EnterpriseCertification;

import java.util.List;

/**
 * 企业认证接口
 * @author z
 */
public interface IEnterpriseCertificationService extends IService<EnterpriseCertification> {


    /**
     * 提交企业认证
     * @param CompanyVerifyDto CompanyVerifyDto
     * @return Object
     */
    Result<Object> commitOrUpdateEnterpriseCertification(CompanyVerifyDto CompanyVerifyDto);



}