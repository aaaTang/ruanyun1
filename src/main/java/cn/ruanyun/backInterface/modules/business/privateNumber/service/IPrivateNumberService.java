package cn.ruanyun.backInterface.modules.business.privateNumber.service;

import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.business.privateNumber.vo.PrivateNumberVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.privateNumber.pojo.PrivateNumber;

import java.util.List;

/**
 * 虚拟号接口
 * @author z
 */
public interface IPrivateNumberService extends IService<PrivateNumber> {


    /**
     * 添加或者修改虚拟号段
     * @param privateNumber 实体
     */
    void insertOrUpdatePrivateNumber(PrivateNumber privateNumber);


    /**
     * 移除虚拟号段
     * @param ids ids集合
     */
    void removePrivateNumber(String ids);


    /**
     * 获取虚拟号段列表
     * @param pageVo 分页参数
     * @return PrivateNumber
     */
    DataVo<PrivateNumberVo> getPrivateNumberList(PageVo pageVo);


    /**
     * 通过id 获取虚拟号
     * @param privateNumberId 虚拟号id
     * @return 虚拟号
     */
    String getPrivateNumber(String privateNumberId);


    /**
     * 获取没有被绑定的虚拟号段
     * @return 虚拟号段
     */
    PrivateNumber getNotBoundPrivateNumber();


    /**
     * 通过虚拟号获取虚拟号id
     * @param privateNumber 虚拟号
     * @return 虚拟号id
     */
    String getIdByPrivateNum(String privateNumber);
}