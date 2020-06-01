package cn.ruanyun.backInterface.modules.merchant.trustIdentity.service;

import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.merchant.trustIdentity.DTO.TrustIdentityDTO;
import cn.ruanyun.backInterface.modules.merchant.trustIdentity.VO.TrustIdentityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.merchant.trustIdentity.pojo.TrustIdentity;

import java.util.List;

/**
 * 商家信任标识接口
 * @author z
 */
public interface ITrustIdentityService extends IService<TrustIdentity> {


      /**
        * 插入或者更新trustIdentity
        * @param trustIdentity
       */
     void insertOrderUpdateTrustIdentity(TrustIdentity trustIdentity);



      /**
       * 移除trustIdentity
       * @param ids
       */
     void removeTrustIdentity(String ids);


    /**
     *  审核信任标识
     * @param trustIdentity 实体类
     * @return
     */
    Result<Object> checkTrustIdentity(TrustIdentity trustIdentity);


    /**
     * 获取申请信任标识列表
     * @param trustIdentityDTO 实体类
     * @return
     */
    List<TrustIdentityVO> getTrustIdentity(TrustIdentityDTO trustIdentityDTO);
}