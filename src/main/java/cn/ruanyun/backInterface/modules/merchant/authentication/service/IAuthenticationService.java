package cn.ruanyun.backInterface.modules.merchant.authentication.service;

import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.merchant.authentication.DTO.AuthenticationDTO;
import cn.ruanyun.backInterface.modules.merchant.authentication.vo.AuthenticationVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.merchant.authentication.pojo.Authentication;

import java.util.List;

/**
 * 商家连锁认证接口
 * @author z
 */
public interface IAuthenticationService extends IService<Authentication> {


    /**
     * 插入或者更新authentication
     * @param authentication
     */
    void insertOrderUpdateAuthentication(Authentication authentication);

    /**
     * 移除authentication
     * @param ids
     */
    void removeAuthentication(String ids);

    /**
     * 审核申请
     */
    Result<Object> checkAuthentication(Authentication authentication);


    /**
     * 获取申请连锁认证列表
     */
    List<AuthenticationVo> getAuthentication(AuthenticationDTO authenticationDTO);
}