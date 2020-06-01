package cn.ruanyun.backInterface.modules.merchant.trustIdentity.serviceimpl;

import cn.hutool.core.util.ObjectUtil;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.CheckEnum;
import cn.ruanyun.backInterface.common.enums.StoreTypeEnum;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.pojo.UserRole;
import cn.ruanyun.backInterface.modules.business.storeAudit.DTO.StoreAuditDTO;
import cn.ruanyun.backInterface.modules.merchant.authentication.DTO.AuthenticationDTO;
import cn.ruanyun.backInterface.modules.merchant.authentication.pojo.Authentication;
import cn.ruanyun.backInterface.modules.merchant.authentication.vo.AuthenticationVo;
import cn.ruanyun.backInterface.modules.merchant.trustIdentity.DTO.TrustIdentityDTO;
import cn.ruanyun.backInterface.modules.merchant.trustIdentity.VO.TrustIdentityVO;
import cn.ruanyun.backInterface.modules.merchant.trustIdentity.mapper.TrustIdentityMapper;
import cn.ruanyun.backInterface.modules.merchant.trustIdentity.pojo.TrustIdentity;
import cn.ruanyun.backInterface.modules.merchant.trustIdentity.service.ITrustIdentityService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;

import javax.annotation.Resource;


/**
 * 商家信任标识接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class ITrustIdentityServiceImpl extends ServiceImpl<TrustIdentityMapper, TrustIdentity> implements ITrustIdentityService {


       @Autowired
       private SecurityUtil securityUtil;

       @Resource
       private UserMapper  userMapper;

       @Override
       public void insertOrderUpdateTrustIdentity(TrustIdentity trustIdentity) {

           if (ToolUtil.isEmpty(trustIdentity.getCreateBy())) {

                       trustIdentity.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       trustIdentity.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(trustIdentity)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeTrustIdentity(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    /**
     *  审核信任标识
     * @param trustIdentity
     * @return
     */
    @Override
    public Result<Object> checkTrustIdentity(TrustIdentity trustIdentity) {

        return CompletableFuture.supplyAsync(() -> Optional.ofNullable(super.getById(trustIdentity.getId())))
                .thenApplyAsync(identity -> identity.map(trustIdentity1 -> {

                    //1.审核成功 角色转化
                    if (ObjectUtil.equal(trustIdentity.getStatus(), CheckEnum.CHECK_SUCCESS)) {

                       User user = userMapper.selectById(trustIdentity1.getCreateBy());
                       user.setTrustIdentity(1);

                       userMapper.updateById(user);
                    }

                    ToolUtil.copyProperties(trustIdentity, trustIdentity1);
                    super.updateById(trustIdentity1);

                    return new ResultUtil<>().setSuccessMsg("审核成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无该数据！")))
                .join();
    }



    /**
     * 获取申请信任标识列表
     * @param trustIdentityDTO 实体类
     * @return
     */
    @Override
    public List<TrustIdentityVO> getTrustIdentity(TrustIdentityDTO trustIdentityDTO){


        return Optional.ofNullable(ToolUtil.setListToNul(this.list(new QueryWrapper<TrustIdentity>().lambda()
                .eq(ToolUtil.isNotEmpty(trustIdentityDTO.getId()),TrustIdentity::getId,trustIdentityDTO.getId())
                .eq(ToolUtil.isNotEmpty(trustIdentityDTO.getStatus()),TrustIdentity::getStatus,trustIdentityDTO.getStatus()))))

                .map(trustIdentities -> trustIdentities.parallelStream().flatMap(trustIdentity -> {

                    TrustIdentityVO trustIdentityVO = new TrustIdentityVO();
                    ToolUtil.copyProperties(trustIdentity,trustIdentityVO);
                    //商家名称
                    trustIdentityVO.setShopName(Optional.ofNullable(userMapper.selectById(trustIdentity.getCreateBy())).map(User::getShopName).orElse(null));

                    return Stream.of(trustIdentityVO);

                }).collect(Collectors.toList()))
                .orElse(null);
    }







}