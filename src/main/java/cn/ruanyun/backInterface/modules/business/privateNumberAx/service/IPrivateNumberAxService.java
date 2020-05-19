package cn.ruanyun.backInterface.modules.business.privateNumberAx.service;

import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.business.privateNumberAx.pojo.PrivateNumberAx;
import cn.ruanyun.backInterface.modules.business.privateNumberAx.vo.PrivateNumberAxVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 华为隐私通话接口
 * @author z
 */
public interface IPrivateNumberAxService{


    /**
     * 通过商家和用户查询虚拟号
     * @param storeId 商家id
     * @return 虚拟号段
     */
    Result<Object> getPrivateNumByStoreIdAndUseId(String storeId);


    /**
     * 获取虚拟号段绑定关系列表
     * @param pageVo 分页参数
     * @return PrivateNumberAxVo
     */
    DataVo<PrivateNumberAxVo> getPrivateNumberAxVoList(PageVo pageVo);


    /**
     * 接触虚拟号段绑定关系
     * @param id id
     */
    Result<Object> unbindPrivateNum(String id);
}