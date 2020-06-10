package cn.ruanyun.backInterface.modules.fadada.service;

import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.fadada.dto.*;
import cn.ruanyun.backInterface.modules.fadada.vo.FadadaVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 法大大接口
 * @author z
 */
public interface IfadadaService {


    /*-----------三方接口部分-----------------*/

    /**
     * 注册账号
     * @param openId 用户在接入方的唯一标识
     * @param accountType 1:个人，2:企业
     * @return Object
     */
    String accountRegister(String openId, String accountType);


    /**
     * 获取企业实名认证地址
     * @param companyVerifyDto 参数
     * @return Object
     */
    Result<Object> getCompanyVerifyUrl(CompanyVerifyDto companyVerifyDto);


    /**
     * 获取个人实名认证地址
     * @param personVerifyDto 参数
     * @return Object
     */
    Result<Object> getPersonVerifyUrl(PersonVerifyDto personVerifyDto);


    /**
     * 实名证书申请
     * @return Object
     */
    Result<Object> applyCert();


    /**
     * 印章上传
     * @param signatureDto 签章图片 base64
     * @return Object
     */
    Result<Object> addSignature(SignatureDto signatureDto) throws Exception;

}