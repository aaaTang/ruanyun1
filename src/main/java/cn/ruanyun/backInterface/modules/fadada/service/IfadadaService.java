package cn.ruanyun.backInterface.modules.fadada.service;

import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.fadada.dto.*;
import cn.ruanyun.backInterface.modules.fadada.vo.FadadaVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.fadada.pojo.Fadada;

/**
 * 法大大接口
 * @author z
 */
public interface IfadadaService extends IService<Fadada> {


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


    /**
     * 上传合同
     * @param uploaddocsDto 参数
     * @return Object
     * @throws Exception 异常
     */
    Result<Object> uploadDocs(UploaddocsDto uploaddocsDto) throws Exception;


    /**
     * 手动签署
     * @param extsignDto 参数
     * @return Object
     */
    Result<Object> extSign(ExtSignDto extsignDto);


    /**
     * 查看合同
     * @param id 法大大id
     * @return Object
     */
    Result<Object> viewContract(String id);


    /**
     * 下载合同
     * @param id id
     * @return Object
     */
    Result<Object> downLoadContract(String id);


    /**
     * 合同归档
     * @param id id
     * @return Object
     */
    Result<Object> contractFiling(String id);


    /*--------------------------业务部分-----------------------------*/

    /**
     * 获取列表
     * @param pageVo 分页参数
     * @return Page<Fadada>
     */
    Result<DataVo<FadadaVo>> getFadadaList(PageVo pageVo);

}