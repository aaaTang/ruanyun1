package cn.ruanyun.backInterface.modules.business.storeAudit.service;

import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.dto.StoreListDto;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.business.storeAudit.dto.StoreAuditDTO;
import cn.ruanyun.backInterface.modules.business.storeAudit.vo.StoreAuditListVo;
import cn.ruanyun.backInterface.modules.business.storeAudit.vo.StoreAuditVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.storeAudit.pojo.StoreAudit;

import java.util.List;

/**
 * 商家入驻审核接口
 * @author fei
 */
public interface IStoreAuditService extends IService<StoreAudit> {

    /**
     * 插入或者更新storeAudit
     * @param storeAudit storeAudit
     */
    Result<Object> insertOrderUpdateStoreAudit(StoreAudit storeAudit);

    /**
     * 移除storeAudit
     * @param ids ids
     */
    void removeStoreAudit(String ids);

    /**
     * 后台管理系统审核店铺申请
     *
     * 1.审核通过后，会把该用户的角色信息变更为商家。并开通登录商家后台管理权限
     * @param storeAuditDTO storeAuditDTO
     */
    Result<Object> checkStoreAudit(StoreAuditDTO storeAuditDTO);

    /**
     * 后台获取app获取审核列表,根据审核状态进行筛选
     * @return StoreAuditVO
     */
    List<StoreAuditVo> getStoreAuditList(StoreAuditDTO storeAuditDTO);

    /**
     * app商铺信息
     * @return StoreAuditListVO
     */
    StoreAuditListVo getStoreAudisByid(String id);


    /**
     * 查找所有审核通过的店主id
     * @return String
     */
    List<User> getStoreIdByCheckPass(StoreListDto storeListDto);


    /*-----------------逻辑更改流程---------------------------*/

    /**
     * 获取我的审核记录
     * @return StoreAuditVo
     */
    Result<StoreAuditVo> getMyStoreAudit();

    /**
     * 撤销我的申请
     * @return Object
     */
    Result<Object> cancelStoreAudit();

    /**
     * 更新我的申请
     * @param storeAuditVo 申请参数
     * @return Object
     */
    Result<Object> updateStoreAudit(StoreAuditVo storeAuditVo);



}