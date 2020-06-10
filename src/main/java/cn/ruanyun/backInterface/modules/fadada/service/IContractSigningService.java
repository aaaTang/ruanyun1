package cn.ruanyun.backInterface.modules.fadada.service;

import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.fadada.dto.ExtSignDto;
import cn.ruanyun.backInterface.modules.fadada.vo.ContractSigningVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.fadada.pojo.ContractSigning;

import java.util.List;

/**
 * 合同签署接口
 * @author z
 */
public interface IContractSigningService extends IService<ContractSigning> {


    /**
     * 获取客户的审核数据
     * @return  ContractSigningVo
     */
    Result<ContractSigningVo> getCustomerSigning();


    /**
     * 客户手动签署合同
     * @param extsignDto 参数
     * @return Object
     */
    Result<Object> extSign(ExtSignDto extsignDto);


    /**
     * 客户手动修改签署合同
     * @param extsignDto 参数
     * @return Object
     */
    Result<Object> updateExtSign(ExtSignDto extsignDto);



    /**
     * 获取审核列表
     * @param pageVo 分页
     * @return ContractSigningVo
     */
    Result<DataVo<ContractSigningVo>> getContractSigningList(PageVo pageVo);



    /*-------------------------------操作----------------------------------*/

    /**
     * 后台审核手动签署合同
     * @param contractSigning contractSigning
     * @return Object
     */
    Result<Object> checkContractSigning(ContractSigning contractSigning);


    /**
     * 合同归档
     * @param id id
     * @return Object
     */
    Result<Object> contractFiling(String id);


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

}