package cn.ruanyun.backInterface.config.security.jwt;

import cn.hutool.core.util.StrUtil;
import cn.ruanyun.backInterface.common.annotation.SystemLog;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.constant.SecurityConstant;
import cn.ruanyun.backInterface.common.enums.LogType;
import cn.ruanyun.backInterface.common.utils.IpInfoUtil;
import cn.ruanyun.backInterface.common.utils.ResponseUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;
import cn.ruanyun.backInterface.common.vo.TokenUser;
import cn.ruanyun.backInterface.config.properties.RuanyunTokenProperties;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IRolePermissionService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 登录成功处理类
 * @author fei
 */
@Slf4j
@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private RuanyunTokenProperties tokenProperties;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRolePermissionService rolePermissionService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserRoleService userRoleService;

    @Override
    @SystemLog(description = "登录系统", type = LogType.LOGIN)
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //用户选择保存登录状态几天
        String saveLogin = request.getParameter(SecurityConstant.SAVE_LOGIN);
        boolean saved = false;
        if(StrUtil.isNotBlank(saveLogin) && Boolean.parseBoolean(saveLogin)){
            saved = true;
            if(!tokenProperties.getRedis()){
                tokenProperties.setTokenExpireTime(tokenProperties.getSaveLoginTime() * 60 * 24);
            }
        }
        String username = ((UserDetails)authentication.getPrincipal()).getUsername();

        List<String> permissionList = Lists.newArrayList();
        //添加权限
        ThreadPoolUtil.getPool().execute(() ->
                Optional.ofNullable(rolePermissionService.getPermissionByRoles(userRoleService
                .getRoleIdsByUserId(userService.getUserIdByName(username))))
                .ifPresent(permissions -> permissions.parallelStream().forEach(permission -> {

                    if(CommonConstant.PERMISSION_OPERATION.equals(permission.getType())
                            && StrUtil.isNotBlank(permission.getTitle())
                            && StrUtil.isNotBlank(permission.getPath())) {
                        permissionList.add(permission.getTitle());
                    }
                })));

        //添加角色
        ThreadPoolUtil.getPool().execute(() ->
                Optional.ofNullable(roleService.getRolesByRoleIds(userRoleService.getRoleIdsByUserId(userService
                .getUserIdByName(username))))
                .ifPresent(roles -> roles.parallelStream().forEach(role ->
                        permissionList.add(role.getName()))));

        // 登陆成功生成token
        String token;
        if(tokenProperties.getRedis()){
            // redis
            token = UUID.randomUUID().toString().replace("-", "");
            TokenUser user = new TokenUser(username, permissionList, saved);

            // 单设备登录 之前的token失效
            if(tokenProperties.getSdl()){
                String oldToken = redisTemplate.opsForValue().get(SecurityConstant.USER_TOKEN + username);
                if(StrUtil.isNotBlank(oldToken)){
                    redisTemplate.delete(SecurityConstant.TOKEN_PRE + oldToken);
                }
            }
            if(saved){
                redisTemplate.opsForValue().set(SecurityConstant.USER_TOKEN + username, token, tokenProperties.getSaveLoginTime(), TimeUnit.DAYS);
                redisTemplate.opsForValue().set(SecurityConstant.TOKEN_PRE + token, new Gson().toJson(user), tokenProperties.getSaveLoginTime(), TimeUnit.DAYS);
            }else{
                redisTemplate.opsForValue().set(SecurityConstant.USER_TOKEN + username, token, tokenProperties.getTokenExpireTime(), TimeUnit.MINUTES);
                redisTemplate.opsForValue().set(SecurityConstant.TOKEN_PRE + token, new Gson().toJson(user), tokenProperties.getTokenExpireTime(), TimeUnit.MINUTES);
            }
        }else{
            // jwt
            token = SecurityConstant.TOKEN_SPLIT + Jwts.builder()
                    //主题 放入用户名
                    .setSubject(username)
                    //自定义属性 放入用户拥有请求权限
                    .claim(SecurityConstant.AUTHORITIES, new Gson().toJson(permissionList))
                    //失效时间
                    .setExpiration(new Date(System.currentTimeMillis() + tokenProperties.getTokenExpireTime() * 60 * 1000))
                    //签名算法和密钥
                    .signWith(SignatureAlgorithm.HS512, SecurityConstant.JWT_SIGN_KEY)
                    .compact();
        }

        if(permissionList.get(0).equals(CommonConstant.DEFAULT_ROLE)){
            ResponseUtil.out(response, ResponseUtil.resultMap(false,201,"登录失败！您无权登陆！", null));
        }else {
            ResponseUtil.out(response, ResponseUtil.resultMap(true,200,"登录成功", token));
        }



    }
}
