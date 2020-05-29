package cn.ruanyun.backInterface.modules.business.commonParam.service;

import cn.ruanyun.backInterface.modules.business.commonParam.vo.CommonParamVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.commonParam.pojo.commonParam;

import java.util.List;

/**
 * 公众参数接口
 * @author z
 */
public interface IcommonParamService extends IService<commonParam> {


    /**
     * 更新或者插入公众参数
     * @param commonParam 实体
     */
    void insertOrUpdateCommonParam(commonParam commonParam);


    /**
     * 获取公众参数数据
     * @return CommonParamVo
     */
    CommonParamVo getCommonParamVo();


}