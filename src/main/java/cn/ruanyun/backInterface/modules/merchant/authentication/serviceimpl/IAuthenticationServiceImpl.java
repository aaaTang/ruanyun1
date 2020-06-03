package cn.ruanyun.backInterface.modules.merchant.authentication.serviceimpl;

import cn.hutool.core.util.ObjectUtil;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.CheckEnum;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import cn.ruanyun.backInterface.modules.merchant.authentication.DTO.AuthenticationDTO;
import cn.ruanyun.backInterface.modules.merchant.authentication.mapper.AuthenticationMapper;
import cn.ruanyun.backInterface.modules.merchant.authentication.pojo.Authentication;
import cn.ruanyun.backInterface.modules.merchant.authentication.service.IAuthenticationService;
import cn.ruanyun.backInterface.modules.merchant.authentication.vo.AuthenticationVo;
import cn.ruanyun.backInterface.modules.merchant.trustIdentity.pojo.TrustIdentity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
 * 商家连锁认证接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IAuthenticationServiceImpl extends ServiceImpl<AuthenticationMapper, Authentication> implements IAuthenticationService {


       @Autowired
       private SecurityUtil securityUtil;
       @Resource
       private UserMapper userMapper;
       @Resource
       private IGoodService iGoodService;

       @Override
       public void insertOrderUpdateAuthentication(Authentication authentication) {

           if (ToolUtil.isEmpty(authentication.getCreateBy())) {

                       authentication.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       authentication.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(authentication)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeAuthentication(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }



    /**
     *  审核连锁认证
     * @param authentication
     * @return
     */
    @Override
    public Result<Object> checkAuthentication(Authentication authentication) {

        return CompletableFuture.supplyAsync(() -> Optional.ofNullable(super.getById(authentication.getId())))
                .thenApplyAsync(authentication1 -> authentication1.map(authentication2 -> {

                    //1.审核成功 角色转化
                    if (ObjectUtil.equal(authentication.getStatus(), CheckEnum.CHECK_SUCCESS)) {

                        User user = userMapper.selectById(authentication2.getCreateBy());

                        //连锁认证：品牌商家、品牌联盟
                        user.setAuthenticationTypeEnum(authentication.getAuthenticationTypeEnum());

                        userMapper.updateById(user);
                    }

                    ToolUtil.copyProperties(authentication, authentication2);
                    super.updateById(authentication2);

                    return new ResultUtil<>().setSuccessMsg("审核成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无该数据！")))
                .join();
    }


    @Override
    public List<AuthenticationVo> getAuthentication(AuthenticationDTO authenticationDTO){

        //角色权限
        String  userRole = iGoodService.getRoleUserList(securityUtil.getCurrUser().getId());

        return Optional.ofNullable(ToolUtil.setListToNul(this.list(new QueryWrapper<Authentication>().lambda()

            .eq(ToolUtil.isNotEmpty(authenticationDTO.getId()),Authentication::getId,authenticationDTO.getId())

                //权限是商家或者是个人商家
            .eq( userRole.equals(CommonConstant.PER_STORE)||userRole.equals(CommonConstant.STORE) ,Authentication::getCreateBy,securityUtil.getCurrUser().getId())

            .eq(ToolUtil.isNotEmpty(authenticationDTO.getStatus()),Authentication::getStatus,authenticationDTO.getStatus())

            .eq(ToolUtil.isNotEmpty(authenticationDTO.getAuthenticationTypeEnum()),Authentication::getAuthenticationTypeEnum,authenticationDTO.getAuthenticationTypeEnum())

            .orderByDesc(Authentication::getCreateTime))))

              .map(authentications -> authentications.parallelStream().flatMap(authentication1 -> {

                  AuthenticationVo authenticationVo = new AuthenticationVo();
                  ToolUtil.copyProperties(authentication1,authenticationVo);
                  //商家名称
                  authenticationVo.setShopName(Optional.ofNullable(userMapper.selectById(authentication1.getCreateBy())).map(User::getShopName).orElse(null));


                  return Stream.of(authenticationVo);

              }).collect(Collectors.toList()))
                .orElse(null);
    }



}