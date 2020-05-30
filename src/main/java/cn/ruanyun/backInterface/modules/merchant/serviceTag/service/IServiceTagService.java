package cn.ruanyun.backInterface.modules.merchant.serviceTag.service;

import cn.ruanyun.backInterface.modules.merchant.serviceTag.DTO.ServiceTagDTO;
import cn.ruanyun.backInterface.modules.merchant.serviceTag.VO.ServiceTagVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.merchant.serviceTag.pojo.ServiceTag;

import java.util.List;

/**
 * 优质服务标签接口
 * @author z
 */
public interface IServiceTagService extends IService<ServiceTag> {


      /**
        * 插入或者更新serviceTag
        * @param serviceTag
       */
     void insertOrderUpdateServiceTag(ServiceTag serviceTag);



      /**
       * 移除serviceTag
       * @param ids
       */
     void removeServiceTag(String ids);


    /**
     * 获取优质服务标签列表
     * @param serviceTagDTO 实体类
     * @return
     */
    List<ServiceTagVO> getServiceTag(ServiceTagDTO serviceTagDTO);
}