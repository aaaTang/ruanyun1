package cn.ruanyun.backInterface.modules.fadada.service;

import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.fadada.dto.UploaddocsDto;
import cn.ruanyun.backInterface.modules.fadada.vo.ElectronicContractVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.fadada.pojo.ElectronicContract;

import java.util.List;

/**
 * 电子合同接口
 * @author z
 */
public interface IElectronicContractService extends IService<ElectronicContract> {


    /**
     * 添加
     * @param uploaddocsDto 电子合同
     * @return Object
     */
    Result<Object> insertElectronicContract(UploaddocsDto uploaddocsDto);

    /**
     * 移除电子合同
     * @param ids ids
     * @return Object
     */
    Result<Object> removeElectronicContract(String ids);

    /**
     * 获取电子合同列表
     * @param pageVo 分页
     * @return 电子合同vo
     */
    Result<DataVo<ElectronicContractVo>> getElectronicContractList(PageVo pageVo);

    /**
     * 随机获取一个合同编号
     * @return 合同编号
     */
    String getContractIdByRandom();

    /**
     * 修改合同归档状态
     * @param contractId 合同编号id
     */
    void updateContractFiling(String contractId);


}