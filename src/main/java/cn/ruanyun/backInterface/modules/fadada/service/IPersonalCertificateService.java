package cn.ruanyun.backInterface.modules.fadada.service;

import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.fadada.dto.PersonVerifyDto;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.fadada.pojo.PersonalCertificate;

import java.util.List;

/**
 * 个人认证接口
 * @author z
 */
public interface IPersonalCertificateService extends IService<PersonalCertificate> {


    /**
     * 提交个人认证信息
     * @param personVerifyDto personVerifyDto
     */
    Result<Object> commitOrUpdatePersonalCertificate(PersonVerifyDto personVerifyDto);


    /**
     * 获取个人或者企业的认证信息
     * @return PersonalCertificate
     */
    Result<Object> getCertificate();

    /**
     * 查询实名认证信息
     * @return Object
     */
    Result<Object> findCertInfo();

}