package cn.ruanyun.backInterface.modules.business.storeAudit.service;

import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.storeAudit.DTO.StoreAuditDTO;
import cn.ruanyun.backInterface.modules.business.storeAudit.VO.StoreAuditListVO;
import cn.ruanyun.backInterface.modules.business.storeAudit.VO.StoreAuditVO;
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
        * @param storeAudit
       */
     void insertOrderUpdateStoreAudit(StoreAudit storeAudit);


      /**
       * 移除storeAudit
       * @param ids
       */
     void removeStoreAudit(String ids);


    /**
     * 后台管理系统审核店铺申请
     *
     * 1.审核通过后，会把该用户的角色信息变更为商家。并开通登录商家后台管理权限
     * @param storeAuditDTO
     */
     Result<Object> checkStoreAudit(StoreAuditDTO storeAuditDTO);


    /**
     * 后台获取app获取审核列表,根据审核状态进行筛选
     * @return
     */
    List<StoreAuditVO> getStoreAuditList(StoreAuditDTO storeAuditDTO);

    /**
     * app商铺信息
     * @return
     */
    StoreAuditListVO getStoreAudisByid(String id);

}